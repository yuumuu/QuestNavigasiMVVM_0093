package com.example

import com.example.data.repository.InMemoryQuestRepository
import com.example.ui.viewmodel.QuestViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuestViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: InMemoryQuestRepository
    private lateinit var viewModel: QuestViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = InMemoryQuestRepository()
        viewModel = QuestViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_hasQuestsLoaded() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(5, state.totalQuests)
    }

    @Test
    fun onSearchQueryChanged_filtersQuests() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSearchQueryChanged("NavHost")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("NavHost", state.searchQuery)
        assertTrue(state.filteredQuests.any { it.title.contains("NavHost") })
    }

    @Test
    fun onFilterDifficultyChanged_filtersCorrectly() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onFilterDifficultyChanged("HARD")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("HARD", state.selectedDifficultyFilter)
        assertTrue(state.filteredQuests.all { it.difficulty.equals("HARD", ignoreCase = true) })
    }

    @Test
    fun toggleQuestCompletion_updatesScore() = runTest {
        testDispatcher.scheduler.advanceUntilIdle()
        val initialScore = viewModel.uiState.value.totalScore

        viewModel.toggleQuestCompletion("Q-02")
        testDispatcher.scheduler.advanceUntilIdle()

        val newScore = viewModel.uiState.value.totalScore
        assertEquals(initialScore + 100, newScore)
    }
}
