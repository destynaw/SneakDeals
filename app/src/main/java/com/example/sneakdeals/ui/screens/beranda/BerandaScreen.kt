package com.example.sneakdeals.ui.screens.beranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.R
import com.example.sneakdeals.data.model.cartItems
import com.example.sneakdeals.data.model.wishlistItems
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.screens.kategori.Product
import com.example.sneakdeals.ui.screens.kategori.allProducts
import com.example.sneakdeals.ui.theme.SneakDealsTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val searchResults = if (searchQuery.isNotBlank()) {
        allProducts.filter { it.name.contains(searchQuery, ignoreCase = true) }
    } else {
        null
    }

    Scaffold(
        topBar = { BerandaTopAppBar(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.GlobalChat.route) },
                shape = CircleShape,
                containerColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Filled.ChatBubble,
                    contentDescription = "Asisten Chat",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { 
                Spacer(Modifier.height(16.dp))
                SearchBarSection(query = searchQuery, onQueryChange = { searchQuery = it }) 
            }

            if (searchResults != null) {
                // Search results view
                item {
                    Text(
                        "Hasil Pencarian",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }
                items(searchResults.chunked(2)) { rowItems ->
                    Row(
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top=16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        rowItems.forEach { product ->
                            Box(modifier = Modifier.weight(1f)) {
                                ProductCard(product = product, navController = navController)
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

            } else {
                // Default home view
                item { QuickCategoriesSection(navController) }
                item { SneakLaunchBanner(onClick = { navController.navigate(Screen.SneakLaunch.route) }) }
                item { StorePromoBanner(navController) }
                item { JoinNowBanner(navController) }
            }
        }
    }
}

@Composable
fun SneakLaunchBanner(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(modifier = Modifier.height(180.dp)) {
            Image(
                painter = painterResource(id = R.drawable.speedcat_galaxy), // A cool, futuristic image
                contentDescription = "SneakLaunch Banner", 
                contentScale = ContentScale.Crop, 
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)))
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text("SNEAKLAUNCH", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha=0.8f), letterSpacing = 2.sp)
                Spacer(Modifier.height(4.dp))
                Text("Wujudkan Desain Sepatu Impianmu", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(Modifier.height(8.dp))
                Text("Dukung & miliki produk edisi terbatas →", style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }
}


@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier, navController: NavController) {
    val isWishlisted = wishlistItems.contains(product)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.clickable { navController.navigate(Screen.ProductDetail.createRoute(product.id)) }) {
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                 if (product.originalPrice != null) {
                    Box(
                        modifier = Modifier.align(Alignment.TopStart).padding(8.dp).background(Color.Red, CircleShape).padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        val discountPercent = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                        Text("$discountPercent%", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(
                modifier = Modifier.background(Color(0xFFF5F5F5)).fillMaxWidth().padding(12.dp)
            ) {
                Text(product.name, maxLines = 2, minLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text("Rp${String.format("%,.0f", product.price)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        if (product.originalPrice != null) {
                            Text("Rp${String.format("%,.0f", product.originalPrice)}", style = TextStyle(textDecoration = TextDecoration.LineThrough), fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    IconButton(onClick = { if (isWishlisted) wishlistItems.remove(product) else wishlistItems.add(product) }) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) Color.Red else Color.Gray
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { cartItems.add(product) }, 
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Keranjang")
                }
            }
        }
    }
}

@Composable
fun JoinNowBanner(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable {
                navController.navigate(Screen.Akun.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    restoreState = true
                    launchSingleTop = true
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A5A71))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Join Now SneakDeals", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun StorePromoBanner(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable {
                navController.navigate(Screen.Toko.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    restoreState = true
                    launchSingleTop = true
                }
            },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = "Location Icon", modifier = Modifier.size(32.dp))
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Temukan Promo Toko Terdekat", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Aktifkan lokasimu sekarang!", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun QuickCategoriesSection(navController: NavController) {
    val categories = listOf(
        "Diskon Besar" to "Diskon Besar",
        "Terbaru" to "Terbaru",
        "Lari" to "Running",
        "Lifestyle" to "Lifestyle"
    )

    val categoryIcons = mapOf(
        "Diskon Besar" to Icons.Filled.LocalOffer,
        "Terbaru" to Icons.Filled.NewLabel,
        "Lari" to Icons.Filled.DirectionsRun,
        "Lifestyle" to Icons.Filled.Category
    )

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Kategori Cepat", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.forEach { (displayName, categoryTarget) ->
                CategoryItem(
                    name = displayName,
                    icon = categoryIcons.getValue(displayName),
                    onClick = { navController.navigate(Screen.ProductList.createRoute(categoryTarget)) }
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(name: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(imageVector = icon, contentDescription = name, modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(name, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarSection(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp),
        placeholder = { Text("Cari sepatu Puma, diskon...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BerandaTopAppBar(navController: NavController) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "App Logo", modifier = Modifier.height(50.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Selamat datang di", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Text("SneakDeals", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
                }
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate(Screen.Wishlist.route) }) {
                Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "Wishlist")
            }
            IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "Keranjang")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview() {
    SneakDealsTheme {
        BerandaScreen(rememberNavController())
    }
}
