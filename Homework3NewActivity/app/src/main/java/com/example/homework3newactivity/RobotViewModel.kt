package com.example.homework3newactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

// const are only allowed at the top level
// gets loaded at compile time

/*
    savedStateHandle is memory that is loaded outside your application
        - in case OS kills memory used in application
 */
private const val TAG = "RobotViewModel"
private const val TURN_COUNT_KEY = "TURN_COUNT_KEY"
const val IS_PURCHASE_KEY = "IS_PURCHASE_KEY"

class RobotViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    init {
        Log.d(TAG, "Instance of RobotViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "Instance of RobotViewModel about to be destroyed/cleared")
    }

    /*
        - turnCount used to keep track of which robot is big
        - set to 1 initially to keep in bounds in MainActivity
     */
    var turnCount : Int
        get() = savedStateHandle.get(TURN_COUNT_KEY) ?: 0   // if NULL, set to 1
        set(value) = savedStateHandle.set(TURN_COUNT_KEY, value)

    // no private keyword - needs to be used by MainActivity()
    fun advanceTurn() {
        turnCount++
        if(turnCount > 3) { turnCount = 1}
    }
}
