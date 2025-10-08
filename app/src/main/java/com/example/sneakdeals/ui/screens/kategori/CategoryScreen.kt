package com.example.sneakdeals.ui.screens.kategori

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.R
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.theme.SneakDealsTheme

data class ShoeCategory(
    val name: String,
    @DrawableRes val imageRes: Int
)

// Updated categories to be functional
val shoeCategories = listOf(
    ShoeCategory("Running", R.drawable.speedcat_red),
    ShoeCategory("Training", R.drawable.puma_boys1),
    ShoeCategory("Lifestyle", R.drawable.speedcat_galaxy),
    ShoeCategory("Football", R.drawable.speedcat_footbal1),
    ShoeCategory("Classics", R.drawable.speedcat_grey)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kategori", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White, titleContentColor = Color.Black)
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(shoeCategories) { category ->
                CategoryCard(category = category, onClick = { navController.navigate(Screen.ProductList.createRoute(category.name)) })
            }
        }
    }
}

@Composable
fun CategoryCard(category: ShoeCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Makes the card a square
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 200f
                        )
                    )
            )
            // Category Name
            Text(
                text = category.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryScreenPreview() {
    SneakDealsTheme {
        CategoryScreen(navController = rememberNavController())
    }
}
