package com.example.homework1robotgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    // putting up here makes them Global - promising they will be non-null before use
    private lateinit var redRobotImage: ImageView
    private lateinit var whiteRobotImage: ImageView
    private lateinit var yellowRobotImage: ImageView

    private lateinit var clockwiseButton: ImageButton
    private lateinit var counterClockwiseButton: ImageButton

    private var turnCount = 0
    private var firstClickCW = 0

    // ? = it is okay if it is null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        redRobotImage = findViewById(R.id.red_robot)
        whiteRobotImage = findViewById(R.id.white_robot)
        yellowRobotImage = findViewById(R.id.yellow_robot)

        clockwiseButton = findViewById(R.id.cw_arrow)
        counterClockwiseButton = findViewById(R.id.ccw_arrow)

        counterClockwiseButton.setOnClickListener { view: View -> rotateCCW() }
        clockwiseButton.setOnClickListener { view: View -> rotateCW() }
    }

    private fun rotateCW() {
        if (firstClickCW == 0) {
            redRobotLarge()
            firstClickCW = -1
            turnCount++
            return
        }

        turnCount--

        if (turnCount < 0) { turnCount = 2 }

        when(turnCount) {
            1 -> redRobotLarge()
            2 -> whiteRobotLarge()
            0 -> yellowRobotLarge()
        }
    }

    private fun rotateCCW() {
        turnCount++

        if (turnCount > 2) { turnCount = 0 }

        when(turnCount) {
            1 -> redRobotLarge()
            2 -> whiteRobotLarge()
            0 -> yellowRobotLarge()
        }
    }

    private fun redRobotLarge() {
        redRobotImage.setImageResource(R.drawable.king_of_detroit_robot_red_large)
        whiteRobotImage.setImageResource(R.drawable.king_of_detroit_robot_white_small)
        yellowRobotImage.setImageResource(R.drawable.king_of_detroit_robot_yellow_small)
    }

    private fun yellowRobotLarge() {
        redRobotImage.setImageResource(R.drawable.king_of_detroit_robot_red_small)
        whiteRobotImage.setImageResource(R.drawable.king_of_detroit_robot_white_small)
        yellowRobotImage.setImageResource(R.drawable.king_of_detroit_robot_yellow_large)
    }

    private fun whiteRobotLarge() {
        redRobotImage.setImageResource(R.drawable.king_of_detroit_robot_red_small)
        whiteRobotImage.setImageResource(R.drawable.king_of_detroit_robot_white_large)
        yellowRobotImage.setImageResource(R.drawable.king_of_detroit_robot_yellow_small)
    }

}