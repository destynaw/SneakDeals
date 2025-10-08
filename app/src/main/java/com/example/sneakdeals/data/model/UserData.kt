package com.example.sneakdeals.data.model

import androidx.compose.runtime.mutableStateListOf
import com.example.sneakdeals.ui.screens.kategori.Product

// Global state for user's wishlist and cart
// This acts as a simple, in-memory database for our app's state.

val wishlistItems = mutableStateListOf<Product>()
val cartItems = mutableStateListOf<Product>()
