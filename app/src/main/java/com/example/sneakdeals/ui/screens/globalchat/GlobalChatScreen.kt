package com.example.sneakdeals.ui.screens.globalchat

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.data.model.allStores
import com.example.sneakdeals.ui.theme.SneakDealsTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Data class untuk satu pesan
data class GlobalMessage(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String
)

// Fungsi untuk mendapatkan timestamp saat ini
private fun getCurrentTimestamp(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}

// Logika Cerdas untuk Chatbot Global
private fun getBotResponse(userInput: String): String {
    val lowercasedInput = userInput.lowercase(Locale.ROOT)
    val cities = allStores.map { it.city.split(": ").last().lowercase(Locale.ROOT) }.distinct()
    val foundCity = cities.find { city -> lowercasedInput.contains(city) }

    return when {
        lowercasedInput.contains("toko") || lowercasedInput.contains("store") -> {
            if (foundCity != null) {
                val storesInCity = allStores
                    .filter { it.city.lowercase(Locale.ROOT).contains(foundCity) }
                    .map { it.name }
                if (storesInCity.isNotEmpty()) {
                    "Tentu, kami menemukan beberapa toko di ${foundCity.replaceFirstChar { it.uppercase() }}:\n- ${storesInCity.joinToString("\n- ")}"
                } else {
                    "Maaf, kami tidak menemukan toko di ${foundCity.replaceFirstChar { it.uppercase() }}."
                }
            } else {
                "Bisa sebutkan kota yang Anda cari? Misalnya: 'toko di Jakarta'."
            }
        }
        lowercasedInput.contains("halo") || lowercasedInput.contains("hai") -> {
            "Hai! Ada yang bisa saya bantu? Anda bisa bertanya tentang lokasi toko."
        }
        else -> {
            "Maaf, saya belum mengerti. Anda bisa bertanya tentang lokasi toko, misalnya: 'ada toko di Bandung?'"
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalChatScreen(navController: NavController) {
    val messages = remember {
        mutableStateListOf(GlobalMessage("Hai! Ada yang bisa saya bantu? Tanyakan tentang lokasi toko di seluruh Indonesia.", false, getCurrentTimestamp()))
    }
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    val context = LocalContext.current
    val textToSpeech = remember {
        TextToSpeech(context, null)
    }
    DisposableEffect(Unit) {
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    fun handleSendMessage() {
        if (textInput.isNotBlank()) {
            val userMessage = textInput
            messages.add(GlobalMessage(userMessage, true, getCurrentTimestamp()))
            val botResponse = getBotResponse(userMessage)
            messages.add(GlobalMessage(botResponse, false, getCurrentTimestamp()))
            textInput = ""
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asisten Chat", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implementasi Speech-to-Text */ }) {
                        Icon(Icons.Default.Mic, contentDescription = "Input Suara")
                    }
                }
            )
        },
        bottomBar = {
            MessageInput(value = textInput, onValueChange = { textInput = it }, onSend = { handleSendMessage() })
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    onSpeak = { text ->
                        textToSpeech.language = Locale("id", "ID")
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: GlobalMessage, onSpeak: (String) -> Unit) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val backgroundColor = if (message.isFromUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
    val textColor = if (message.isFromUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
    val bubbleShape = if (message.isFromUser) {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
             Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (!message.isFromUser) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Dengarkan pesan",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onSpeak(message.text) },
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(bubbleShape)
                        .background(backgroundColor)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(message.text, color = textColor)
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                message.timestamp,
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(value: String, onValueChange: (String) -> Unit, onSend: () -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Tanya lokasi toko...") },
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                enabled = value.isNotBlank(),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(Icons.Default.Send, contentDescription = "Kirim Pesan")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GlobalChatScreenPreview() {
    SneakDealsTheme {
        GlobalChatScreen(navController = rememberNavController())
    }
}
