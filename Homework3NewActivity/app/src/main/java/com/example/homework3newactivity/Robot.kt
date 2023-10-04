package com.example.homework3newactivity

// R class generates all Id's as an Integer
data class Robot(
    val messageResource: Int,
    var myTurn: Boolean,
    val largeImageResource : Int,
    val smallImageResource: Int,
    var myEnergy : Int,
    var lastPurchase : Int
) {}