package com.example.papb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.papb.auth.LoginScreen
import com.example.papb.auth.RegisterScreen
import com.example.papb.data.habit.HabitViewModel
import com.example.papb.data.user.UserViewModel
import com.example.papb.fragment.CreateHabitScreenDialog
import com.example.papb.fragment.HomeFragment
import com.example.papb.fragment.ProfileScreen
import com.example.papb.fragment.ProgressScreen
import com.example.papb.ui.BottomNavigationBar
import com.example.papb.ui.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val habitViewModel = remember { HabitViewModel() }
    val userViewModel = remember { UserViewModel() }

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Ambil data pengguna
    LaunchedEffect(userId) {
        userId?.let { userViewModel.fetchUser(it) }
    }

    // Pantau perubahan data pengguna
    val user by userViewModel.currentUser.collectAsState(initial = null)

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(onSplashComplete = { navController.navigate("login") })
        }
        composable("login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("main") },
                onRegisterClick = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = userViewModel,
                onRegisterSuccess = { navController.popBackStack("login", false) },
            )
        }
        composable("main") {
            user?.let { userData ->
                MainScreen(
                    viewModel = habitViewModel,
                    user = userData, // Kirim data pengguna ke MainScreen
                    onNavigateToCreateHabit = { navController.navigate("createHabit") }
                )
            }
        }
        composable("profile") {
            ProfileScreen(viewModel = userViewModel)
        }
        composable("Progress") {
            ProgressScreen(viewModel = HabitViewModel())
        }
    }
}
