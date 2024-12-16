package com.example.papb

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.papb.data.habit.HabitViewModel
import com.example.papb.data.user.User
import com.example.papb.data.user.UserViewModel
import com.example.papb.fragment.ConfirmationScreen
import com.example.papb.fragment.CreateHabitScreenDialog
import com.example.papb.fragment.HomeFragment
import com.example.papb.fragment.ProfileScreen
import com.example.papb.fragment.ProgressScreen
import com.example.papb.ui.BottomNavigationBar

@Composable
fun MainScreen(
    viewModel: HabitViewModel,
    user: User,
    onNavigateToCreateHabit: () -> Unit
) {
    val navController = rememberNavController()
    val bottomNavItems = listOf("list", "progress", "profile")

    var selectedItem by remember { mutableStateOf(0) }
    var showCreateHabitDialog by remember { mutableStateOf(false) }
    var showConfirmationScreen by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = selectedItem,
                onItemSelected = { index ->
                    selectedItem = index
                    navController.navigate(bottomNavItems[index]) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = bottomNavItems[0],
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable("list") {
                HomeFragment(
                    viewModel = viewModel,
                    user = user,
                    onNavigateToCreateHabit = { showCreateHabitDialog = true }
                )
            }
            composable("progress") {
                ProgressScreen(viewModel = viewModel) // Progress Screen Added
            }
            composable("profile") {
                ProfileScreen(viewModel = UserViewModel())
            }
        }

        if (showCreateHabitDialog) {
            CreateHabitScreenDialog(
                onDismissRequest = { showCreateHabitDialog = false },
                viewModel = viewModel,
                onHabitCreated = {
                    showCreateHabitDialog = false
                    showConfirmationScreen = true // Pindah ke layar konfirmasi
                }
            )
        }

        if (showConfirmationScreen) {
            ConfirmationScreen(
                onOk = { showConfirmationScreen = false }
            )
        }
    }
}
