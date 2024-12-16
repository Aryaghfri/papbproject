package com.example.papb.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.papb.R
import com.example.papb.data.habit.HabitViewModel

val nunito = FontFamily(Font(R.font.nunito))


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreenDialog(
    onDismissRequest: () -> Unit,
    viewModel: HabitViewModel,
    onHabitCreated: () -> Unit // Callback untuk navigasi ke layar konfirmasi
) {
    var yourGoal by remember { mutableStateOf("") }
    var habitName by remember { mutableStateOf("") }
    var selectedPeriod by remember { mutableStateOf("1 Month (30 Days)") }
    var selectedHabitType by remember { mutableStateOf("Everyday") }

    val periodOptions = listOf("1 Week (7 Days)", "1 Month (30 Days)", "1 Year (360 Days)")
    val habitTypeOptions = listOf("Everyday", "Once a Week", "Every 3 Days")

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFFCFCFF), // Background sesuai referensi
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Create New Habit Goal",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF2F2F2F),
                        fontSize = 18.sp
                    )
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFA0A0A0)
                        )
                    }
                }

                // Your Goal Input
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Your Goal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2F2F2F),
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = yourGoal,
                        onValueChange = { yourGoal = it },
                        placeholder = { Text("Enter your goal") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFEDEDED),
                            focusedBorderColor = Color(0xFF006FFD)
                        )
                    )
                }

                // Habit Name Input
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Habit Name",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2F2F2F),
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = habitName,
                        onValueChange = { habitName = it },
                        placeholder = { Text("Enter habit name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.White,
                            unfocusedBorderColor = Color(0xFFEDEDED),
                            focusedBorderColor = Color(0xFF006FFD)
                        )
                    )
                }

                // Period Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Period",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2F2F2F),
                        fontSize = 14.sp
                    )
                    DropdownMenu(
                        options = periodOptions,
                        selectedOption = selectedPeriod,
                        onOptionSelected = { selectedPeriod = it }
                    )
                }

                // Habit Type Dropdown
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Habit Type",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2F2F2F),
                        fontSize = 14.sp
                    )
                    DropdownMenu(
                        options = habitTypeOptions,
                        selectedOption = selectedHabitType,
                        onOptionSelected = { selectedHabitType = it }
                    )
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            if (yourGoal.isNotBlank() && habitName.isNotBlank()) {
                                viewModel.addHabit(
                                    yourGoal = yourGoal,
                                    habitName = habitName,
                                    period = selectedPeriod,
                                    habitType = selectedHabitType
                                )
                                onHabitCreated() // Navigasi ke layar konfirmasi
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Text("Create New", color = Color.White)
                    }
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", color = Color(0xFF5D5D5D))
                    }
                }
            }
        }
    }
}


@Composable
fun ConfirmationScreen(
    onOk: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(), // Pastikan layar penuh
            color = Color.White // Warna latar belakang
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ikon Konfirmasi
                Icon(
                    painter = painterResource(id = R.drawable.baseline_done_24), // Ganti dengan ikon Anda
                    contentDescription = "Confirmation Icon",
                    tint = Color(0xFF006FFD),
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Judul
                Text(
                    text = "Done!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006FFD)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Pesan Konfirmasi
                Text(
                    text = "New Habit Goal has added\nLet's do the best to achieve your goal!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Tombol OK
                Button(
                    onClick = { onOk() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006FFD)) // Warna oranye
                ) {
                    Text("OK", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun DropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight() // Tambahkan ini
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = selectedOption)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

