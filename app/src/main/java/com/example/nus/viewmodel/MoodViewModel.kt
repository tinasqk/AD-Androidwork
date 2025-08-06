package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nus.api.ApiClient
import com.example.nus.model.JournalEntryRequest
import com.example.nus.model.MoodEntry
import com.example.nus.model.MoodType
import com.example.nus.model.TimeOfDay
import kotlinx.coroutines.launch
import java.time.LocalDate

class MoodViewModel : ViewModel() {
    private val _moodEntries = mutableStateListOf<MoodEntry>()
    val moodEntries: List<MoodEntry> get() = _moodEntries

    val selectedDate = mutableStateOf(LocalDate.now())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    val submitSuccess = mutableStateOf(false)

    // 当前用户ID（从登录响应获取）
    var currentUserId: String = ""
    
    // 提交日记条目到后端
    fun submitJournalEntry(
        mood: MoodType,
        entryTitle: String,
        entryText: String,
        emotions: List<String> = emptyList(),
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        if (entryTitle.isBlank()) {
            onError("Please enter a title for your journal entry")
            return
        }

        isLoading.value = true
        error.value = null
        submitSuccess.value = false

        viewModelScope.launch {
            try {
                val journalRequest = JournalEntryRequest(
                    userId = currentUserId,
                    mood = mood.value,
                    entryTitle = entryTitle,
                    entryText = entryText,
                    emotions = emotions
                )

                println("Submitting journal entry: userId=$currentUserId, mood=${mood.value}, title=$entryTitle")
                val response = ApiClient.journalApiService.submitJournalEntry(journalRequest)

                if (response.isSuccessful) {
                    // 添加到本地列表
                    addMoodEntryLocal(mood, TimeOfDay.MORNING, entryTitle, entryText, emotions)
                    submitSuccess.value = true
                    onSuccess()
                    println("Journal entry submitted successfully")
                } else {
                    val errorMessage = "Failed to submit journal entry: ${response.code()} - ${response.message()}"
                    println("Submit error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Submit exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    // 本地添加心情条目（保持原有功能）
    private fun addMoodEntryLocal(
        mood: MoodType,
        timeOfDay: TimeOfDay,
        entryTitle: String = "",
        entryText: String = "",
        emotions: List<String> = emptyList()
    ) {
        val existingEntryIndex = _moodEntries.indexOfFirst {
            it.timeOfDay == timeOfDay && it.date == selectedDate.value
        }

        if (existingEntryIndex >= 0) {
            _moodEntries[existingEntryIndex] = MoodEntry(
                id = _moodEntries[existingEntryIndex].id,
                mood = mood,
                timeOfDay = timeOfDay,
                date = selectedDate.value,
                entryTitle = entryTitle,
                entryText = entryText,
                emotions = emotions,
                userId = currentUserId
            )
        } else {
            _moodEntries.add(
                MoodEntry(
                    mood = mood,
                    timeOfDay = timeOfDay,
                    date = selectedDate.value,
                    entryTitle = entryTitle,
                    entryText = entryText,
                    emotions = emotions,
                    userId = currentUserId
                )
            )
        }
    }

    // 兼容性方法（保持原有API）
    fun addMoodEntry(mood: MoodType, timeOfDay: TimeOfDay, notes: String = "") {
        addMoodEntryLocal(mood, timeOfDay, notes, notes)
    }
    
    // 获取所有日记条目
    fun loadAllJournalEntries(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (currentUserId.isEmpty()) {
            onError("User not logged in")
            return
        }

        isLoading.value = true
        error.value = null

        viewModelScope.launch {
            try {
                val response = ApiClient.journalApiService.getAllJournalEntries(currentUserId)

                if (response.isSuccessful) {
                    response.body()?.let { journalEntries ->
                        _moodEntries.clear()
                        _moodEntries.addAll(journalEntries.map { MoodEntry.fromJournalEntryResponse(it) })
                        onSuccess()
                        println("Loaded ${journalEntries.size} journal entries")
                    } ?: run {
                        onError("Empty response from server")
                    }
                } else {
                    val errorMessage = "Failed to load journal entries: ${response.code()} - ${response.message()}"
                    println("Load error: $errorMessage")
                    onError(errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Network error: ${e.message}"
                println("Load exception: $errorMessage")
                onError(errorMessage)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getMoodForTimeOfDay(timeOfDay: TimeOfDay): MoodType? {
        return _moodEntries.find {
            it.timeOfDay == timeOfDay && it.date == selectedDate.value
        }?.mood
    }

    fun changeSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }

    fun clearError() {
        error.value = null
    }

    fun setUserId(userId: String) {
        currentUserId = userId
    }
} 