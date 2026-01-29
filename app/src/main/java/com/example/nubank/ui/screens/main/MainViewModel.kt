package com.example.nubank.ui.screens.main

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var link = mutableStateOf("")
        private set

    var history = mutableStateOf<List<ShortenedLink>>(emptyList())
        private set

    fun onLinkChange(newLink: String) {
        link.value = newLink
    }

    fun onShortenClick(): LinkResult {
        val currentLink = link.value.trim()

        return when {
            currentLink.isBlank() -> {
                LinkResult.EmptyFields
            }

            !Patterns.WEB_URL.matcher(currentLink).matches() -> {
                LinkResult.InvalidLink
            }

            else -> {
                val fakeShortUrl = "https://short.ly/${currentLink.hashCode()}"

                history.value = listOf(
                    ShortenedLink(
                        originalUrl = currentLink,
                        shortUrl = fakeShortUrl
                    )
                ) + history.value

                link.value = "" // limpa o campo

                LinkResult.Success(currentLink)
            }
        }
    }
}

sealed class LinkResult {
    object EmptyFields : LinkResult()
    object InvalidLink : LinkResult()
    data class Success(val link: String) : LinkResult()
}