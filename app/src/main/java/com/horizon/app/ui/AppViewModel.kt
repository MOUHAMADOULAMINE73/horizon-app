package com.horizon.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.horizon.app.data.TokenManager
import com.horizon.app.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application)
    private val api = RetrofitClient.api

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _listings = MutableStateFlow<List<Listing>>(emptyList())
    val listings: StateFlow<List<Listing>> = _listings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        checkExistingSession()
    }

    private fun checkExistingSession() {
        viewModelScope.launch {
            val header = tokenManager.getAuthHeader()
            if (header != null) {
                val response = api.getMe(header)
                if (response.isSuccessful) {
                    _currentUser.value = response.body()
                    _isLoggedIn.value = true
                }
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun register(fullName: String, email: String, password: String, city: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.register(RegisterRequest(fullName, email, password, city = city))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveSession(body.token, body.user.id, body.user.fullName)
                    _currentUser.value = body.user
                    _isLoggedIn.value = true
                    onSuccess()
                } else {
                    _errorMessage.value = "Inscription impossible. Vérifie tes informations."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Impossible de contacter le serveur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    tokenManager.saveSession(body.token, body.user.id, body.user.fullName)
                    _currentUser.value = body.user
                    _isLoggedIn.value = true
                    onSuccess()
                } else {
                    _errorMessage.value = "Email ou mot de passe incorrect."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Impossible de contacter le serveur : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearSession()
            _currentUser.value = null
            _isLoggedIn.value = false
        }
    }

    fun loadListings(category: String? = null, search: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getListings(category = category, search = search)
                if (response.isSuccessful) {
                    _listings.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Impossible de charger les annonces : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun publishListing(
        title: String,
        description: String,
        category: String,
        price: String,
        city: String,
        mediaFile: File?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val header = tokenManager.getAuthHeader() ?: return@launch
                val mediaPart = mediaFile?.let {
                    val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("media", it.name, requestFile)
                }
                val response = api.createListing(
                    token = header,
                    title = title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    description = description.toRequestBody("text/plain".toMediaTypeOrNull()),
                    category = category.toRequestBody("text/plain".toMediaTypeOrNull()),
                    price = if (price.isNotBlank()) price.toRequestBody("text/plain".toMediaTypeOrNull()) else null,
                    city = if (city.isNotBlank()) city.toRequestBody("text/plain".toMediaTypeOrNull()) else null,
                    media = mediaPart
                )
                if (response.isSuccessful) {
                    onSuccess()
                    loadListings()
                } else {
                    _errorMessage.value = "Impossible de publier l'annonce."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la publication : ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
