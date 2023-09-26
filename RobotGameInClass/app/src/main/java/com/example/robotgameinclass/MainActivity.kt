package com.example.robotgameinclass

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels

private const val TAG = "MainActivity"

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

    private lateinit var robotImages : MutableList<ImageView>

    // listOf is IMMUTABLE
    private val robots = listOf(
        Robot(R.string.red_robot_message, false, R.drawable.king_of_detroit_robot_red_large, R.drawable.king_of_detroit_robot_red_small),
        Robot(R.string.white_robot_message, false, R.drawable.king_of_detroit_robot_white_large, R.drawable.king_of_detroit_robot_white_small),
        Robot(R.string.yellow_robot_message, false, R.drawable.king_of_detroit_robot_yellow_large, R.drawable.king_of_detroit_robot_yellow_small)
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

        // d = debug, i = info, v = verbose, e = error
        Log.d(TAG, "onCreate() entered")
        Log.d(TAG, "instance of viewModel created")

        // Logic to make sure right robot is large upon recreation
        if (robotViewModel.turnCount != 0) {
            setRobotTurn()
            updateMessageBox()
            setRobotImages()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart entered")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume entered")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause entered")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop entered")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy entered")
    }


    /*
        Have there been any clicks yet? If not, all robots are large.
        If any robots are clicked, then they rotate. First, only red
        is large, second only white is large, third only yellow is large.
        The cycle repeats... RED, WHITE, YELLOW, RED, etc.
     */
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
