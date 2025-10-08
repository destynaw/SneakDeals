package com.example.sneakdeals.ui.screens.productdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.data.model.cartItems
import com.example.sneakdeals.data.model.wishlistItems
import com.example.sneakdeals.ui.screens.kategori.Product
import com.example.sneakdeals.ui.screens.kategori.allProducts
import com.example.sneakdeals.ui.theme.SneakDealsTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProductDetailScreen(product: Product, navController: NavController) {
    var selectedSize by remember { mutableStateOf<String?>(null) }
    val isWishlisted by remember { derivedStateOf { wishlistItems.contains(product) } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        if (isWishlisted) {
                            wishlistItems.remove(product)
                        } else {
                            wishlistItems.add(product)
                        }
                    }) {
                        Icon(
                            imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (isWishlisted) Color.Red else Color.Gray
                        )
                    }
                }
            )
        },
        bottomBar = {
            ProductDetailBottomBar(product = product)
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            item {
                ProductImage(product = product)
                Spacer(Modifier.height(16.dp))
                ProductInfo(product = product)
                Spacer(Modifier.height(24.dp))
                SizeSelector(product = product, selectedSize = selectedSize, onSizeSelected = { selectedSize = it })
                Spacer(Modifier.height(24.dp))
                ProductDescription(product = product)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProductImage(product: Product) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = product.name,
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ProductInfo(product: Product) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("PUMA", style = MaterialTheme.typography.labelLarge, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Kode Produk: ${product.productCode}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Rp${String.format("%,.0f", product.price)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            if (product.originalPrice != null) {
                val discountPercent = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                Badge(containerColor = Color.Red.copy(alpha = 0.1f)) {
                    Text("$discountPercent%", color = Color.Red, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        if (product.originalPrice != null) {
            Text("Rp${String.format("%,.0f", product.originalPrice)}", style = TextStyle(textDecoration = TextDecoration.LineThrough), color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SizeSelector(product: Product, selectedSize: String?, onSizeSelected: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Ukuran", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Panduan Ukuran", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { /*TODO*/ })
        }
        Spacer(Modifier.height(12.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (size in product.availableSizes) {
                SizeChip(size = size, isSelected = size == selectedSize, onSelected = { onSizeSelected(size) })
            }
        }
    }
}

@Composable
fun SizeChip(size: String, isSelected: Boolean, onSelected: () -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val border = if (isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.primary) else BorderStroke(1.dp, Color.LightGray)

    Box(
        modifier = Modifier
            .size(width = 60.dp, height = 40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .border(border, RoundedCornerShape(8.dp))
            .clickable(onClick = onSelected),
        contentAlignment = Alignment.Center
    ) {
        Text(text = size, color = contentColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ProductDescription(product: Product) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Deskripsi Produk", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(product.longDescription, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, lineHeight = 22.sp)
    }
}

@Composable
fun ProductDetailBottomBar(product: Product) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { cartItems.add(product) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Ke Keranjang")
            }
            Button(
                onClick = { /* TODO: Buy Now logic */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Beli Sekarang")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    SneakDealsTheme {
        ProductDetailScreen(product = allProducts.first(), navController = rememberNavController())
    }
}
