package com.example.homework3newactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

private const val TAG = "RobotViewModel"
private const val TURN_COUNT_KEY = "TURN_COUNT_KEY"

class RobotViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "Instance of RobotViewModel about to be destroyed/cleared")
    }

    var turnCount : Int
        get() = savedStateHandle.get(TURN_COUNT_KEY) ?: 0
        set(value) = savedStateHandle.set(TURN_COUNT_KEY, value)

    fun advanceTurn() {
        turnCount++
        if(turnCount > 3) { turnCount = 1}
    }
}
