package com.example.robotgameinclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

private const val TAG = "MainActivity"

//const val EXTRA_ROBOT_ENERGY = "com.bignerdranch.android.robot.current_robot_energy"

class MainActivity : AppCompatActivity() {
    /*
        val = kind of like 'Final'
        var = more like variable
        const = static final
     */

    // putting up here makes them Global - promising they will be non-null before use
    private lateinit var redRobotImage: ImageView
    private lateinit var whiteRobotImage: ImageView
    private lateinit var yellowRobotImage: ImageView
    private lateinit var messageBox: TextView
    private lateinit var rewardButton: Button

    private lateinit var robotImages : MutableList<ImageView>

    // listOf is IMMUTABLE
    private val robots = listOf(
        Robot(R.string.red_robot_message, false, R.drawable.king_of_detroit_robot_red_large, R.drawable.king_of_detroit_robot_red_small, 0),
        Robot(R.string.white_robot_message, false, R.drawable.king_of_detroit_robot_white_large, R.drawable.king_of_detroit_robot_white_small, 0),
        Robot(R.string.yellow_robot_message, false, R.drawable.king_of_detroit_robot_yellow_large, R.drawable.king_of_detroit_robot_yellow_small, 0)
    )

    /* by = property delegate ---
        - the activity is destroyed when rotating whether we use view model or not
        - we make use of the view model to bring it back to the state we want
    */
    private val robotViewModel : RobotViewModel by viewModels()

    // ? = it is okay if it is null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redRobotImage = findViewById(R.id.red_robot)
        whiteRobotImage = findViewById(R.id.white_robot)
        yellowRobotImage = findViewById(R.id.yellow_robot)
        messageBox = findViewById(R.id.message_box)

        robotImages = mutableListOf(redRobotImage, whiteRobotImage, yellowRobotImage)

        redRobotImage.setOnClickListener { view: View -> toggleImage() }
        whiteRobotImage.setOnClickListener { view: View -> toggleImage() }
        yellowRobotImage.setOnClickListener { view: View -> toggleImage() }

        rewardButton.setOnClickListener { view : View ->
            // Toast.makeText(this, "Going to make a purchase", Toast.LENGTH_SHORT).show()

            /*
                this =
                ::class.java  =  relic when java was the only folder/language
                    - forcing it to be the java version
             */

            //val intent = Intent(this, RobotPurchase::class.java)
            //intent.putExtra(EXTRA_ROBOT_ENERGY, robots[robotViewModel.turnCount - 1].myEnergy)

            /*
                A companion object is a static method (put class name in front)
             */
            val intent = RobotPurchase.newIntent(this, robots[robotViewModel.turnCount - 1].myEnergy)
            //startActivity(intent)
            purchaseLauncher.launch(intent)
        }

        // Logic to make sure right robot is large upon recreation
        if (robotViewModel.turnCount != 0) {
            setRobotTurn()
            updateMessageBox()
            setRobotImages()
        }
    }


    private val purchaseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        // TODO Later
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
        // get the turn count from the robotViewModel class
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
