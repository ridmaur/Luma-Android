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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adobe.luma.tutorial.android.models.MobileSDK

@Composable
fun TermsOfServiceSheet(onDismiss: () -> Unit) {
    val model: WebViewModel = viewModel()

    LaunchedEffect(Unit) {
        model.loadUrl()
        MobileSDK.shared.sendTrackScreenEvent("luma: content: android: us: en: terms of service")
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row {
            Button(onClick = onDismiss) {
                Text("Close")
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        AndroidView(factory = { model.webView })
    }
}