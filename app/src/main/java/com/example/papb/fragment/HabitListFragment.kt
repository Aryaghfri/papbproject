package com.example.papb.fragment

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.papb.R
import com.example.papb.data.habit.Habit
import com.example.papb.data.habit.HabitViewModel
import com.example.papb.data.user.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeFragment(
    viewModel: HabitViewModel,
    user: User,
    onNavigateToCreateHabit: () -> Unit
) {
    // Observing habits state
    val habits = viewModel.habits.collectAsState(initial = emptyList())
    val completedHabitsCount = habits.value.count { it.notified }

    // Progress state
    var progress by remember { mutableStateOf(0f) }

    // Menghitung progress
    progress = if (habits.value.isNotEmpty()) {
        completedHabitsCount.toFloat() / habits.value.size
    } else {
        0f
    }

    // Animated progress
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000)
    )

    // Current date string
    val currentDate = remember {
        val dateFormat = SimpleDateFormat("EEE, d MMMM yyyy", Locale.ENGLISH)
        dateFormat.format(Date())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateHabit,
                containerColor = Color(0xFF2897FF),
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = "Add Habit",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DateSection(currentDate = currentDate)
                Spacer(modifier = Modifier.height(8.dp))
                GreetingSection(userName = user.name)
                Spacer(modifier = Modifier.height(24.dp))
                HeaderProgressSection(
                    progress = animatedProgress,
                    totalHabits = habits.value.size,
                    completedHabits = completedHabitsCount
                )
            }
            item {
                HabitListWithHeader(
                    habits = habits.value,
                    viewModel = viewModel,
                    onEdit = { habit -> println("Edit habit: ${habit.habitName}") },
                    onDelete = { habit -> println("Delete habit: ${habit.habitName}") },
                    onSeeAll = { println("Navigate to See All") },
                    onProgressUpdated = {
                        // Update progress
                        progress = if (habits.value.isNotEmpty()) {
                            habits.value.count { it.notified }.toFloat() / habits.value.size
                        } else {
                            0f
                        }
                    }
                )
            }
            item {
                YourGoalsSection(
                    habits = habits.value.filter { it.period != "1 Week (7 Days)" },
                    onDeleteHabit = { habitId -> viewModel.deleteHabit(habitId) }
                )
            }
        }
    }
}

@Composable
fun HabitListWithHeader(
    habits: List<Habit>,
    viewModel: HabitViewModel,
    onEdit: (Habit) -> Unit,
    onDelete: (Habit) -> Unit,
    onSeeAll: () -> Unit,
    onProgressUpdated: () -> Unit
) {
    val nunito = FontFamily(Font(R.font.nunito))
    val currentDate = remember {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.format(Date())
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 0.1.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today Habit",
                    style = TextStyle(
                        fontFamily = nunito,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        color = Color(0xFF006FFD)
                    )
                )
                TextButton(onClick = { onSeeAll() }) {
                    Text(
                        text = "See all",
                        style = TextStyle(
                            fontFamily = nunito,
                            fontWeight = FontWeight.Thin,
                            fontSize = 17.sp,
                            color = Color(0xFFFF9800)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            habits.take(3).forEach { habit ->
                HabitCard(
                    habit = habit,
                    currentDate = currentDate,
                    onHabitCheckedChange = { isChecked ->
                        if (isChecked && habit.lastCompletedDate != currentDate) {
                            viewModel.completeHabit(habit)
                        } else if (!isChecked && habit.lastCompletedDate == currentDate) {
                            viewModel.undoCompleteHabit(habit)
                        }
                        onProgressUpdated() // Panggil callback progress update
                    },
                    onEdit = { onEdit(habit) },
                    onDelete = { viewModel.deleteHabit(habit.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun HabitCard(
    habit: Habit,
    currentDate: String,
    onHabitCheckedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val nunito = FontFamily(Font(R.font.nunito))
    val cardBackgroundColor = if (habit.lastCompletedDate == currentDate) Color(0xFFEAF2FF) else Color(0xFFFBFBFB)
    val textColor = Color(0xFF231F1F)

    var expanded by remember { mutableStateOf(false) } // State untuk DropdownMenu

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = habit.habitName,
                style = TextStyle(
                    fontFamily = nunito,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = textColor
                ),
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (habit.lastCompletedDate == currentDate) Color(0xFF006FFD) else Color(0xFFFBFBFB),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 2.dp,
                        color = if (habit.lastCompletedDate == currentDate) Color(0xFF006FFD) else Color.Gray,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                        val isChecked = habit.lastCompletedDate != currentDate
                        onHabitCheckedChange(isChecked)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (habit.lastCompletedDate == currentDate) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_done_24),
                        contentDescription = "Checked",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // IconButton untuk titik tiga
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        painter = painterResource(R.drawable.dot),
                        contentDescription = "More Options",
                        tint = Color.Gray
                    )
                }

                // DropdownMenu untuk Edit dan Delete
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onEdit()
                        },
                        text = { Text("Edit") }
                    )
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onDelete()
                        },
                        text = { Text("Delete") }
                    )
                }
            }
        }
    }
}

@Composable
fun DateSection(currentDate: String) {
    val nunito = FontFamily(
        Font(R.font.nunito)
    )
    Text(
        text = currentDate,
        style = TextStyle(
            fontFamily = nunito,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF231F1F)
        ),
        modifier = Modifier.padding(top = 10.dp)
    )
}

@Composable
fun GreetingSection(userName: String) {
    val nunito = FontFamily(
        Font(R.font.nunito)
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Hello, ",
            style = TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                color = Color(0xFF231F1F)
            )
        )
        Text(
            text = "$userName!",
            style = TextStyle(
                fontFamily = nunito,
                fontWeight = FontWeight.Bold,
                fontSize = 34.sp,
                color = Color(0xFFFFA450)
            )
        )
    }
}

@Composable
fun HeaderProgressSection(
    progress: Float,
    totalHabits: Int,
    completedHabits: Int
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 300,
            easing = androidx.compose.animation.core.LinearEasing
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF006FFD), Color(0xFF00BCD4))
                ),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFB4DFFF),
                    strokeWidth = 12.dp
                )
                CircularProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White,
                    strokeWidth = 12.dp
                )
                // Fix overlapping by adjusting text size and alignment
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp, // Adjusted for better scaling
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$completedHabits of $totalHabits habits completed today!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
fun YourGoalsSection(habits: List<Habit>, onDeleteHabit: (String) -> Unit) {
    val nunito = FontFamily(Font(R.font.nunito))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header untuk Your Goals
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Goals",
                    style = TextStyle(
                        fontFamily = nunito,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        color = Color(0xFF006FFD)
                    )
                )
                TextButton(onClick = { /* Handle see all */ }) {
                    Text(
                        text = "See all",
                        style = TextStyle(
                            fontFamily = nunito,
                            fontWeight = FontWeight.Thin,
                            fontSize = 17.sp,
                            color = Color(0xFFFF9800)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Menampilkan daftar goal berdasarkan habit
            habits.forEach { habit ->
                GoalCard(
                    habit = habit,
                    onDelete = { onDeleteHabit(habit.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun GoalCard(habit: Habit, onDelete: () -> Unit) {
    val nunito = FontFamily(Font(R.font.nunito))

    val targetDays = getDaysFromPeriod(habit.period)
    val targetProgress = habit.completedCount.toFloat() / targetDays

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 500,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F7F9)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = habit.yourGoal,
                style = TextStyle(
                    fontFamily = nunito,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF231F1F)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Progress bar dengan animasi
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color(0xFFFF9800),
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${habit.completedCount} from $targetDays days target",
                style = TextStyle(
                    fontFamily = nunito,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = habit.habitType,
                style = TextStyle(
                    fontFamily = nunito,
                    fontSize = 12.sp,
                    color = Color(0xFFFF9800)
                )
            )
        }
    }
}



// Fungsi untuk menghitung progres (0.0 hingga 1.0)
fun calculateProgress(startDate: Long, period: String): Float {
    val currentTime = System.currentTimeMillis()
    val elapsedTime = currentTime - startDate
    val maxTime = when (period) {
        "1 Week (7 Days)" -> 7 * 24 * 60 * 60 * 1000L
        "1 Month (30 Days)" -> 30 * 24 * 60 * 60 * 1000L
        "1 Year (360 Days)" -> 360 * 24 * 60 * 60 * 1000L
        else -> 0
    }
    return (elapsedTime.toFloat() / maxTime).coerceAtMost(1f)
}

fun getDaysFromPeriod(period: String): Int {
    return when (period) {
        "1 Week (7 Days)" -> 7
        "1 Month (30 Days)" -> 30
        "1 Year (360 Days)" -> 360
        else -> 0
    }
}

// Mengkonversi progress (float) ke jumlah hari
fun progressToDays(progress: Float, period: String): Int {
    val maxDays = getDaysFromPeriod(period)
    return (progress * maxDays).toInt()
}
