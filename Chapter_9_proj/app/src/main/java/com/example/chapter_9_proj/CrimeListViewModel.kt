package com.example.chapter_9_proj

import androidx.lifecycle.ViewModel
import java.util.UUID
import java.util.Date

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()

    init {
        for( i in 0 until  100) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = false
            )
            crimes.add(crime)
        }
    }
}