package com.example.sneakdeals.ui.screens.beranda

import androidx.annotation.DrawableRes
import com.example.sneakdeals.R

// Shared data class for Featured Deals
data class FeaturedDeal(
    val title: String,
    @DrawableRes val imageRes: Int,
    val description: String
)

// Shared list of featured deals, accessible by all screens in this package
val featuredDeals = listOf(
    FeaturedDeal("Mid Season Sale", R.drawable.puma_boys1, "Diskon Gila!"),
    FeaturedDeal("Forever New Deals!", R.drawable.puma_boys10, "Sampai 50% OFF"),
    FeaturedDeal("New Arrival Promo", R.drawable.speedcat_galaxy, "Edisi Terbatas"),
    FeaturedDeal("Football Fever", R.drawable.speedcat_footbal1, "Koleksi Terbaru"),
    FeaturedDeal("Running Essentials", R.drawable.speedcat_red, "Diskon Spesial")
)
