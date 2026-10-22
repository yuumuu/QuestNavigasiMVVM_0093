package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.model.QuestItem
import com.example.ui.viewmodel.QuestUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestListScreen(
    uiState: QuestUiState,
    onSearchChanged: (String) -> Unit,
    onFilterDifficulty: (String?) -> Unit,
    onToggleCompletion: (String) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quest Navigasi MVVM",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "0093 • Jetpack Compose",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNavigateToStats,
                        modifier = Modifier.testTag("btn_to_stats")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Statistik & Poin"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                modifier = Modifier.testTag("fab_add_quest")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Quest Baru"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Header ringkasan progress
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("progress_card"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progres Petualangan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.completedQuests}/${uiState.totalQuests} Selesai",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    val progress = if (uiState.totalQuests > 0) {
                        uiState.completedQuests.toFloat() / uiState.totalQuests.toFloat()
                    } else 0f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Total Skor: ${uiState.totalScore} / ${uiState.maxPossibleScore} EXP",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Search bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChanged,
                placeholder = { Text("Cari quest atau topik...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari"
                    )
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Hapus pencarian"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .testTag("search_field"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Filter chips untuk difficulty
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val difficulties = listOf("SEMUA", "EASY", "MEDIUM", "HARD")
                items(difficulties) { diff ->
                    val isSelected = if (diff == "SEMUA") {
                        uiState.selectedDifficultyFilter == null
                    } else {
                        uiState.selectedDifficultyFilter.equals(diff, ignoreCase = true)
                    }
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            if (diff == "SEMUA") {
                                onFilterDifficulty(null)
                            } else {
                                onFilterDifficulty(diff)
                            }
                        },
                        label = { Text(diff) },
                        modifier = Modifier.testTag("filter_$diff")
                    )
                }
            }

            // Daftar Quest
            if (uiState.filteredQuests.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tidak ada quest yang cocok.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.filteredQuests, key = { it.id }) { quest ->
                        QuestCardItem(
                            quest = quest,
                            onToggleCompletion = { onToggleCompletion(quest.id) },
                            onClick = { onNavigateToDetail(quest.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestCardItem(
    quest: QuestItem,
    onToggleCompletion: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("quest_item_${quest.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (quest.isCompleted) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggleCompletion,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("btn_toggle_${quest.id}")
            ) {
                Icon(
                    imageVector = if (quest.isCompleted) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.RadioButtonUnchecked
                    },
                    contentDescription = if (quest.isCompleted) "Tandai belum selesai" else "Tandai selesai",
                    tint = if (quest.isCompleted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = quest.id,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    DifficultyBadge(difficulty = quest.difficulty)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = quest.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (quest.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = quest.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "+${quest.points}p",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: String) {
    val (bgColor, textColor) = when (difficulty.uppercase()) {
        "EASY" -> Pair(Color(0xFFDCFCE7), Color(0xFF166534))
        "MEDIUM" -> Pair(Color(0xFFFEF9C3), Color(0xFF854D0E))
        "HARD" -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B))
        else -> Pair(Color(0xFFE2E8F0), Color(0xFF334155))
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = difficulty.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}
