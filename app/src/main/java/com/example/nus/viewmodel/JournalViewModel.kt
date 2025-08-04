package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.nus.model.JournalEntry
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class JournalViewModel : ViewModel() {
    private val _journalEntries = mutableStateListOf<JournalEntry>()
    val journalEntries: List<JournalEntry> get() = _journalEntries
    
    val selectedDate = mutableStateOf(LocalDate.now())
    val title = mutableStateOf("")
    val content = mutableStateOf("")
    
    // Format date like the original XML: "Today is 23rd July 2025."
    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("d'${getDaySuffix(selectedDate.value.dayOfMonth)}' MMMM yyyy")
        return "Today is ${selectedDate.value.format(formatter)}."
    }
    
    private fun getDaySuffix(day: Int): String {
        return when {
            day in 11..13 -> "th"
            day % 10 == 1 -> "st"
            day % 10 == 2 -> "nd"
            day % 10 == 3 -> "rd"
            else -> "th"
        }
    }
    
    fun saveJournalEntry() {
        if (title.value.isNotBlank() || content.value.isNotBlank()) {
            // Check if an entry already exists for this date
            val existingEntryIndex = _journalEntries.indexOfFirst { 
                it.date == selectedDate.value 
            }
            
            // If entry exists, update it; otherwise add new entry
            if (existingEntryIndex >= 0) {
                _journalEntries[existingEntryIndex] = JournalEntry(
                    id = _journalEntries[existingEntryIndex].id,
                    title = title.value,
                    content = content.value,
                    date = selectedDate.value
                )
            } else {
                _journalEntries.add(
                    JournalEntry(
                        title = title.value,
                        content = content.value,
                        date = selectedDate.value
                    )
                )
            }
        }
    }
    
    fun loadEntryForDate(date: LocalDate) {
        selectedDate.value = date
        val entry = _journalEntries.find { it.date == date }
        title.value = entry?.title ?: ""
        content.value = entry?.content ?: ""
    }
    
    fun clearCurrentEntry() {
        title.value = ""
        content.value = ""
    }
    
    fun saveAndGoHome(): Boolean {
        saveJournalEntry()
        return true // Indicate successful save
    }
    
    fun saveAndContinue(): Boolean {
        saveJournalEntry()
        return true // Indicate successful save
    }
}
