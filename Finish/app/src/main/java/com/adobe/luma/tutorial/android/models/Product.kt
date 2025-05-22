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

package com.adobe.luma.tutorial.android.models

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Product(
    @SerializedName("sku") val sku: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("color") val color: String,
    @SerializedName("size") val size: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("imageUrl") val imageURL: String,
    @SerializedName("url") val url: String,
    @SerializedName("stockQuantity") val stockQuantity: Int?,
    @SerializedName("featured") val featured: Boolean?
) {
    val id: UUID = UUID.randomUUID()

    companion object {
        val example = Product(
            sku = "",
            name = "",
            category = "",
            color = "",
            size = "",
            price = 0.0,
            description = "",
            imageURL = "",
            url = "",
            stockQuantity = 0,
            featured = false
        )
    }
}