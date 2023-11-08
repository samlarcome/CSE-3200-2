package com.example.chapter_9_proj

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.Date

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()
    //val crimes = mutableListOf<Crime>()


    //val crimes = crimeRepository.getCrimes() // FLOW
    // STATE FLOW - present flow of crimes
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes : StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {
        // COROUTINE BUILDER (launch) --- Fxn that creates a new coroutine
        viewModelScope.launch{
            // COROUTINE SCOPE --- can invoke suspending functions

            // To access values within the Flow, you must observe it using the collect{} function
            // collect{} is a suspending function, need to be called in a coroutine scope
            crimeRepository.getCrimes().collect{
                _crimes.value = it
            }
        }
    }

    /* ADDING FLOW
    suspend fun loadCrimes() : List<Crime>{
        return crimeRepository.getCrimes()
    }*/

//    suspend fun loadCrimes() : List<Crime> {
//        delay(5000)
//        val result = mutableListOf<Crime>()
//        for( i in 0 until  100) {
//            val crime = Crime(
//                id = UUID.randomUUID(),
//                title = "Crime #$i",
//                date = Date(),
//                isSolved = ((i % 2) == 0)
//            )
//            result.add(crime)
//        }
//        return result
//    }
}