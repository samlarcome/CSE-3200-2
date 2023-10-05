package com.example.homework3newactivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle


private const val TURN_COUNT_KEY = "com.bignerdranch.android.robot.TURN_COUNT_KEY"
private const val ENERGY_COUNT_KEY = "com.bignerdranch.android.robot.ENERGY_COUNT_KEY"
private const val PURCHASE_LIST_KEY = "com.bignerdranch.android.robot.PURCHASE_LIST_KEY"
private const val PURCHASED_BUTTONS = "com.bignerdranch.android.robot.PURCHASED_BUTTONS"


class RobotViewModel(private val savedStateHandle : SavedStateHandle) : ViewModel() {
    /*
        This class is used to store relevant information about the MainActivity in case the screen is turned, etc...

        1: integerList/energyList:
            - Used to store how much energy each robot has in Red, White, Yellow order.
            - A list of integers, where first index is Red's energy, second is White's, etc...
            - We will need to increase energy by 1 each turn ( hence increaseEnergy() )
            - Will also need to subtract an energy amount that was spent ( hence updateEnergy )

        2: purchaseList/purchases:
            - Used to store each robot's purchases.
            - [ [D, C, G] , [B, D] , [E, A, F] ] would be Red's, White's, Yellow's purchases respectively.
            - We would want to add a purchase to a robot's list of purchases ( hence addPurchaseToList() )

        3: Turn count is not new

        4: purchasedButtons/disabledButtons
            - I saved a list of Buttons in robotViewModel, so if one robot clicked a button, none other could.
            - I based it on the string that is inside each button (e.g. 'Reward A') because it is unique for each.
            - We would want to add a button that is disabled, so that none others could click it ( hence addDisabledButton() )
     */


    // 1 : ******************************************************************************************************************************
    private var integerList: ArrayList<Int> = savedStateHandle.get(ENERGY_COUNT_KEY) ?: ArrayList<Int>(listOf(0,0,0))

    val energyList: List<Int>
        get() = integerList

    fun updateEnergy(index : Int, energyVal : Int) { integerList[index] += energyVal }
    fun increaseEnergy() { integerList[turnCount - 1] += 1 }
    // 2 : ******************************************************************************************************************************

    private var purchaseList: ArrayList<ArrayList<String>> = savedStateHandle.get(PURCHASE_LIST_KEY) ?: ArrayList<ArrayList<String>>(listOf(ArrayList(), ArrayList(), ArrayList()))

    val purchases: List<List<String>>
        get() = purchaseList

    fun addPurchaseToList(index : Int, purchase : String) { purchaseList[index].add(purchase) }

    // 3 : ********************************************************************************************************************************
    var turnCount : Int
        get() = savedStateHandle.get(TURN_COUNT_KEY) ?: 0
        set(value) = savedStateHandle.set(TURN_COUNT_KEY, value)

    fun advanceTurn() {
        turnCount++
        if(turnCount > 3) { turnCount = 1}
    }

    // 4 : ********************************************************************************************************************************
    private var purchasedButtons: MutableSet<String> = savedStateHandle.get(PURCHASED_BUTTONS) ?: mutableSetOf<String>()
    val disabledButtons: Set<String>
        get() = purchasedButtons

    fun addDisabledButton(id : String) {
        purchasedButtons.add(id)
    }

    // ********************************************************************************************************************************

}
