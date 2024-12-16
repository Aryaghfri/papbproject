package com.example.papb.auth

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.papb.R
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // Tidak ada animasi untuk ukuran input field
    val inputScale = 1f
    val fontSizeAnim = 16f

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.hazzbit),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(100.dp)
                    .scale(inputScale) // Tetap menggunakan scale 1f
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Sign in to your Account",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    lineHeight = 42.sp,
                    letterSpacing = (-0.02).sp
                ),
                fontSize = fontSizeAnim.sp, // Ukuran font tetap
                modifier = Modifier.scale(inputScale) // Tetap menggunakan scale 1f
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your email and password to log in",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                fontSize = (fontSizeAnim * 0.9).sp,
                modifier = Modifier.scale(inputScale)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Email Input
            AnimatedInputField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                inputScale = inputScale,
                fontSizeAnim = fontSizeAnim
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Password Input
            AnimatedInputField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = Icons.Default.Lock,
                visualTransformation = PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                inputScale = inputScale,
                fontSizeAnim = fontSizeAnim
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Forgot Password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { /* Handle Forgot Password */ }) {
                    Text(
                        "Forgot Password?",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF006FFD)),
                        fontSize = (fontSizeAnim * 0.8).sp
                    )
                }
            }

            // Error Message
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Login Button
            // Login Button
            Button(
                onClick = {
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                onLoginSuccess()
                            } else {
                                error = "Password anda salah"
                            }
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006FFD))
            ) {
                Text("Log In", color = Color.White, fontSize = fontSizeAnim.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Or Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text(
                    text = " Or ",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    fontSize = fontSizeAnim.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Google Login Button
            OutlinedButton(
                onClick = { /* Handle Google Login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .scale(inputScale)
            ) {
                Text("Continue with Google", fontSize = fontSizeAnim.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Register Text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account?", color = Color.Gray, fontSize = fontSizeAnim.sp)
                TextButton(onClick = onRegisterClick) {
                    Text(
                        "Sign Up",
                        color = Color(0xFF006FFD),
                        fontWeight = FontWeight.Bold,
                        fontSize = fontSizeAnim.sp
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType,
    inputScale: Float,
    fontSizeAnim: Float
) {
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontSize = fontSizeAnim.sp,
                color = if (isFocused) Color(0xFF006FFD) else Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                leadingIcon,
                contentDescription = "$label Icon",
                tint = if (isFocused) Color(0xFF006FFD) else Color.Gray
            )
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize() // Smooth height change
            .scale(inputScale)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        colors = TextFieldDefaults.run {
            outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF006FFD),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color(0xFF006FFD),
                focusedLabelColor = Color(0xFF006FFD),
                unfocusedLabelColor = Color.Gray
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}

