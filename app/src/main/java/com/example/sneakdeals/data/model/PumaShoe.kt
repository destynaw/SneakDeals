package com.example.sneakdeals.data.model

import com.google.firebase.firestore.PropertyName

data class PumaShoe(
    val id: String = "",
    val name: String = "",
    @get:PropertyName("model_type") @set:PropertyName("model_type") var modelType: String = "",
    val colorway: String = "",
    val price: Double = 0.0,
    @get:PropertyName("original_price") @set:PropertyName("original_price") var originalPrice: Double? = null,
    val description: String = "",
    @get:PropertyName("image_urls") @set:PropertyName("image_urls") var imageUrls: List<String> = emptyList(),
    val gender: String = "Unisex",
    val features: List<String>? = null,
    @get:PropertyName("release_date") @set:PropertyName("release_date") var releaseDate: String? = null,
    val sku: String? = null,
    @get:PropertyName("available_sizes") @set:PropertyName("available_sizes") var availableSizes: List<String> = emptyList(),
    @get:PropertyName("store_id_list") @set:PropertyName("store_id_list") var storeIdList: List<String> = emptyList(),
    @get:PropertyName("discount_start_date") @set:PropertyName("discount_start_date") var discountStartDate: String? = null,
    @get:PropertyName("discount_end_date") @set:PropertyName("discount_end_date") var discountEndDate: String? = null
)