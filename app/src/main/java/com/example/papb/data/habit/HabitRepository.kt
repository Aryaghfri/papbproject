package com.example.papb.data.habit

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HabitRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Mendapatkan User ID
    private fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    // Mendapatkan semua habit dari Firebase
    suspend fun getHabits(): List<Habit> {
        val userId = getUserId() ?: return emptyList()
        val snapshot = database.child("users").child(userId).child("habits").get().await()
        return snapshot.children.mapNotNull { it.getValue(Habit::class.java) }
    }


    // Menyimpan habit baru ke Firebase
    suspend fun saveHabit(habit: Habit) {
        val userId = getUserId() ?: return
        val habitId = habit.id.ifEmpty {
            database.child("users").child(userId).child("habits").push().key ?: return
        }
        val habitWithId = habit.copy(id = habitId)
        database.child("users").child(userId).child("habits").child(habitId).setValue(habitWithId).await()
    }

    // Memperbarui habit yang sudah ada di Firebase
    suspend fun updateHabit(habit: Habit) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child("habits").child(habit.id).setValue(habit).await()
    }


    // Menghapus habit dari Firebase
    suspend fun deleteHabit(habitId: String) {
        val userId = getUserId() ?: return
        database.child("users").child(userId).child("habits").child(habitId).removeValue().await()
    }
}
