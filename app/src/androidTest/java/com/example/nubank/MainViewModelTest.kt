package com.example.nubank

import com.example.nubank.ui.screens.main.MainViewModel
import com.example.nubank.ui.screens.main.NubankApi
import com.example.nubank.ui.screens.main.CreateNubankRequest
import com.example.nubank.ui.screens.main.CreateNubankResponse
import com.example.nubank.ui.screens.main.GetNubankResponse
import com.example.nubank.ui.screens.main.LinksResponse
import org.junit.Test
import org.junit.Assert.*

class MainViewModelTest {

    // API fake bem simples
    private class FakeNubankApi : NubankApi {
        override suspend fun getURL(id: String): GetNubankResponse {
            return GetNubankResponse("https://google.com")
        }

        override suspend fun createURL(request: CreateNubankRequest): CreateNubankResponse {
            return CreateNubankResponse(
                alias = "12345",
                url = request.url,
                _links = LinksResponse(
                    self = request.url,
                    short = "https://short.url/12345"
                )
            )
        }
    }

    @Test
    fun testLinkVazioDeveMostrarErro() { // ✅ SEM crases
        val viewModel = MainViewModel(FakeNubankApi())
        viewModel.onShortenClick()
        assertEquals("Preencha com o link", viewModel.erro.value)
    }

    @Test
    fun testLinkInvalidoDeveMostrarErro() { // ✅ SEM crases
        val viewModel = MainViewModel(FakeNubankApi())
        viewModel.onLinkChange("google")
        viewModel.onShortenClick()
        assertEquals("Link inválido", viewModel.erro.value)
    }

    @Test
    fun testDigitarDeveLimparErroAnterior() { // ✅ SEM crases
        val viewModel = MainViewModel(FakeNubankApi())
        viewModel.onShortenClick()
        viewModel.onLinkChange("https://google.com")
        assertEquals("", viewModel.erro.value)
    }
}