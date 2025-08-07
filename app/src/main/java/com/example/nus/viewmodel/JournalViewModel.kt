// app/src/main/java/com/example/nus/viewmodel/JournalViewModel.kt

package com.example.nus.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.nus.model.JournalEntry
import java.time.LocalDateTime

class JournalViewModel : ViewModel() {
    // backing list of model.JournalEntry
    private val _journalList = mutableStateListOf<JournalEntry>()
    val journalList: List<JournalEntry> get() = _journalList

    init {
        // Optional: load some dummy data for testing
        loadMockEntries()
    }

    private fun loadMockEntries() {
        val now = LocalDateTime.now()
        val userId = "test_user"
        _journalList.addAll(
            listOf(
                JournalEntry(
                    user = userId,
                    entryTitle = "Those Goddamn Ducks...",
                    entryText = "I can’t stop worrying about the ducks.",
                    date = now
                ),
                JournalEntry(
                    user = userId,
                    entryTitle = "Gabagool",
                    entryText = "Had some amazing gabagool today.",
                    date = now.minusDays(1)
                ),
                JournalEntry(
                    user = userId,
                    entryTitle = "Nostradamus...",
                    entryText = "Felt prophetic vibes.",
                    date = now.minusDays(2)
                )
            )
        )
    }

    /**
     * Add a new entry for the given user
     */
    fun addEntry(
        user: String,
        title: String,
        text: String,
        date: LocalDateTime = LocalDateTime.now()
    ) {
        _journalList.add(
            JournalEntry(
                user = user,
                entryTitle = title,
                entryText = text,
                date = date
            )
        )
    }

    /**
     * Get a single entry by title (or you can use an index or id)
     */
    fun getEntryByTitle(title: String): JournalEntry? =
        _journalList.find { it.entryTitle == title }
}
