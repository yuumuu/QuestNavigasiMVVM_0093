package com.example.data.repository

import com.example.data.model.QuestDifficulty
import com.example.data.model.QuestItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

interface QuestRepository {
    fun getQuests(): Flow<List<QuestItem>>
    fun getQuestById(id: String): Flow<QuestItem?>
    fun toggleQuestCompletion(id: String)
    fun addQuest(quest: QuestItem)
    fun deleteQuest(id: String)
}

class InMemoryQuestRepository : QuestRepository {
    private val _quests = MutableStateFlow(
        listOf(
            QuestItem(
                id = "Q-01",
                title = "Inisialisasi NavHost Compose",
                description = "Pelajari cara kerja NavController dan NavHost untuk menghubungkan antar halaman.",
                difficulty = QuestDifficulty.EASY.name,
                points = 50,
                isCompleted = true
            ),
            QuestItem(
                id = "Q-02",
                title = "Integrasi Pola MVVM",
                description = "Pisahkan UI State, ViewModel, dan Repository untuk arsitektur kode yang modular.",
                difficulty = QuestDifficulty.MEDIUM.name,
                points = 100,
                isCompleted = false
            ),
            QuestItem(
                id = "Q-03",
                title = "Kirim Parameter Antar Layar",
                description = "Teruskan argumen ID dinamis melalui rute composable dengan NavType.",
                difficulty = QuestDifficulty.MEDIUM.name,
                points = 100,
                isCompleted = false
            ),
            QuestItem(
                id = "Q-04",
                title = "Handling Tombol Kembali (BackHandler)",
                description = "Tangani sistem gesture back dan navigasi kembali secara deklaratif.",
                difficulty = QuestDifficulty.EASY.name,
                points = 50,
                isCompleted = false
            ),
            QuestItem(
                id = "Q-05",
                title = "Implementasi StateFlow & State Recomposition",
                description = "Kelola state reaktif dengan StateFlow dan collectAsStateWithLifecycle.",
                difficulty = QuestDifficulty.HARD.name,
                points = 150,
                isCompleted = false
            )
        )
    )

    override fun getQuests(): Flow<List<QuestItem>> = _quests.asStateFlow()

    override fun getQuestById(id: String): Flow<QuestItem?> =
        _quests.map { list -> list.find { it.id == id } }

    override fun toggleQuestCompletion(id: String) {
        _quests.value = _quests.value.map { quest ->
            if (quest.id == id) {
                quest.copy(isCompleted = !quest.isCompleted)
            } else {
                quest
            }
        }
    }

    override fun addQuest(quest: QuestItem) {
        _quests.value = _quests.value + quest
    }

    override fun deleteQuest(id: String) {
        _quests.value = _quests.value.filter { it.id != id }
    }
}
