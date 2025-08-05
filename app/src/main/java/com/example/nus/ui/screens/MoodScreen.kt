package com.example.nus.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nus.model.MoodType
import com.example.nus.model.TimeOfDay
import com.example.nus.viewmodel.MoodViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun MoodScreen(viewModel: MoodViewModel) {
    var journalTitle by remember { mutableStateOf("") }
    var journalContent by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf<MoodType?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Date selector (preserved from original)
        DateSelector(
            selectedDate = viewModel.selectedDate.value,
            onDateSelected = { viewModel.changeSelectedDate(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Main content card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Date display
                val selectedDate = viewModel.selectedDate.value
                val dateText = "Today is ${selectedDate.dayOfMonth}${getDaySuffix(selectedDate.dayOfMonth)} ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.year}."

                Text(
                    text = dateText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Write into your journal, and log how you feel right now.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // How do you feel section
                Text(
                    text = "How do you feel?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // Mood selection buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MoodButton(
                        text = "Very Good",
                        mood = MoodType.VERY_GOOD,
                        isSelected = selectedMood == MoodType.VERY_GOOD,
                        onSelect = { selectedMood = MoodType.VERY_GOOD }
                    )
                    MoodButton(
                        text = "Good",
                        mood = MoodType.GOOD,
                        isSelected = selectedMood == MoodType.GOOD,
                        onSelect = { selectedMood = MoodType.GOOD }
                    )
                    MoodButton(
                        text = "Neutral",
                        mood = MoodType.NEUTRAL,
                        isSelected = selectedMood == MoodType.NEUTRAL,
                        onSelect = { selectedMood = MoodType.NEUTRAL }
                    )
                    MoodButton(
                        text = "Bad",
                        mood = MoodType.BAD,
                        isSelected = selectedMood == MoodType.BAD,
                        onSelect = { selectedMood = MoodType.BAD }
                    )
                    MoodButton(
                        text = "Very Bad",
                        mood = MoodType.VERY_BAD,
                        isSelected = selectedMood == MoodType.VERY_BAD,
                        onSelect = { selectedMood = MoodType.VERY_BAD }
                    )
                }

                // Journal title prompt
                Text(
                    text = "Start with a title to remember this day.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Title input field
                OutlinedTextField(
                    value = journalTitle,
                    onValueChange = { journalTitle = it },
                    placeholder = { Text("Today, I...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                // Journal content input field
                OutlinedTextField(
                    value = journalContent,
                    onValueChange = { journalContent = it },
                    placeholder = { Text("Write about your day...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Save button
                Button(
                    onClick = {
                        selectedMood?.let { mood ->
                            viewModel.addMoodEntry(mood, TimeOfDay.MORNING) // You can modify this based on current time
                            // Here you could also save the journal content
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save & Continue",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onDateSelected(selectedDate.minusDays(1))
        }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Previous Day")
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { showDatePicker = true }
        ) {
            Text(
                text = selectedDate.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Select Date"
            )
        }
        
        IconButton(onClick = {
            onDateSelected(selectedDate.plusDays(1))
        }) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Next Day")
        }
    }
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val newDate = Instant
                            .ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(newDate)
                    }
                    showDatePicker = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun MoodButton(
    text: String,
    mood: MoodType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = when {
        isSelected && mood == MoodType.VERY_BAD -> Color(0xFFDC3545)
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
    }

    val textColor = when {
        isSelected -> Color.White
        else -> MaterialTheme.colorScheme.onSurface
    }

    Button(
        onClick = onSelect,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.height(36.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

// Helper function to get day suffix (1st, 2nd, 3rd, etc.)
fun getDaySuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}

