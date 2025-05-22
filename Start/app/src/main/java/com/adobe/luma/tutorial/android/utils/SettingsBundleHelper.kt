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
import android.content.SharedPreferences
import android.content.pm.PackageManager

class SettingsBundleHelper(private var context: Context) {

    private val PREFERENCES_NAME = "app_settings"
    private val RESET = "RESET_APP_KEY"
    private val BUILD_VERSION = "build_preference"
    private val APP_VERSION = "version_preference"
    private val DEVELOPMENT_TEAM = "development_preference"
    private val USE_TEST_CONFIG = "testconfig_preference"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    /**
     * Checks and execute settings for information in App Settings
     */
    fun checkAndExecuteSettings() {
        // if (sharedPreferences.getBoolean(SettingsBundleKeys.RESET, false)) {
        with(sharedPreferences.edit()) {
            clear() // This removes all preferences
            putBoolean(RESET, false)
            apply()
        }
    }

    /**
     * Set version and build number
     */
    fun setVersionAndBuildNumber() {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val version = packageInfo.versionName ?: "1.0.0"
            val build = packageInfo.versionCode.toString()
            val developmentTeam = "Ryan Morales"

            with(sharedPreferences.edit()) {
                putString(APP_VERSION, version)
                putString(BUILD_VERSION, build)
                putString(DEVELOPMENT_TEAM, developmentTeam)
                apply()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }
}