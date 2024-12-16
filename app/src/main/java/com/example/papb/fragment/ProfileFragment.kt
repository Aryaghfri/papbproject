package com.example.papb.fragment

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.papb.R
import com.example.papb.data.user.UserViewModel

@Composable
fun ProfileScreen(viewModel: UserViewModel) {
    val user by viewModel.currentUser.collectAsState()

    // Gunakan font Nunito
    val nunito = FontFamily(Font(R.font.nunito))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Profile",
            style = TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (user != null) {
            Text(
                text = "Name: ${user?.name}",
                style = TextStyle(
                    fontFamily = nunito,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
            Text(
                text = "Username: ${user?.username}",
                style = TextStyle(
                    fontFamily = nunito,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
            Text(
                text = "Email: ${user?.email}",
                style = TextStyle(
                    fontFamily = nunito,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            )
        } else {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading user data...",
                style = TextStyle(
                    fontFamily = nunito,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
