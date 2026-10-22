package com.example.data.model

enum class QuestDifficulty {
    EASY,
    MEDIUM,
    HARD
}

data class QuestCategory(
    val id: String,
    val name: String,
    val iconName: String
)
