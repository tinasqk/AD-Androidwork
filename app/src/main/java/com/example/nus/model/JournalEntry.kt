package com.example.nus.model

import java.time.LocalDate
import java.time.LocalDateTime

data class JournalEntry(
    val id: String = System.currentTimeMillis().toString(),
    val title: String = "",
    val content: String = "",
    val date: LocalDate = LocalDate.now(),
    val timestamp: LocalDateTime = LocalDateTime.now()
)
