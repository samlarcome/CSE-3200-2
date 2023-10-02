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


    /*
        purchaseLauncher is registered to handle the result of starting an activity. It processes the result when the launched activity (RobotPurchase) finishes.
        Lambda - if the RobotPurchase activity exits ok (hit the back button, don't swipe up), handle the Extra's that are sent back from RobotPurchase.

        - EXTRA_ROBOT_ITEM_PURCHASED = the latest amount purchased by the current robot
        - EXTRA_ROBOT_ENERGY_SPENT = the total amount spent by current robot while in the RobotPurchase Activity (to update it)
            - robot energy is not in RobotViewModel (maybe in task 3???)
     */
    private val purchaseLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // capture this data for a toast
            latestPurchaseCost = result.data?.getIntExtra(EXTRA_ROBOT_ITEM_PURCHASED, 0) ?: 0
            totalEnergySpent = result.data?.getIntExtra(EXTRA_ROBOT_ENERGY_SPENT, 0) ?: 0
            robots[robotViewModel.turnCount - 1].myEnergy -= totalEnergySpent
            Toast.makeText(this, "The Latest Purchase Was For $latestPurchaseCost Energy!", Toast.LENGTH_SHORT).show()
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

        /*
            Set on click event listener for the make purchase button that launches the Robot Purchase Activity
            We should only launch the Activity if any robot has had a turn (when turnCount != 0 ... it is initially 0, but never again after a single turn)
            Conditionally launch an activity using purchaseLauncher. The intent for launching the activity is created using NewRobotPurchase.newIntent (static method)
            NewRobotPurchase.newIntent requires 2 extras
                - the energy of current robot
                - the id of largeImageResource for current robot (to set the correct image in new activity)
         */
        makePurchase.setOnClickListener { view : View ->
            if ((robotViewModel.turnCount) != 0) {
                val intent = NewRobotPurchase.newIntent(this, robots[robotViewModel.turnCount - 1].myEnergy, robots[robotViewModel.turnCount - 1].largeImageResource)
                purchaseLauncher.launch(intent)
            }
        }


        // Logic to make sure the correct robot is large upon recreation
        if (robotViewModel.turnCount != 0) {
            setRobotTurn()
            updateMessageBox()
            setRobotImages()
        }
    }

    private fun toggleImage() {
        /*
            Call necessary functions each time a robot is clicked.
            Increment the turn count in the view model - advanceTurn()
            Set the boolean to True for current robots turn & increment the energy by 1 - setRobotTurn()
            Set the current robot's image to large - setRobotImages()
         */
        robotViewModel.advanceTurn()
        updateMessageBox()
        setRobotTurn()
        setRobotImages()
    }

    private fun updateMessageBox() {
        // get the turn count from the robotViewModel class & update the message box
        messageBox.setText(robots[robotViewModel.turnCount - 1].messageResource)
    }

    private fun setRobotTurn() {
        /*
            Iterate through each robot, setting their turn to False.
            Set the current robot's turn to True & increment their energy by 1.
         */
        for(robot in robots) { robot.myTurn = false }

        robots[robotViewModel.turnCount - 1].myTurn = true
        robots[robotViewModel.turnCount - 1].myEnergy += 1
    }

    private fun setRobotImages() {
        //Iterate through the 3 robot's and make the current robot's image large, and all other small
        for(index in 0 .. 2) {
            if(robots[index].myTurn) {
                robotImages[index].setImageResource(robots[index].largeImageResource)
            } else {
                robotImages[index].setImageResource(robots[index].smallImageResource)
            }
        }
    }
}
