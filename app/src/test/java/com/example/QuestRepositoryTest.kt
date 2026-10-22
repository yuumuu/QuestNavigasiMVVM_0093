package com.example

import com.example.data.model.QuestItem
import com.example.data.repository.InMemoryQuestRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuestRepositoryTest {

    private lateinit var repository: InMemoryQuestRepository

    @Before
    fun setUp() {
        repository = InMemoryQuestRepository()
    }

    @Test
    fun getQuests_returnsInitialQuests() = runTest {
        val quests = repository.getQuests().first()
        assertTrue(quests.isNotEmpty())
        assertEquals(5, quests.size)
    }

    @Test
    fun toggleQuestCompletion_updatesStatus() = runTest {
        val initial = repository.getQuests().first().first { it.id == "Q-02" }
        assertEquals(false, initial.isCompleted)

        repository.toggleQuestCompletion("Q-02")
        val updated = repository.getQuests().first().first { it.id == "Q-02" }
        assertEquals(true, updated.isCompleted)
    }

    @Test
    fun addQuest_increasesQuestList() = runTest {
        val newQuest = QuestItem(
            id = "Q-TEST",
            title = "Test Quest",
            description = "Test Desc",
            difficulty = "EASY",
            points = 25
        )
        repository.addQuest(newQuest)

        val quests = repository.getQuests().first()
        assertEquals(6, quests.size)
        assertNotNull(quests.find { it.id == "Q-TEST" })
    }

    @Test
    fun deleteQuest_removesItem() = runTest {
        repository.deleteQuest("Q-01")
        val quests = repository.getQuests().first()
        assertEquals(4, quests.size)
    }
}
