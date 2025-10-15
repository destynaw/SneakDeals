package com.example.sneakdeals.ui.screens.kategori

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // ktlint-disable no-wildcard-imports
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.R
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.theme.SneakDealsTheme
import kotlinx.coroutines.delay

// --- DATA MODELS ---
data class Product(
    val id: String,
    val name: String,
    val productCode: String, // Added for detail screen
    val longDescription: String, // Added for detail screen
    val availableSizes: List<String>, // Added for detail screen
    val price: Double,
    val originalPrice: Double? = null,
    @DrawableRes val imageRes: Int,
    val category: String,
    val gender: String, // "Men", "Women", "Unisex"
    val soldCount: Int
)

// --- MASTER PRODUCT LIST WITH FULL DETAILS ---
val allProducts = listOf(
    Product("cl1", "Puma Suede Classic XXI", "PS001", "The Suede hit the scene in 1968 and has been changing the game ever since. It’s been worn by icons of every generation – and it’s stayed classic through it all. This year, we relaunch the Suede with fresh colorways and subtle design updates. Classic as ever, for all-time.", listOf("39", "40", "41", "42", "43"), 999000.0, 1299000.0, R.drawable.speedcat_grey, "Classics", "Men", soldCount = 12500),
    Product("run1", "Speedcat Running Red", "RN001", "A lightweight and responsive running shoe, perfect for your daily miles. Features a breathable mesh upper and ProFoam Lite midsole for ultimate cushioning.", listOf("40", "41", "42", "43", "44"), 850000.0, 1100000.0, R.drawable.speedcat_red, "Running", "Men", soldCount = 5200),
    Product("run2", "Speedcat Running Blue Pink", "RN002", "A stylish running shoe for women, combining performance with a standout look. The SoftFoam+ sockliner provides superior comfort for every step of your day.", listOf("37", "38", "39", "40"), 899000.0, 1150000.0, R.drawable.speedcat_bluepink, "Running", "Women", soldCount = 3800),
    Product("run3", "Speedcat Galaxy", "RN003", "Explore the city in comfort. These running-inspired sneakers feature a sleek design and a cushioned feel that lasts all day.", listOf("39", "40", "41", "42"), 920000.0, 1200000.0, R.drawable.speedcat_galaxy, "Running", "Unisex", soldCount = 4100),
    Product("fb1", "Speedcat Football", "FB001", "Dominate the pitch with these football boots, engineered for speed and precision. The lightweight upper provides a barefoot feel for ultimate ball control.", listOf("41", "42", "43"), 950000.0, 1250000.0, R.drawable.speedcat_football0, "Football", "Men", soldCount = 1800),
    Product("cl2", "Puma ROMA", "CL002", "A classic from the PUMA archives, the Roma is a lightweight training shoe equipped with comfort-enhancing features such as a thick padded tongue and orthopedic arch supports.", listOf("38", "39", "40", "41", "42", "43"), 899000.0, null, R.drawable.puma_boys1, "Classics", "Unisex", soldCount = 8300),
    Product("run4", "Deviate NITRO 2", "RN004", "The Deviate NITRO™ 2 is a max-cushioned, max-performance running shoe that makes going faster even easier. It’s the perfect choice for high-mileage runs.", listOf("40", "41", "42", "43"), 1500000.0, 2100000.0, R.drawable.puma_boys10, "Running", "Men", soldCount = 2600),
    Product("ls1", "Puma CA Pro Classic", "LS001", "The CA Pro Classic features a clean, court-inspired silhouette with a stacked midsole and premium leather upper. A modern classic for any look.", listOf("39", "40", "41", "42", "43", "44"), 1100000.0, null, R.drawable.puma_boys12, "Lifestyle", "Men", soldCount = 7800)
)

private fun formatSoldCount(count: Int): String {
    if (count <= 0) return ""
    return when {
        count >= 1000 -> {
            val formatted = String.format(java.util.Locale.US, "%.1f", count / 1000.0)
            val cleanValue = if (formatted.endsWith(".0")) formatted.substring(0, formatted.length - 2) else formatted
            "${cleanValue}rb terjual"
        }
        else -> "$count terjual"
    }
}

// --- MAIN SCREEN ---
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProductListScreen(navController: NavController, categoryName: String?) {
    val products = when (categoryName) {
        "Diskon Besar" -> allProducts.filter { it.originalPrice != null }
        "Terbaru" -> allProducts.sortedByDescending { it.id } // Assuming higher ID is newer
        else -> allProducts.filter { it.category == categoryName }
    }
    
    var selectedSort by remember { mutableStateOf("Relevansi") }
    var showSortSheet by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }
    val sortSheetState = rememberModalBottomSheetState()
    val filterSheetState = rememberModalBottomSheetState()
    var selectedGender by remember { mutableStateOf<String?>(null) }

    val finalTitle = if (categoryName == "Diskon Besar") "Mega Sale Beli 1 Gratis 1" else categoryName ?: "Produk"

    val filteredAndSortedProducts by remember(products, selectedSort, selectedGender) {
        derivedStateOf {
            val genderFiltered = if (selectedGender != null) {
                products.filter { it.gender == selectedGender || it.gender == "Unisex" }
            } else {
                products
            }
            when (selectedSort) {
                "Termurah" -> genderFiltered.sortedBy { it.price }
                "Termahal" -> genderFiltered.sortedByDescending { it.price }
                "Populer" -> genderFiltered.sortedByDescending { it.soldCount }
                else -> genderFiltered // "Relevansi"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(finalTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
        ) {
            if (categoryName == "Diskon Besar") {
                item {
                    MegaSaleHeader()
                }
            }

            stickyHeader {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                    SortFilterControls(
                        onSortClick = { showSortSheet = true },
                        onFilterClick = { showFilterSheet = true }
                    )
                }
            }
            
            item {
                Text(
                    text = if(categoryName == "Diskon Besar") "Semua Produk Diskon" else "Semua Produk",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
            }

            items(filteredAndSortedProducts) { product ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ProductListItem(
                        product = product,
                        onClick = { navController.navigate(Screen.ProductDetail.createRoute(product.id)) }
                    )
                }
            }
        }

        if (showSortSheet) {
            ModalBottomSheet(onDismissRequest = { showSortSheet = false }, sheetState = sortSheetState) {
                SortOptionsSheet(selectedOption = selectedSort, onOptionSelected = { selectedSort = it; showSortSheet = false })
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(onDismissRequest = { showFilterSheet = false }, sheetState = filterSheetState) {
                FilterOptionsSheet(selectedGender = selectedGender, onGenderSelected = { selectedGender = it; showFilterSheet = false })
            }
        }
    }
}

// --- COMPOSABLES ---
@Composable
fun MegaSaleHeader() {
    var totalSeconds by remember { mutableStateOf(3 * 24 * 3600 + 6 * 3600 + 59 * 60 + 49) } // 3d 6h 59m 49s

    LaunchedEffect(key1 = Unit) {
        while (totalSeconds > 0) {
            delay(1000L)
            totalSeconds--
        }
    }

    val days = totalSeconds / (24 * 3600)
    val hours = (totalSeconds % (24 * 3600)) / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // Countdown Timer
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Berakhir dalam",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TimerBox(value = days, label = "hari", modifier = Modifier.weight(1f))
                TimerBox(value = hours, label = "jam", modifier = Modifier.weight(1f))
                TimerBox(value = minutes, label = "menit", modifier = Modifier.weight(1f))
                TimerBox(value = seconds, label = "detik", modifier = Modifier.weight(1f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Terms and conditions
        Column {
            Text(
                "Syarat & Ketentuan",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("1. Promo ‘Beli 1 Gratis 1’ berlaku untuk semua produk bertanda khusus di halaman ini.", style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                Text("2. Produk gratis adalah produk dengan harga terendah di antara dua produk yang dibeli.", style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                Text("3. Promo tidak dapat digabungkan dengan voucer atau promosi lainnya.", style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                Text("4. Persediaan terbatas dan dapat berubah sewaktu-waktu.", style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun TimerBox(value: Int, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp), RoundedCornerShape(8.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = String.format("%02d", value),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Composable
fun SortFilterControls(onSortClick: () -> Unit, onFilterClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), 
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(onClick = onSortClick, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.SwapVert, contentDescription = "Urutkan", modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Urutkan")
        }
        OutlinedButton(onClick = onFilterClick, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
            Icon(Icons.Default.FilterList, contentDescription = "Filter", modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Filter")
        }
    }
}

@Composable
fun SortOptionsSheet(selectedOption: String, onOptionSelected: (String) -> Unit) {
    val sortOptions = listOf("Relevansi", "Populer", "Termurah", "Termahal")
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Text("Urutkan Berdasarkan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        sortOptions.forEach { option ->
            ListItem(
                headlineContent = { Text(option) },
                modifier = Modifier.clickable { onOptionSelected(option) },
                trailingContent = { if (selectedOption == option) Icon(Icons.Default.Check, contentDescription = "Pilihan") }
            )
        }
    }
}

@Composable
fun FilterOptionsSheet(selectedGender: String?, onGenderSelected: (String?) -> Unit) {
    val genderOptions = listOf("Men", "Women")
    Column(modifier = Modifier.padding(bottom = 32.dp)) {
        Text("Filter Berdasarkan Gender", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        genderOptions.forEach { option ->
            ListItem(
                headlineContent = { Text(option) },
                modifier = Modifier.clickable { onGenderSelected(option) },
                trailingContent = { if (selectedGender == option) Icon(Icons.Default.Check, contentDescription = "Pilihan") }
            )
        }
        // Add a clear filter option
        ListItem(
            headlineContent = { Text("Tampilkan Semua", color = MaterialTheme.colorScheme.primary) },
            modifier = Modifier.clickable { onGenderSelected(null) }
        )
    }
}

@Composable
fun ProductListItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(product.productCode, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                if (product.originalPrice != null) {
                     Row(verticalAlignment = Alignment.CenterVertically) {
                        val discount = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                        Text(text = "Rp${String.format("%,.0f", product.originalPrice)}", textDecoration = TextDecoration.LineThrough, fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("($discount%)", color = Color.Red, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp)) // Placeholder for spacing when there is no discount
                }
                 Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Rp${String.format("%,.0f", product.price)}", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    if (product.soldCount > 0) {
                        Divider(
                            modifier = Modifier
                                .height(12.dp)
                                .width(1.dp),
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatSoldCount(product.soldCount),
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

// --- PREVIEW ---
@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    SneakDealsTheme {
        ProductListScreen(navController = rememberNavController(), categoryName = "Diskon Besar")
    }
}
