package com.example.nubank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.nubank.ui.screens.main.MainScreen
import com.example.nubank.ui.screens.main.MainViewModel
import com.example.nubank.ui.screens.main.NubankApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    // by lazy: cria algo só quando precisa utilizar, ou seja, não cria objetos pesados se você não usar
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://url-shortener-server.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val nubankApi by lazy {
        retrofit.create(NubankApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = MainViewModel(nubankApi)

        setContent {
            MainScreen(viewModel)
        }
    }
}