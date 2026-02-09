package com.example.nubank.ui.screens.main

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val nubankApi: NubankApi) : ViewModel() {

    // States
    private val _link = MutableStateFlow("")
    val link = _link.asStateFlow()

    private val _erro = MutableStateFlow("")
    val erro = _erro.asStateFlow()

    private val _history = MutableStateFlow<List<ShortenedLink>>(emptyList())
    val history = _history.asStateFlow()

    // Necessário para o usuário saber se a API está processando
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Actions
    fun onLinkChange(newLink: String) {
        _link.update { newLink }
        if (_erro.value.isNotBlank()) {
            _erro.update { "" }
        }
    }

    fun onShortenClick() {
        val currentLink = _link.value.trim()

        when {
            currentLink.isBlank() -> {
                _erro.update { "Preencha com o link" }
            }

            !Patterns.WEB_URL.matcher(currentLink).matches() -> {
                _erro.update { "Link inválido" }
            }

            else -> {
                createRequest(currentLink)
                _link.update { "" }
            }
        }
    }

    // Private methods
    private fun createRequest(url: String) {
        viewModelScope.launch {
            _isLoading.update { true }

            try {
                val response = nubankApi.createURL(
                    CreateNubankRequest(url = url)
                )


                _history.update {
                    listOf(
                        ShortenedLink(
                            originalUrl = url,
                            shortUrl = response._links.short
                        )
                    ) + it
                }

                _erro.update { "" }

            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "Erro completo:", e)
                _erro.update { "Erro: ${e.message}" }

            } finally {
                _isLoading.update { false }
            }
        }
    }
}