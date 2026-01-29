package com.example.nubank.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "ENCURTADOR DE LINK",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = viewModel.link.value,
            onValueChange = viewModel::onLinkChange,
            label = { Text("Insira aqui sua URL") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when (viewModel.onShortenClick()) {
                    LinkResult.EmptyFields -> {
                        Toast.makeText(
                            context,
                            "Preencha com o link",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    LinkResult.InvalidLink -> {
                        Toast.makeText(
                            context,
                            "Link inválido",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is LinkResult.Success -> {
                        Toast.makeText(
                            context,
                            "Link encurtado!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = viewModel.link.value.isNotBlank()
        ) {
            Text("Encurtar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (viewModel.history.value.isNotEmpty()) {
            Text(
                text = "Histórico",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.history.value) { item ->
                Column {
                    Text(
                        text = item.shortUrl,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = item.originalUrl,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}