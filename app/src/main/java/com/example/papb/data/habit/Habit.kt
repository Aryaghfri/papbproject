package com.example.papb.data.habit

data class Habit(
    val id: String = "",
    val yourGoal: String = "",
    val habitName: String = "",
    val period: String = "",
    val habitType: String = "",
    val notified: Boolean = false,
    val startDate: Long = System.currentTimeMillis(),
    val lastCompletedDate: String = "", // Tanggal terakhir selesai
    val completedCount: Int = 0 // Jumlah penyelesaian
)





