package com.pnu.myweather.feature.developer.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.pnu.myweather.core.gemma.PromptProvider


class PromptEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PromptEditScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptEditScreen() {
    val activity = LocalActivity.current
    val context = LocalContext.current
    var promptText by remember { mutableStateOf(PromptProvider.loadPrompt(context)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("프롬프트 수정") },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "🧾 프롬프트 수정",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = promptText,
                onValueChange = { promptText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                singleLine = false,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    PromptProvider.savePrompt(context, promptText)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("프롬프트 저장")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    PromptProvider.resetPrompt(context)
                    promptText = PromptProvider.loadPrompt(context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("기본값으로 초기화")
            }
        }
    }
}