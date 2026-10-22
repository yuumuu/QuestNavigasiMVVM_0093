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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.QuestUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestStatsScreen(
    uiState: QuestUiState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val rank = when {
        uiState.totalScore >= 350 -> "Master of Compose"
        uiState.totalScore >= 200 -> "Senior Adventurer"
        uiState.totalScore >= 100 -> "Apprentice Navigator"
        else -> "Novice Explorer"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Statistik Petualang") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_back_stats")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali ke Daftar Quest"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
            // Rank Badge Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rank_badge_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Trofi Prestasi",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.height(56.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = rank,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "Total EXP: ${uiState.totalScore} Poin",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Rincian Statistik
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ringkasan Misi",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    StatRow(label = "Total Quest Tersedia", value = "${uiState.totalQuests}")
                    StatRow(label = "Quest Selesai", value = "${uiState.completedQuests}")
                    StatRow(
                        label = "Persentase Selesai",
                        value = if (uiState.totalQuests > 0) "${(uiState.completedQuests * 100) / uiState.totalQuests}%" else "0%"
                    )
                    StatRow(label = "EXP Tercapai", value = "${uiState.totalScore} / ${uiState.maxPossibleScore}")
                }
            }

            // Card Pola MVVM & Navigasi
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(
                            text = "Arsitektur MVVM 0093",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Model: QuestItem & QuestCategory\n" +
                                "Repository: InMemoryQuestRepository (Flow)\n" +
                                "ViewModel: QuestViewModel (StateFlow & UiState)\n" +
                                "View: Jetpack Compose + NavHost",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_stats_back_home")
            ) {
                Text("Kembali ke Beranda Quest")
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
