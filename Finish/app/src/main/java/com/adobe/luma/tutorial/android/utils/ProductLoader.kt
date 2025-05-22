/*
  Copyright 2025 Adobe. All rights reserved.
  This file is licensed to you under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License. You may obtain a copy
  of the License at http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software distributed under
  the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR REPRESENTATIONS
  OF ANY KIND, either express or implied. See the License for the specific language
  governing permissions and limitations under the License.
*/

package com.adobe.luma.tutorial.android.utils

import android.content.Context
import android.util.Log
import com.adobe.luma.tutorial.android.models.Product
import com.adobe.luma.tutorial.android.models.Products
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object ProductLoader {
    val loadedProductMap = mutableMapOf<String, Product>()
    suspend fun loadProducts(context: Context, configLocation: String): List<Product> {
        return withContext(Dispatchers.IO) {
            if (configLocation.isEmpty()) {
                val inputStream = context.assets.open("data/products.json")
                val reader = InputStreamReader(inputStream)
                val products: Products = Gson().fromJson(reader, Products::class.java)
                Log.i(
                    "ProductLoader",
                    "ProductLoader (local) - loadProducts loaded ${products.products.size} products..."
                )
                loadedProductMap.putAll(products.products.associateBy { it.sku })
                products.products
            } else {
                val url = URL("$configLocation/products.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = InputStreamReader(connection.inputStream)
                    val products: Products = Gson().fromJson(reader, Products::class.java)
                    Log.i(
                        "ProductLoader",
                        "Network - loadProducts loaded ${products.products.size} products..."
                    )
                    products.products
                } else {
                    Log.e(
                        "ProductLoader",
                        "Network - loadProducts: Something wrong retrieving products, check JSON"
                    )
                    emptyList()
                }
            }
        }
    }
}