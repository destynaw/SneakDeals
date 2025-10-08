package com.example.sneakdeals.ui.screens.chat

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
import com.example.sneakdeals.data.model.Store
import com.example.sneakdeals.data.model.allStores
import com.example.sneakdeals.ui.theme.SneakDealsTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Data class untuk satu pesan
data class Message(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(store: Store, navController: NavController) {
    val messages = remember {
        mutableStateListOf(Message("Halo! Ada yang bisa kami bantu terkait ${store.name}?", false, getCurrentTimestamp()))
    }
    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Inisialisasi TextToSpeech
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

    fun getBotResponse(userInput: String): String {
        val lowercasedInput = userInput.lowercase()
        return when {
            lowercasedInput.contains("jam") || lowercasedInput.contains("buka") || lowercasedInput.contains("tutup") -> {
                "Tentu, toko kami buka ${store.hours}."
            }
            lowercasedInput.contains("promo") || lowercasedInput.contains("diskon") -> {
                if (store.promotions.isNotEmpty()) {
                    "Ada promo menarik! ${store.promotions.joinToString(", ")}."
                } else {
                    "Saat ini belum ada promosi spesifik di toko ini, tapi Anda bisa cek penawaran umum di halaman utama."
                }
            }
            lowercasedInput.contains("alamat") || lowercasedInput.contains("lokasi") || lowercasedInput.contains("mana") -> {
                "Kami berada di ${store.address}."
            }
            else -> {
                "Maaf, saya belum mengerti pertanyaan itu. Anda bisa tanyakan seputar jam buka, promosi, atau alamat."
            }
        }
    }

    fun handleSendMessage() {
        if (textInput.isNotBlank()) {
            val userMessage = textInput
            messages.add(Message(userMessage, true, getCurrentTimestamp()))
            val botResponse = getBotResponse(userMessage)
            messages.add(Message(botResponse, false, getCurrentTimestamp()))
            textInput = ""
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(store.name, fontWeight = FontWeight.Bold) },
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
            MessageInput(
                value = textInput,
                onValueChange = { textInput = it },
                onSend = { handleSendMessage() }
            )
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
fun MessageBubble(message: Message, onSpeak: (String) -> Unit) {
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
                placeholder = { Text("Ketik pesan...") },
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

fun getCurrentTimestamp(): String {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    SneakDealsTheme {
        ChatScreen(store = allStores.first(), navController = rememberNavController())
    }
}
