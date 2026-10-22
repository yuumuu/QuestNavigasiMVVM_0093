package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object QuestList : Screen("quest_list")
    data object QuestDetail : Screen("quest_detail/{questId}") {
        fun createRoute(questId: String): String = "quest_detail/$questId"
    }
    data object AddQuest : Screen("add_quest")
    data object Stats : Screen("stats")
}
