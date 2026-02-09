package com.example.nubank.ui.screens.main

// Requests
data class CreateNubankRequest(val url: String)

// Responses
data class GetNubankResponse(val url: String)

data class CreateNubankResponse(
    val alias: String,
    val url: String,
    val _links: LinksResponse
)

data class LinksResponse(
    val self: String,
    val short: String
)