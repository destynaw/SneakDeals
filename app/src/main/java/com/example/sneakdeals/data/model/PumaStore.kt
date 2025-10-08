package com.example.sneakdeals.data.model

import com.google.firebase.firestore.PropertyName

data class StorePromotion(
    val description: String = "",
    @get:PropertyName("start_date") @set:PropertyName("start_date") var startDate: String = "",
    @get:PropertyName("end_date") @set:PropertyName("end_date") var endDate: String = ""
)

data class PumaStore(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val city: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    @get:PropertyName("opening_hours") @set:PropertyName("opening_hours") var openingHours: String = "",
    @get:PropertyName("phone_number") @set:PropertyName("phone_number") var phoneNumber: String? = null,
    @get:PropertyName("mall_name") @set:PropertyName("mall_name") var mallName: String? = null,
    @get:PropertyName("store_image_url") @set:PropertyName("store_image_url") var storeImageUrl: String? = null,
    val promotions: List<StorePromotion>? = null
)