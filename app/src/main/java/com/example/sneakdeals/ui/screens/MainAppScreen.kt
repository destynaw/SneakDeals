package com.example.sneakdeals.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sneakdeals.data.model.allStores
import com.example.sneakdeals.ui.navigation.Screen
import com.example.sneakdeals.ui.screens.akun.AccountScreen
import com.example.sneakdeals.ui.screens.beranda.AllDealsScreen
import com.example.sneakdeals.ui.screens.beranda.BerandaScreen
import com.example.sneakdeals.ui.screens.beranda.MegaSaleScreen
import com.example.sneakdeals.ui.screens.cart.CartScreen
import com.example.sneakdeals.ui.screens.chat.ChatScreen
import com.example.sneakdeals.ui.screens.globalchat.GlobalChatScreen
import com.example.sneakdeals.ui.screens.kategori.CategoryScreen
import com.example.sneakdeals.ui.screens.kategori.ProductListScreen
import com.example.sneakdeals.ui.screens.kategori.allProducts
import com.example.sneakdeals.ui.screens.productdetail.ProductDetailScreen
import com.example.sneakdeals.ui.screens.store.StoreDetailScreen
import com.example.sneakdeals.ui.screens.store.StoreScreen
import com.example.sneakdeals.ui.screens.wishlist.WishlistScreen

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
fun MainAppScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val screensWithBottomBar = listOf(Screen.Beranda.route, Screen.Kategori.route, Screen.Toko.route, Screen.Akun.route)
            if (currentRoute in screensWithBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Beranda.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Beranda.route) {
                BerandaScreen(navController = navController)
            }
            composable(Screen.Kategori.route) {
                CategoryScreen(navController = navController)
            }
            composable(Screen.Toko.route) {
                StoreScreen(navController = navController)
            }
            composable(Screen.Akun.route) {
                AccountScreen(navController = navController)
            }
            composable(Screen.Wishlist.route) {
                WishlistScreen(navController = navController)
            }
            composable(Screen.Cart.route) {
                CartScreen(navController = navController)
            }
            composable(Screen.ProductDetail.route) {
                val productId = it.arguments?.getString("productId")
                val product = allProducts.find { p -> p.id == productId }
                if (product != null) {
                    ProductDetailScreen(product = product, navController = navController)
                }
            }
            composable(Screen.StoreDetail.route) {
                val storeId = it.arguments?.getString("storeId")
                val store = allStores.find { s -> s.id == storeId }
                if (store != null) {
                    StoreDetailScreen(store = store, navController = navController)
                }
            }
            composable(Screen.Chat.route) {
                val storeId = it.arguments?.getString("storeId")
                val store = allStores.find { s -> s.id == storeId }
                if (store != null) {
                    ChatScreen(store = store, navController = navController)
                }
            }
            composable(Screen.GlobalChat.route) {
                GlobalChatScreen(navController = navController)
            }
            composable(Screen.AllDeals.route) {
                AllDealsScreen(navController = navController)
            }
            composable(Screen.ProductList.route) {
                val categoryName = it.arguments?.getString("categoryName")
                ProductListScreen(navController = navController, categoryName = categoryName)
            }
            composable(Screen.MegaSale.route) {
                MegaSaleScreen(navController = navController)
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    navController: NavHostController
) {
    val navItems = listOf(
        BottomNavItem(label = "Beranda", icon = Icons.Filled.Home, screen = Screen.Beranda),
        BottomNavItem(label = "Kategori", icon = Icons.Filled.GridView, screen = Screen.Kategori),
        BottomNavItem(label = "Toko", icon = Icons.Filled.Store, screen = Screen.Toko),
        BottomNavItem(label = "Akun", icon = Icons.Filled.AccountCircle, screen = Screen.Akun)
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
