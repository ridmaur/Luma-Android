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
import com.adobe.luma.tutorial.android.models.Decisions
import com.adobe.luma.tutorial.android.models.General
import com.adobe.luma.tutorial.android.models.Beacon
import com.adobe.luma.tutorial.android.models.Beacons
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object Network {

    suspend fun loadDecisions(context: Context, configLocation: String): Decisions {
        return withContext(Dispatchers.IO) {
            if (configLocation.isEmpty()) {
                val inputStream = context.assets.open("data/decisions.json")
                val reader = InputStreamReader(inputStream)
                val decisions: Decisions = Gson().fromJson(reader, Decisions::class.java)
                Log.i("Network", "Network (local) - loadDecisions loaded...")
                decisions
            } else {
                val url = URL("$configLocation/decisions.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = InputStreamReader(connection.inputStream)
                    val decisions: Decisions = Gson().fromJson(reader, Decisions::class.java)
                    Log.i("Network", "Network - loadDecisions loaded...")
                    decisions
                } else {
                    Log.e(
                        "Network",
                        "Network - loadDecisions: Something wrong retrieving decisions, check JSON"
                    )
                    Decisions.example
                }
            }
        }
    }

    suspend fun loadGeneral(context: Context, configLocation: String): General {
        return withContext(Dispatchers.IO) {
            if (configLocation.isEmpty()) {
                val inputStream = context.assets.open("data/general.json")
                val reader = InputStreamReader(inputStream)
                val general: General = Gson().fromJson(reader, General::class.java)
                Log.i("Network", "Network (local) - loadGeneral loaded...")
                general
            } else {
                val url = URL("$configLocation/general.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = InputStreamReader(connection.inputStream)
                    val general: General = Gson().fromJson(reader, General::class.java)
                    Log.i("Network", "Network - loadGeneral loaded...")
                    general
                } else {
                    Log.e(
                        "Network",
                        "Network - loadGeneral: Something wrong retrieving general, check JSON"
                    )
                    General.example
                }
            }
        }
    }

    suspend fun loadBeacons(context: Context, configLocation: String): List<Beacon> {
        return withContext(Dispatchers.IO) {
            if (configLocation.isEmpty()) {
                val inputStream = context.assets.open("data/ibeacons.json")
                val reader = InputStreamReader(inputStream)
                val beacons: Beacons = Gson().fromJson(reader, Beacons::class.java)
                Log.i(
                    "Network",
                    "Network (local) - loadBeacons - Found ${beacons.beacons.size} beacons…"
                )
                beacons.beacons
            } else {
                val url = URL("$configLocation/ibeacons.json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = InputStreamReader(connection.inputStream)
                    val beacons: Beacons = Gson().fromJson(reader, Beacons::class.java)
                    Log.i(
                        "Network",
                        "Network - loadBeacons - Found ${beacons.beacons.size} beacons…"
                    )
                    beacons.beacons
                } else {
                    Log.e(
                        "Network",
                        "Network - loadBeacons: Something wrong retrieving beacons, check JSON"
                    )
                    emptyList()
                }
            }
        }
    }
}