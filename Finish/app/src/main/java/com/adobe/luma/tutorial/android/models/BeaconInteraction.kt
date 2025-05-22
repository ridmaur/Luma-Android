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

data class BeaconInteractionPayload(
    @SerializedName("placeContext") val placeContext: PlaceContext,
    @SerializedName("eventType") val eventType: String
) {
    fun toMap(): Map<String, Any> {
        return mapOf(
            "placeContext" to placeContext,
            "eventType" to eventType
        )
    }
}

data class PlaceContext(
    @SerializedName("POIinteraction") val poIinteraction: POIinteraction?
)

data class POIinteraction(
    @SerializedName("poiDetail") val poiDetail: PoiDetail,
    @SerializedName("poiEntries") val poiEntries: PoiEntries,
    @SerializedName("poiExits") val poiExits: PoiExits
)

data class PoiDetail(
    @SerializedName("name") val name: String,
    @SerializedName("poiID") val poiID: String,
    @SerializedName("locatingType") val locatingType: String,
    @SerializedName("category") val category: String,
    @SerializedName("beaconInteractionDetails") val beaconInteractionDetails: BeaconInteractionDetails
)

data class PoiEntries(
    @SerializedName("value") val value: Double
)

data class PoiExits(
    @SerializedName("value") val value: Double
)

data class BeaconInteractionDetails(
    @SerializedName("beaconMajor") val beaconMajor: Double,
    @SerializedName("beaconMinor") val beaconMinor: Double
)