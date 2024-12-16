package com.example.papb.fragment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.papb.data.habit.Habit
import com.example.papb.data.habit.HabitViewModel

@Composable
fun ProgressScreen(viewModel: HabitViewModel) {
    val habits by viewModel.habits.collectAsState()
    val achievedCount = habits.count { it.completedCount >= (it.yourGoal.toIntOrNull() ?: 1) }
    val totalCount = habits.size
    val progressPercentage = if (totalCount > 0) (achievedCount.toFloat() / totalCount) * 100 else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8FF))
            .padding(horizontal = 16.dp)
    ) {
        // Header
        ProgressHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // Circular Progress
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            CircularProgressIndicator(
                progress = progressPercentage / 100f,
                color = Color(0xFFFF7043),
                trackColor = Color(0xFFECEFF1),
                strokeWidth = 12.dp,
                modifier = Modifier.size(140.dp)
            )
            Text(
                text = "${progressPercentage.toInt()}%",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Achieved Summary
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AchievedStatus(icon = Icons.Default.CheckCircle, count = achievedCount, label = "Goals Achieved", color = Color(0xFF00C853))
            AchievedStatus(icon = Icons.Default.Close, count = totalCount - achievedCount, label = "Goals Unachieved", color = Color(0xFFFF7043))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Goals List
        Text(
            text = "Your Goals",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2F2F2F)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            items(habits) { habit ->
                GoalCard(habit)
            }
        }
    }
}


@Composable
fun ProgressHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Progress", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2F2F2F))
            Text("Progress Report", fontSize = 16.sp, color = Color.Gray)
        }
        InputChip(
            selected = true,
            onClick = { /* Dropdown logic */ },
            label = {
                Text("This Month", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2F2F2F))
            },
            leadingIcon = {
                Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
            },
            shape = RoundedCornerShape(8.dp),
            colors = InputChipDefaults.inputChipColors(containerColor = Color(0xFFE7E7E7))
        )
    }
}

@Composable
fun AchievedStatus(icon: ImageVector, count: Int, label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text("$count $label", color = color, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun GoalCard(habit: Habit) {
    val goal = habit.yourGoal.toIntOrNull() ?: 1 // Jika bukan angka, default ke 1
    val completed = habit.completedCount
    val progress = (completed.toFloat() / goal).coerceIn(0f, 1f)
    val isAchieved = progress >= 1f

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = habit.habitName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = progress,
                    color = Color(0xFF00C853),
                    trackColor = Color(0xFFE0E0E0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$completed / $goal days", // Menampilkan progress numerik
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Text(
                text = if (isAchieved) "Achieved" else "Unachieved",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isAchieved) Color(0xFF00C853) else Color(0xFFFF7043),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}



