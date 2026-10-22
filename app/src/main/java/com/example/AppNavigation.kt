package com.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.navigation.Screen
import com.example.ui.screens.AddQuestScreen
import com.example.ui.screens.QuestDetailScreen
import com.example.ui.screens.QuestListScreen
import com.example.ui.screens.QuestStatsScreen
import com.example.ui.viewmodel.QuestViewModel

/**
 * Composable utama untuk mengelola perpindahan antar layar menggunakan NavHost
 * dan pola arsitektur MVVM dengan StateFlow.
 */
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: QuestViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Screen.QuestList.route,
        modifier = modifier
    ) {
        // Layar 1: Daftar Quest
        composable(route = Screen.QuestList.route) {
            QuestListScreen(
                uiState = uiState,
                onSearchChanged = viewModel::onSearchQueryChanged,
                onFilterDifficulty = viewModel::onFilterDifficultyChanged,
                onToggleCompletion = viewModel::toggleQuestCompletion,
                onNavigateToDetail = { questId ->
                    viewModel.selectQuest(questId)
                    navController.navigate(Screen.QuestDetail.createRoute(questId))
                },
                onNavigateToAdd = {
                    navController.navigate(Screen.AddQuest.route)
                },
                onNavigateToStats = {
                    navController.navigate(Screen.Stats.route)
                }
            )
        }

        // Layar 2: Detail Quest dengan parameter dinamis {questId}
        composable(
            route = Screen.QuestDetail.route,
            arguments = listOf(
                navArgument("questId") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val questId = backStackEntry.arguments?.getString("questId") ?: ""
            val quest = uiState.quests.find { it.id == questId }

            QuestDetailScreen(
                quest = quest,
                questId = questId,
                onToggleCompletion = {
                    viewModel.toggleQuestCompletion(questId)
                },
                onDeleteQuest = {
                    viewModel.deleteQuest(questId)
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Layar 3: Tambah Quest Baru
        composable(route = Screen.AddQuest.route) {
            AddQuestScreen(
                onSaveQuest = { title, desc, diff, pts ->
                    viewModel.addQuest(title, desc, diff, pts)
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Layar 4: Statistik & EXP Quest
        composable(route = Screen.Stats.route) {
            QuestStatsScreen(
                uiState = uiState,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
