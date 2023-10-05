package com.example.homework3newactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle

private const val ENERGY_KEY = "com.bignerdranch.android.robot.ENERGY_KEY"
private const val ENERGY_SPENT_KEY = "com.bignerdranch.android.robot.ENERGY_SPENT_KEY"
private const val RAND_BUTTON_INDICES = "com.bignerdranch.android.robot.RAND_BUTTON_INDICES"
private const val PURCHASED_BUTTONS = "com.bignerdranch.android.robot.PURCHASED_BUTTONS"
private const val PURCHASE_LIST_KEY = "com.bignerdranch.android.robot.PURCHASE_LIST_KEY"

/*
        This class is used to store relevant information about the RobotPurchaseActivity in case the screen is turned, etc...

        1: energy:
            - We need to restore the on screen value that shows how much energy the robot currently has.

        2: energySpent:
            - We need to store how much the robot has spent so far, in case the activity is restarted in
              between purchases (e.g. Buy Reward -> Rotate -> Buy Reward).
            - Make purchase updates both of these values.
                - subtracts the cost of purchase from robot's energy
                - adds the cost of purchase to total amount of energySpent

        3: indexList/uniqueIdxList
            - This stores the 3 id's of the random buttons that is chosen each time a robot enters RobotPurchaseActivity.
            - The 3 random rewards should be the same after rotating the screen after entering the PurchaseActivity.

        4: purchasedButtons/disabledButtons
            - I saved a list of Buttons in purchaseViewModel, so if the robot purchases a reward (and therefore disabled),
              it will still be disabled after rotating the screen.
            - I based it on the string that is inside each button (e.g. 'Reward A') because it is unique for each.

        5: purchaseList/purchases
            - Used to store each robot's purchases.
            - If screen turns in the middle of buying things, we want to send the things we bought before back to
              MainActivity to make a toast.
            - [ A, E, F, D, ] would the order this robot is making purchases.
            - We would want to add a purchase to a robot's list of purchases ( hence addPurchaseToList() )
     */

class PurchaseViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {

    // 1 & 2 : ********************************************************************************************************************************
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

    // 3 : ********************************************************************************************************************************
    private var indexList: ArrayList<Int> = savedStateHandle.get(RAND_BUTTON_INDICES) ?: ArrayList<Int>(listOf(0,0,0))
    val uniqueIdxList: List<Int>
        get() = indexList

    fun updateIdxList(index : Int, buttonIdx : Int) {
        indexList[index] = buttonIdx
    }

    // 4 : ********************************************************************************************************************************
    private var purchasedButtons: MutableSet<String> = savedStateHandle.get(PURCHASED_BUTTONS) ?: mutableSetOf<String>()
    val disabledButtons: Set<String>
        get() = purchasedButtons

    fun addDisabledButton(id : String) {
        purchasedButtons.add(id)
    }

    fun isDisabled(id : String) : Boolean {
        return (purchasedButtons.contains(id))
    }
    // 5 : ********************************************************************************************************************************

    private var purchaseList: ArrayList<String> = savedStateHandle.get(PURCHASE_LIST_KEY) ?: ArrayList<String>()

    val purchases: List<String>
        get() = purchaseList

    fun addPurchaseToList( purchase : String) { purchaseList.add(purchase) }

    // ********************************************************************************************************************************
}