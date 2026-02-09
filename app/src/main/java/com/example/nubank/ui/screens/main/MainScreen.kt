package com.example.nubank.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val context = LocalContext.current

    // States
    val link by viewModel.link.collectAsState()
    val erro by viewModel.erro.collectAsState()
    val history by viewModel.history.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(erro) {
        if (erro.isNotBlank()) {
            Toast.makeText(context, erro, Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(48.dp)) // ✅ Aumentado de 32dp para 48dp

        // ✅ Ícone menor (de 64sp para 48sp)
        Text(
            text = "🔗",
            fontSize = 48.sp, // ✅ Diminuído
            modifier = Modifier.padding(bottom = 24.dp) // ✅ Mais espaço abaixo
        )

        // Título
        Text(
            text = "ENCURTAR LINKS",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 40.dp) // ✅ Aumentado de 32dp para 40dp
        )

        // Input
        OutlinedTextField(
            value = link,
            onValueChange = viewModel::onLinkChange,
            label = { Text("Insira aqui sua URL") },
            singleLine = true,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp)) // ✅ Aumentado de 16dp para 24dp

        // Botão
        Button(
            onClick = viewModel::onShortenClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = link.isNotBlank() && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Encurtar", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp)) // ✅ Aumentado de 32dp para 40dp

        // Histórico
        if (history.isNotEmpty()) {
            Text(
                text = "Histórico",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history) { item ->
                    HistoryItem(item) // ✅ Agora com botão de copiar
                }
            }
        }
    }
}

@Composable
private fun HistoryItem(item: ShortenedLink) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // ✅ Textos à esquerda
        Column(
            modifier = Modifier.weight(1f) // Ocupa o espaço disponível
        ) {
            Text(
                text = item.shortUrl,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.originalUrl,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ✅ Botão de copiar à direita
        IconButton(
            onClick = {
                clipboardManager.setText(AnnotatedString(item.shortUrl))
                Toast.makeText(context, "Link copiado!", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy, contentDescription = "Copiar link", tint = MaterialTheme.colorScheme.primary)
        }
    }
}