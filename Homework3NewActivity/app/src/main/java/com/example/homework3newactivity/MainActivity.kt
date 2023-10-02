package com.example.homework3newactivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import android.util.Log
import android.widget.Toast

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var redRobotImage: ImageView
    private lateinit var whiteRobotImage: ImageView
    private lateinit var yellowRobotImage: ImageView
    private lateinit var messageBox: TextView
    private lateinit var makePurchase: Button

    private lateinit var robotImages : MutableList<ImageView>
    private var latestPurchaseCost = 0
    private var totalEnergySpent = 0

    private val robots = listOf(
        Robot(R.string.red_robot_message, false, R.drawable.king_of_detroit_robot_red_large, R.drawable.king_of_detroit_robot_red_small, 0),
        Robot(R.string.white_robot_message, false, R.drawable.king_of_detroit_robot_white_large, R.drawable.king_of_detroit_robot_white_small, 0),
        Robot(R.string.yellow_robot_message, false, R.drawable.king_of_detroit_robot_yellow_large, R.drawable.king_of_detroit_robot_yellow_small, 0)
    )

    private val robotViewModel : RobotViewModel by viewModels()

    private val purchaseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // capture this data for a toast
            latestPurchaseCost = result.data?.getIntExtra(EXTRA_ROBOT_ITEM_PURCHASED, 0) ?: 0
            totalEnergySpent = result.data?.getIntExtra(EXTRA_ROBOT_ENERGY_SPENT, 0) ?: 0
            robots[robotViewModel.turnCount - 1].myEnergy -= totalEnergySpent
            Toast.makeText(this, "The Latest Purchase Was For $latestPurchaseCost Energy!\nMy Total Spent: $totalEnergySpent", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redRobotImage = findViewById(R.id.red_robot)
        whiteRobotImage = findViewById(R.id.white_robot)
        yellowRobotImage = findViewById(R.id.yellow_robot)
        messageBox = findViewById(R.id.message_box)
        makePurchase = findViewById(R.id.make_purchase)

        robotImages = mutableListOf(redRobotImage, whiteRobotImage, yellowRobotImage)

        redRobotImage.setOnClickListener { view: View -> toggleImage() }
        whiteRobotImage.setOnClickListener { view: View -> toggleImage() }
        yellowRobotImage.setOnClickListener { view: View -> toggleImage() }

        makePurchase.setOnClickListener { view : View ->
            if ((robotViewModel.turnCount) != 0) {
                val intent = NewRobotPurchase.newIntent(this, robots[robotViewModel.turnCount - 1].myEnergy, robots[robotViewModel.turnCount - 1].largeImageResource)
                purchaseLauncher.launch(intent)
            }
        }


        // Logic to make sure right robot is large upon recreation
        if (robotViewModel.turnCount != 0) {
            setRobotTurn()
            updateMessageBox()
            setRobotImages()
        }

    }

    private fun toggleImage() {
        robotViewModel.advanceTurn()

        updateMessageBox()
        setRobotTurn()
        setRobotImages()
    }

    private fun updateMessageBox() {
        // get the turn count from the robotViewModel class
        messageBox.setText(robots[robotViewModel.turnCount - 1].messageResource)
    }

    private fun setRobotTurn() {
        for(robot in robots) { robot.myTurn = false }

        robots[robotViewModel.turnCount - 1].myTurn = true
        robots[robotViewModel.turnCount - 1].myEnergy += 1
    }

    private fun setRobotImages() {
        for(index in 0 .. 2) {
            if(robots[index].myTurn) {
                robotImages[index].setImageResource(robots[index].largeImageResource)
            } else {
                robotImages[index].setImageResource(robots[index].smallImageResource)
            }
        }
    }
}
