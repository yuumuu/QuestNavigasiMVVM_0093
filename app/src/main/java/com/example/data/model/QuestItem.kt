package com.example.data.model

/**
 * Model data untuk item misi / quest dalam aplikasi.
 */
data class QuestItem(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val points: Int,
    val isCompleted: Boolean = false
)
