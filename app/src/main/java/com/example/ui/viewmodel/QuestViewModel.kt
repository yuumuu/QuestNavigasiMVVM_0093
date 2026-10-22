package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.QuestItem
import com.example.data.repository.InMemoryQuestRepository
import com.example.data.repository.QuestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class QuestUiState(
    val quests: List<QuestItem> = emptyList(),
    val searchQuery: String = "",
    val selectedDifficultyFilter: String? = null,
    val selectedQuest: QuestItem? = null,
    val isLoading: Boolean = false
) {
    val totalQuests: Int get() = quests.size
    val completedQuests: Int get() = quests.count { it.isCompleted }
    val totalScore: Int get() = quests.filter { it.isCompleted }.sumOf { it.points }
    val maxPossibleScore: Int get() = quests.sumOf { it.points }

    val filteredQuests: List<QuestItem>
        get() = quests.filter { quest ->
            val matchesQuery = quest.title.contains(searchQuery, ignoreCase = true) ||
                    quest.description.contains(searchQuery, ignoreCase = true)
            val matchesDifficulty = selectedDifficultyFilter == null ||
                    quest.difficulty.equals(selectedDifficultyFilter, ignoreCase = true)
            matchesQuery && matchesDifficulty
        }
}

class QuestViewModel(
    private val repository: QuestRepository = InMemoryQuestRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedDifficulty = MutableStateFlow<String?>(null)
    private val _selectedQuestId = MutableStateFlow<String?>(null)

    val uiState: StateFlow<QuestUiState> = combine(
        repository.getQuests(),
        _searchQuery,
        _selectedDifficulty,
        _selectedQuestId
    ) { quests, query, diff, selectedId ->
        QuestUiState(
            quests = quests,
            searchQuery = query,
            selectedDifficultyFilter = diff,
            selectedQuest = quests.find { it.id == selectedId },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuestUiState(isLoading = true)
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterDifficultyChanged(difficulty: String?) {
        _selectedDifficulty.value = if (_selectedDifficulty.value == difficulty) null else difficulty
    }

    fun selectQuest(id: String) {
        _selectedQuestId.value = id
    }

    fun toggleQuestCompletion(id: String) {
        viewModelScope.launch {
            repository.toggleQuestCompletion(id)
        }
    }

    fun addQuest(title: String, description: String, difficulty: String, points: Int) {
        viewModelScope.launch {
            val newId = "Q-0${(10..99).random()}"
            val quest = QuestItem(
                id = newId,
                title = title,
                description = description,
                difficulty = difficulty,
                points = points,
                isCompleted = false
            )
            repository.addQuest(quest)
        }
    }

    fun deleteQuest(id: String) {
        viewModelScope.launch {
            repository.deleteQuest(id)
        }
    }
}
