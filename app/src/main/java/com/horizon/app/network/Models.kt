package com.horizon.app.network

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val phone: String?,
    val city: String?,
    val bio: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("created_at") val createdAt: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class RegisterRequest(
    @SerializedName("full_name") val fullName: String,
    val email: String,
    val password: String,
    val phone: String? = null,
    val city: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class Listing(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("author_name") val authorName: String?,
    val title: String,
    val description: String,
    val category: String,
    val price: Double?,
    val city: String?,
    @SerializedName("media_url") val mediaUrl: String?,
    @SerializedName("media_type") val mediaType: String?,
    val status: String,
    val views: Int,
    @SerializedName("created_at") val createdAt: String
)

data class Message(
    val id: Int,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("listing_id") val listingId: Int?,
    val content: String,
    @SerializedName("is_read") val isRead: Boolean,
    @SerializedName("created_at") val createdAt: String
)

data class SendMessageRequest(
    @SerializedName("receiver_id") val receiverId: Int,
    val content: String,
    @SerializedName("listing_id") val listingId: Int? = null
)

data class Conversation(
    val user: User?,
    @SerializedName("last_message") val lastMessage: Message
)

object Categories {
    val ALL = listOf("Emploi", "Service", "Produit", "Immobilier", "Véhicule", "Autre")
}
