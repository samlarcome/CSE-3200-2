package com.example.homework3newactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

private const val ENERGY_KEY = "ENERGY_KEY"
private const val ENERGY_SPENT_KEY = "ENERGY_SPENT_KEY"
private const val RAND_BUTTON_INDICES = "RAND_BUTTON_INDICES"
private const val PURCHASED_BUTTONS = "PURCHASED_BUTTONS"
private const val PURCHASE_LIST_KEY = "PURCHASE_LIST_KEY"

/*
    Need to save:
        - energy count (next to robot) --- after purchasing???
        - Save the order of buttons
        - Save which buttons have been purchased
 */

class PurchaseViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    // ********************************************************************************************************************************
    var energy : Int
        get() = savedStateHandle.get(ENERGY_KEY) ?: 0
        set(value) = savedStateHandle.set(ENERGY_KEY, value)

    var energySpent : Int
        get() = savedStateHandle.get(ENERGY_SPENT_KEY) ?: 0
        set(value) = savedStateHandle.set(ENERGY_SPENT_KEY, value)

    fun makePurchase(energyVal : Int) {
        energy -= energyVal
        energySpent += energyVal
    }

    // ********************************************************************************************************************************
    private var indexList: ArrayList<Int> = savedStateHandle.get(RAND_BUTTON_INDICES) ?: ArrayList<Int>(listOf(0,0,0))
    val uniqueIdxList: List<Int>
        get() = indexList

    fun updateIdxList(index : Int, buttonIdx : Int) {
        indexList[index] = buttonIdx
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

    private var purchaseList: ArrayList<Int> = savedStateHandle.get(PURCHASE_LIST_KEY) ?: ArrayList<Int>()

    val purchases: List<Int>
        get() = purchaseList

    fun addPurchaseToList( purchase : Int) { purchaseList.add(purchase) }

    // ********************************************************************************************************************************
}