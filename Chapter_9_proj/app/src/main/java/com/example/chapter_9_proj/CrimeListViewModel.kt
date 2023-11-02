package com.example.chapter_9_proj

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.Date

private const val TAG = "Crime List View Model"

class CrimeListViewModel : ViewModel() {
    val crimes = mutableListOf<Crime>()

    init {
        Log.d(TAG, "about to initialize our crime data")
        viewModelScope.launch{
            Log.d(TAG, "coroutine launched... expect a delay")
            crimes += loadCrimes()
            Log.d(TAG, "crime data should be finished")
        }
    }

    suspend fun loadCrimes() : List<Crime> {
        // delay(5000)
        val result = mutableListOf<Crime>()
        for( i in 0 until  100) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = ((i % 2) == 0)
            )
            result.add(crime)
        }
        return result
    }
}