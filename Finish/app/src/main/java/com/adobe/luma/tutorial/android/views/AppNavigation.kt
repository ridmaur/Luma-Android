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

package com.adobe.luma.tutorial.android.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.adobe.luma.tutorial.android.models.MobileSDK

/**
 * AppNavigation composable is the navigation component of the application.
 * It defines the navigation routes and the corresponding composables.
 *
 * @param navController NavController to handle navigation
 */
@Composable
fun AppNavigation(navController: NavHostController) {
    val context = navController.context
    val startDestination =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            Routes.DisclaimerView.route
        } else {
            MobileSDK.shared.updateTrackingStatus(TrackingStatus.AUTHORIZED)
            Routes.ContentView.route
        }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.DisclaimerView.route) {
            DisclaimerView(navController = navController)
        }
        composable(Routes.ContentView.route) {
            ContentView(navController = navController)
        }
    }
}

sealed class Routes(val route: String) {
    data object ContentView : Routes("ContentView")
    data object DisclaimerView : Routes("DisclaimerView")
}