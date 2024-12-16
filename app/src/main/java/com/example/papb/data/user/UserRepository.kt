package com.example.papb.data.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun registerUser(user: User, password: String): String? {
        return try {
            // Buat akun pengguna dengan email dan password
            val result = auth.createUserWithEmailAndPassword(user.email, password).await()
            val userId = result.user?.uid ?: return null

            // Simpan data pengguna ke Realtime Database
            database.child("users").child(userId).setValue(user).await()

            userId // Kembalikan UID pengguna jika berhasil
        } catch (e: Exception) {
            // Tangkap dan log error jika terjadi masalah
            e.printStackTrace()
            null
        }
    }

    /**
     * Fungsi untuk mendapatkan data pengguna dari Realtime Database
     * @param userId UID pengguna yang akan diambil datanya
     * @return Objek User jika ditemukan, null jika tidak ditemukan
     */
    suspend fun getUser(userId: String): User? {
        return try {
            val snapshot = database.child("users").child(userId).get().await()
            snapshot.getValue(User::class.java) // Konversi snapshot ke User
        } catch (e: Exception) {
            // Tangkap dan log error jika terjadi masalah
            e.printStackTrace()
            null
        }
    }
}
