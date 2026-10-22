package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestScreen(
    onSaveQuest: (title: String, description: String, difficulty: String, points: Int) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("EASY") }
    var points by remember { mutableIntStateOf(50) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    BackHandler {
        onNavigateBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Tambah Quest Baru") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_back_add")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Formulir Misi Baru",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    errorMessage = null
                },
                label = { Text("Judul Quest") },
                placeholder = { Text("Contoh: Buat Unit Test ViewModel") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_quest_title"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    errorMessage = null
                },
                label = { Text("Deskripsi Misi") },
                placeholder = { Text("Jelaskan rincian dan tujuan quest ini...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_quest_desc"),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp)
            )

            Text(
                text = "Tingkat Kesulitan",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("EASY", "MEDIUM", "HARD").forEach { diff ->
                    FilterChip(
                        selected = difficulty == diff,
                        onClick = {
                            difficulty = diff
                            points = when (diff) {
                                "EASY" -> 50
                                "MEDIUM" -> 100
                                "HARD" -> 150
                                else -> 50
                            }
                        },
                        label = { Text(diff) },
                        modifier = Modifier.testTag("chip_add_$diff")
                    )
                }
            }

            Text(
                text = "Hadiah Poin EXP: $points",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        errorMessage = "Judul quest tidak boleh kosong."
                        return@Button
                    }
                    if (description.isBlank()) {
                        errorMessage = "Deskripsi quest tidak boleh kosong."
                        return@Button
                    }
                    onSaveQuest(title.trim(), description.trim(), difficulty, points)
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_submit_quest")
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text("Simpan Quest")
            }
        }
    }
}
