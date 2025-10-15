package com.example.sneakdeals.ui.navigation

sealed class Screen(val route: String) {
    object Beranda : Screen("beranda")
    object Kategori : Screen("kategori")
    object Toko : Screen("toko")
    object Akun : Screen("akun")
    object MainApp : Screen("main_app")
    object Login : Screen("login")
    object Wishlist : Screen("wishlist")
    object Cart : Screen("cart")
    object SneakLaunch : Screen("sneak_launch")

    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }

    object StoreDetail : Screen("store_detail/{storeId}") {
        fun createRoute(storeId: String) = "store_detail/$storeId"
    }

    object Chat : Screen("chat/{storeId}") {
        fun createRoute(storeId: String) = "chat/$storeId"
    }

    object GlobalChat : Screen("global_chat")

    object AllDeals : Screen("all_deals")

    object ProductList : Screen("product_list/{categoryName}") {
        fun createRoute(categoryName: String) = "product_list/$categoryName"
    }
}
