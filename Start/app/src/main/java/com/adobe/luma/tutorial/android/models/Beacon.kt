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

data class Beacon(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("major") val major: Int,
    @SerializedName("minor") val minor: Int,
    @SerializedName("identifier") val identifier: String,
    @SerializedName("title") val title: String,
    @SerializedName("location") val location: String,
    @SerializedName("category") val category: String,
    @SerializedName("status") val status: String,
    @SerializedName("symbol") val symbol: String
) {
    companion object {
        val example = Beacon(
            uuid = "",
            major = 0,
            minor = 1,
            identifier = "",
            title = "",
            location = "",
            category = "",
            status = "",
            symbol = ""
        )
    }
}