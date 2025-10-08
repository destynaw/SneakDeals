package com.example.sneakdeals.data.repository

import android.util.Log
import com.example.sneakdeals.data.model.PumaShoe
import com.example.sneakdeals.data.model.PumaStore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

object ProductRepository {

    private const val TAG = "ProductRepository"
    private val db = FirebaseFirestore.getInstance()

    private const val PRODUCTS_COLLECTION = "products"
    private const val STORES_COLLECTION = "stores"

    suspend fun getAllProducts(): List<PumaShoe> {
        return try {
            val result = db.collection(PRODUCTS_COLLECTION).get().await()
            val products = result.toObjects<PumaShoe>()
            Log.d(TAG, "Successfully fetched ${products.size} products.")
            products
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching products.", e)
            emptyList()
        }
    }

    suspend fun getAllStores(): List<PumaStore> {
        return try {
            val result = db.collection(STORES_COLLECTION).get().await()
            val stores = result.toObjects<PumaStore>()
            Log.d(TAG, "Successfully fetched ${stores.size} stores.")
            stores
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching stores.", e)
            emptyList()
        }
    }

    suspend fun getProductById(productId: String): PumaShoe? {
        if (productId.isBlank()) return null
        return try {
            val document = db.collection(PRODUCTS_COLLECTION).document(productId).get().await()
            val shoe = document.toObject<PumaShoe>()
            if (shoe != null) {
                Log.d(TAG, "Successfully fetched product with ID: $productId")
            } else {
                Log.w(TAG, "Product with ID: $productId not found.")
            }
            shoe
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching product with ID: $productId", e)
            null
        }
    }
}
