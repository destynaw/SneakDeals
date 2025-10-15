package com.example.sneakdeals.ui.screens.globalchat

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.data.model.allStores
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.screens.kategori.Product
import com.example.sneakdeals.ui.screens.kategori.allProducts
import com.example.sneakdeals.ui.theme.SneakDealsTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class MessageType {
    TEXT,
    TOP_PRODUCTS
}

// Data class untuk satu pesan
data class Message(
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String,
    val type: MessageType = MessageType.TEXT,
    val products: List<Product> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlobalChatScreen(navController: NavController) {
    val messages = remember {
        mutableStateListOf(Message("Hai! Ada yang bisa saya bantu? Tanyakan tentang lokasi toko atau produk terlaris!", false, getCurrentTimestamp()))
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

    fun getBotResponse(userInput: String): Message {
        val lowercasedInput = userInput.lowercase(Locale.getDefault())
        return when {
            "top 10" in lowercasedInput || "paling laku" in lowercasedInput || "terlaris" in lowercasedInput -> {
                val topProducts = allProducts.sortedByDescending { it.soldCount }.take(10)
                Message(
                    text = "Tentu, ini 10 produk terlaris kami saat ini:",
                    isFromUser = false,
                    timestamp = getCurrentTimestamp(),
                    type = MessageType.TOP_PRODUCTS,
                    products = topProducts
                )
            }
            "toko" in lowercasedInput || "lokasi" in lowercasedInput -> {
                val foundStore = allStores.find { it.city.lowercase(Locale.getDefault()) in lowercasedInput }
                if (foundStore != null) {
                    Message("Toko kami di ${foundStore.city} berada di ${foundStore.address}, buka pada jam ${foundStore.hours}.", false, getCurrentTimestamp())
                } else {
                    Message("Untuk informasi lokasi toko, silakan sebutkan kota yang ingin Anda cari, misalnya: 'toko di Bandung'", false, getCurrentTimestamp())
                }
            }
            else -> {
                Message("Maaf, saya belum mengerti. Anda bisa bertanya tentang 'produk terlaris' atau 'toko di [kota]'.", false, getCurrentTimestamp())
            }
        }
    }

    fun handleSendMessage() {
        if (textInput.isNotBlank()) {
            val userMessage = textInput
            messages.add(Message(userMessage, true, getCurrentTimestamp()))
            val botResponse = getBotResponse(userMessage)
            messages.add(botResponse)
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
                    },
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, onSpeak: (String) -> Unit, onProductClick: (String) -> Unit) {
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleShape = if (message.isFromUser) RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
                      else RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (message.isFromUser) Alignment.End else Alignment.Start,
            modifier = if(message.type == MessageType.TEXT) Modifier.widthIn(max = 300.dp) else Modifier.fillMaxWidth()
        ) {
             val backgroundColor = if (message.isFromUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
             val textColor = if (message.isFromUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer

            if (message.text.isNotEmpty()) {
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
            }

            if (message.type == MessageType.TOP_PRODUCTS) {
                Spacer(modifier = Modifier.height(12.dp))
                TopProductsCarousel(products = message.products, onProductClick = onProductClick)
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

@Composable
fun TopProductsCarousel(products: List<Product>, onProductClick: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp) // Added vertical padding
    ) {
        items(products) { product ->
            ProductCarouselItem(product = product, onClick = { onProductClick(product.id) })
        }
    }
}

@Composable
fun ProductCarouselItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(230.dp) // Set a fixed height for the entire card
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxHeight(), // Allow this column to fill the remaining height
                verticalArrangement = Arrangement.SpaceBetween // Pushes name to top, price to bottom
            ) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, 
                    minLines = 2, // Reserve space for 2 lines to ensure alignment
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Text(
                    text = "Rp${String.format("%,.0f", product.price)}",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
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
                placeholder = { Text("Tanya lokasi atau produk...") },
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
fun GlobalChatScreenPreview() {
    SneakDealsTheme {
        GlobalChatScreen(navController = rememberNavController())
    }
}
