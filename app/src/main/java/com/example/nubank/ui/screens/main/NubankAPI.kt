package com.example.nubank.ui.screens.main

import retrofit2.http.*

interface NubankApi {
    @GET("api/alias/{id}")
    suspend fun getURL(@Path("id") id: String): GetNubankResponse

    @POST("api/alias")
    suspend fun createURL(@Body request: CreateNubankRequest): CreateNubankResponse
}