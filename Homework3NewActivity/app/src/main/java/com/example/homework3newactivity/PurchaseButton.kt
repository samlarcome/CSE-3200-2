package com.example.homework3newactivity

/*
    There is a lot of overhead when choosing random reward buttons.
    You need to set the correct button text, cost text, and the cost itself.
    I found it easier to store inside a data class, to keep things stored together.
 */
data class PurchaseButton(
    val buttonString : Int,
    val costString : Int,
    val costPrice : Int
) {}
