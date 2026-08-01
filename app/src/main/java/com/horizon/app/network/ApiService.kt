package com.horizon.app.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<AuthResponse>

    @GET("api/auth/me")
    suspend fun getMe(@Header("Authorization") token: String): Response<User>

    @GET("api/listings")
    suspend fun getListings(
        @Query("category") category: String? = null,
        @Query("city") city: String? = null,
        @Query("q") search: String? = null
    ): Response<List<Listing>>

    @GET("api/listings/{id}")
    suspend fun getListing(@Path("id") id: Int): Response<Listing>

    @Multipart
    @POST("api/listings")
    suspend fun createListing(
        @Header("Authorization") token: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("price") price: RequestBody?,
        @Part("city") city: RequestBody?,
        @Part media: MultipartBody.Part?
    ): Response<Listing>

    @DELETE("api/listings/{id}")
    suspend fun deleteListing(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Unit>

    @GET("api/my-listings")
    suspend fun getMyListings(@Header("Authorization") token: String): Response<List<Listing>>

    @GET("api/conversations")
    suspend fun getConversations(@Header("Authorization") token: String): Response<List<Conversation>>

    @GET("api/messages/{userId}")
    suspend fun getConversation(
        @Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<List<Message>>

    @POST("api/messages")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body body: SendMessageRequest
    ): Response<Message>
}
