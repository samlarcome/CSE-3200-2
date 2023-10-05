package com.example.homework3newactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle


private const val TURN_COUNT_KEY = "TURN_COUNT_KEY"
private const val ENERGY_COUNT_KEY = "ENERGY_COUNT_KEY"
private const val PURCHASE_LIST_KEY = "PURCHASE_LIST_KEY"
private const val PURCHASED_BUTTONS = "PURCHASED_BUTTONS"


class RobotViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    // ******************************************************************************************************************************
    private var integerList: ArrayList<Int> = savedStateHandle.get(ENERGY_COUNT_KEY) ?: ArrayList<Int>(listOf(0,0,0))
    // Getter for energyList
    val energyList: List<Int>
        get() = integerList

    // Function to update the list of integers
    fun updateEnergy(index : Int, energyVal : Int) { integerList[index] += energyVal }
    fun increaseEnergy() { integerList[turnCount - 1] += 1 }
    // ******************************************************************************************************************************

    private var purchaseList: ArrayList<ArrayList<String>> = savedStateHandle.get(PURCHASE_LIST_KEY) ?: ArrayList<ArrayList<String>>(listOf(ArrayList(), ArrayList(), ArrayList()))

    val purchases: List<List<String>>
        get() = purchaseList

    fun addPurchaseToList(index : Int, purchase : String) { purchaseList[index].add(purchase) }

    // ********************************************************************************************************************************
    var turnCount : Int
        get() = savedStateHandle.get(TURN_COUNT_KEY) ?: 0
        set(value) = savedStateHandle.set(TURN_COUNT_KEY, value)

    fun advanceTurn() {
        turnCount++
        if(turnCount > 3) { turnCount = 1}
    }

    // ********************************************************************************************************************************
    private var purchasedButtons: MutableSet<String> = savedStateHandle.get(PURCHASED_BUTTONS) ?: mutableSetOf<String>()
    val disabledButtons: Set<String>
        get() = purchasedButtons

    fun addDisabledButton(id : String) {
        purchasedButtons.add(id)
    }

    fun isDisabled(id : String) : Boolean {
        return (purchasedButtons.contains(id))
    }
    // ********************************************************************************************************************************

}
