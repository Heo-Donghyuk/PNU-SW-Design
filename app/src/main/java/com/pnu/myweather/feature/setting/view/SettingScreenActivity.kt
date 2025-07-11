package com.pnu.myweather.feature.setting.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.pnu.myweather.feature.component.Card
import com.pnu.myweather.feature.component.MyButton
import com.pnu.myweather.ui.theme.MyweatherTheme
import com.pnu.myweather.ui.theme.White


class SettingScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyweatherTheme {
                SettingScreen(onGoBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(onGoBack: () -> Unit) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val sidoList = remember { mutableStateListOf<String>() }
    val selectedSido = remember { mutableStateOf("") }
    val guList = remember { mutableStateListOf<String>() }
    val selectedGu = remember { mutableStateOf("") }
    val dongList = remember { mutableStateListOf<String>() }
    val selectedDong = remember { mutableStateOf("") }

    // 시도 목록 불러오기
    LaunchedEffect(Unit) {
        db.collection("sidos").get()
            .addOnSuccessListener { result ->
                sidoList.clear()
                sidoList.addAll(result.map { it.id })
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "시도 불러오기 실패", e)
            }
    }

    // 시도 선택 시: 구/동 초기화 + 구 목록 로드
    LaunchedEffect(selectedSido.value) {
        selectedGu.value = ""
        selectedDong.value = ""
        guList.clear()
        dongList.clear()

        if (selectedSido.value.isNotEmpty()) {
            db.collection("sidos").document(selectedSido.value).get()
                .addOnSuccessListener { document ->
                    val gu = document["guList"] as? List<*>
                    guList.addAll(gu?.mapNotNull { it.toString() } ?: emptyList())
                }
        }
    }

    // 구 선택 시: 동 초기화 + 동 목록 로드
    LaunchedEffect(selectedGu.value) {
        selectedDong.value = ""
        dongList.clear()

        if (selectedSido.value.isNotEmpty() && selectedGu.value.isNotEmpty()) {
            val docId = "${selectedSido.value}_${selectedGu.value}".replace(" ", "")
            db.collection("sigungus").document(docId).get()
                .addOnSuccessListener { document ->
                    val dong = document["dongList"] as? List<*>
                    dongList.addAll(dong?.mapNotNull { it.toString() } ?: emptyList())
                }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("지역 설정", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onGoBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로 가기")
                }
            }
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Card {
                Column {
                    DropdownMenuBox(label = "시도 선택", items = sidoList, selected = selectedSido)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (guList.isNotEmpty()) {
                        DropdownMenuBox(label = "구 선택", items = guList, selected = selectedGu)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    if (dongList.isNotEmpty()) {
                        DropdownMenuBox(label = "동 선택", items = dongList, selected = selectedDong)

                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            MyButton(onClick = {
                if (selectedSido.value.isNotEmpty() && selectedGu.value.isNotEmpty() && selectedDong.value.isNotEmpty()) {
                    val docId =
                        "${selectedSido.value}_${selectedGu.value}_${selectedDong.value}".replace(
                            " ",
                            ""
                        )
                    db.collection("locations").document(docId).get()
                        .addOnSuccessListener { document ->
                            if (document != null && document.exists()) {
                                val nx = (document["nx"] as? Long)?.toInt() ?: -1
                                val ny = (document["ny"] as? Long)?.toInt() ?: -1
                                val sido = document.getString("sido") ?: selectedSido.value
                                val gu = document.getString("sigungu") ?: selectedGu.value
                                val dong = document.getString("dong") ?: selectedDong.value
                                val station = document.getString("station") ?: ""

                                LocationPreference.save(
                                    context = context,
                                    sido = sido,
                                    gu = gu,
                                    dong = dong,
                                    nx = nx,
                                    ny = ny,
                                    station = station
                                )
                                Toast.makeText(context, "위치가 저장되었습니다", Toast.LENGTH_SHORT).show()
                                Log.d("설정", "저장됨: $sido $gu $dong ($nx, $ny)")
                            } else {
                                Log.w("설정", "해당 지역 정보 없음: $docId")
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("설정", "Firestore 조회 실패", e)
                        }
                }
            }) {
                Text("위치 저장", color = White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuBox(label: String, items: List<String>, selected: MutableState<String>) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selected.value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, fontSize = 16.sp) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)

        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, fontSize = 15.sp) },
                    onClick = {
                        selected.value = item
                        expanded = false
                    }
                )
            }
        }
    }
}
