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

data class Decisions(
    @SerializedName("decisionScopes") val decisionScopes: List<Decision>
) {
    companion object {
        val example = Decisions(
            decisionScopes = listOf(
                Decision(
                    name = "",
                    activityId = "",
                    placementId = "",
                    itemCount = 0
                )
            )
        )
    }
}

data class Decision(
    @SerializedName("name") val name: String?,
    @SerializedName("activityId") val activityId: String,
    @SerializedName("placementId") val placementId: String,
    @SerializedName("itemCount") val itemCount: Int
)