package com.example.papb.data.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HabitViewModel(private val repository: HabitRepository = HabitRepository()) : ViewModel() {
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> get() = _habits.asStateFlow()

    init {
        fetchHabits()
        monitorHabitPeriods()
    }

    private fun fetchHabits() {
        viewModelScope.launch {
            try {
                val habitList = repository.getHabits()
                _habits.value = habitList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addHabit(yourGoal: String, habitName: String, period: String, habitType: String) {
        viewModelScope.launch {
            try {
                val newHabit = Habit(
                    id = "",
                    yourGoal = yourGoal,
                    habitName = habitName,
                    period = period,
                    habitType = habitType,
                    notified = false,
                    startDate = System.currentTimeMillis(),
                    completedCount = 0, // Tambahkan completedCount awal
                    lastCompletedDate = "" // Tanggal terakhir selesai kosong
                )
                repository.saveHabit(newHabit)
                fetchHabits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun completeHabit(habit: Habit) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        if (habit.lastCompletedDate != currentDate) {
            val updatedHabit = habit.copy(
                lastCompletedDate = currentDate,
                completedCount = habit.completedCount + 1,
                notified = true // Tandai habit sebagai selesai
            )
            updateHabitState(updatedHabit)
        }
    }

    fun undoCompleteHabit(habit: Habit) {
        if (habit.lastCompletedDate.isNotEmpty()) {
            val updatedHabit = habit.copy(
                lastCompletedDate = "", // Kosongkan lastCompletedDate
                completedCount = (habit.completedCount - 1).coerceAtLeast(0), // Hindari nilai negatif
                notified = false // Tandai habit belum selesai
            )
            updateHabitState(updatedHabit)
        }
    }

    private fun updateHabitState(updatedHabit: Habit) {
        viewModelScope.launch {
            try {
                repository.updateHabit(updatedHabit)
                // Perbarui state flow secara lokal
                _habits.value = _habits.value.map { habit ->
                    if (habit.id == updatedHabit.id) updatedHabit else habit
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    fun deleteHabit(habitId: String) {
        viewModelScope.launch {
            try {
                repository.deleteHabit(habitId)
                fetchHabits()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun monitorHabitPeriods() {
        viewModelScope.launch {
            while (true) {
                delay(24 * 60 * 60 * 1000)
                val currentTime = System.currentTimeMillis()
                val updatedHabits = _habits.value.filter { habit ->
                    val maxDuration = when (habit.period) {
                        "1 Week (7 Days)" -> 7 * 24 * 60 * 60 * 1000
                        "1 Month (30 Days)" -> 30 * 24 * 60 * 60 * 1000
                        "1 Year (360 Days)" -> 360 * 24 * 60 * 60 * 1000
                        else -> 0
                    }
                    currentTime - habit.startDate <= maxDuration
                }
                _habits.value = updatedHabits
            }
        }
    }
}
