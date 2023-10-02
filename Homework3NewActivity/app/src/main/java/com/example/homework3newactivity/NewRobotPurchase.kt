package com.example.homework3newactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.app.Activity
import android.content.Context
import android.renderscript.ScriptGroup.Binding
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels

/*
    Extras are KEY-VALUE pairs that are used to pass data between activities (attached to intents)
    Keys are typically string values, values are the data (strings, int, bool)

    - EXTRA_ROBOT_ENERGY            = Key for current robot energy amount required by RobotPurchaseActivity
    - EXTRA_ROBOT_IMAGE_RESOURCE    = Key for current robot image resource required by RobotPurchaseActivity
    - EXTRA_ROBOT_ITEM_PURCHASED    = Key for the last item purchased by current robot, sent back to main activity
    - EXTRA_ROBOT_ENERGY_SPENT      = Key for total energy spent by current robot, sent back to main activity
 */
private const val EXTRA_ROBOT_ENERGY = "com.bignerdranch.android.robot.current_robot_energy"
private const val EXTRA_ROBOT_IMAGE_RESOURCE = "com.bignerdranch.android.robot.current_image_resource"
const val EXTRA_ROBOT_ITEM_PURCHASED = "com.bignerdranch.android.robot.robot_item_purchased"
const val EXTRA_ROBOT_ENERGY_SPENT = "com.bignerdranch.android.robot.robot_energy_spent"

class NewRobotPurchase : AppCompatActivity() {

    private lateinit var rewardButtonA : Button
    private lateinit var rewardButtonB : Button
    private lateinit var rewardButtonC : Button
    private lateinit var robotImage : ImageView
    private lateinit var robotEnergyAvailable : TextView

    /*


     */
    private var robotEnergy = 0
    private var imageResource = 0
    private var energySpent = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_robot_purchase)

        // set energySpent to zero each time RobotPurchase Activity is created, want to track spent in this session.
        energySpent = 0

        rewardButtonA = findViewById(R.id.buy_reward_a)
        rewardButtonB = findViewById(R.id.buy_reward_b)
        rewardButtonC = findViewById(R.id.buy_reward_c)
        robotImage = findViewById(R.id.robot_image)
        robotEnergyAvailable = findViewById(R.id.robot_energy_to_spend)

        rewardButtonA.setOnClickListener { _ : View ->
            makePurchase(1)
        }
        rewardButtonB.setOnClickListener { _ : View ->
            makePurchase(2)
        }
        rewardButtonC.setOnClickListener { _ : View ->
            makePurchase(3)
        }

        // get the extra energy passed from main and update the energy display
        robotEnergy = intent.getIntExtra(EXTRA_ROBOT_ENERGY, 0)
        robotEnergyAvailable.text = robotEnergy.toString()

        // get the extra imageResource (which image should be set) from main and update the imageView
        imageResource = intent.getIntExtra(EXTRA_ROBOT_IMAGE_RESOURCE, 0)
        robotImage.setImageResource(imageResource)
    }

    /*
        ENCAPSULATION: no reason for MainActivity to know the implementation details of what RobotPurchase Activity
                        expects as extras on its Intent (the amount the robot has & the image resource)

        A companion object allows you to access functions w/out having an instance of the class
     */
    companion object {
        fun newIntent(packageContext : Context, robotEnergy : Int, imageResource: Int) : Intent {
            return Intent(packageContext, NewRobotPurchase::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robotEnergy)
                putExtra(EXTRA_ROBOT_IMAGE_RESOURCE, imageResource)
            }
        }
    }


    private fun makePurchase(costOfPurchase: Int) {
        /*
            Handle the logic when the current robot wants to make a purchase.
            A robot can only make a purchase if they have enough energy (>= costOfPurchase).
            Make a toast equal depending on how much the current robot spends (set string in when statement).
            Subtract the energy spent from the robotEnergy, and update the energy counter on screen.
            Add the amount spent to energySpent, to keep track of total energy spend (to send back to Main Activity).
            Create an Intent with robotEnergy & energySpent as extras and set the result of the current activity with that Intent
        */
        if(robotEnergy >= costOfPurchase) {
            val s1 = when (costOfPurchase) {
                1 -> getString(R.string.reward_a_purchase)
                2 -> getString(R.string.reward_b_purchase)
                3 -> getString(R.string.reward_c_purchase)
                else -> getString(R.string.error_reward)
            }

            Toast.makeText(this, s1, Toast.LENGTH_SHORT).show()
            robotEnergy -= costOfPurchase
            robotEnergyAvailable.text = robotEnergy.toString()
            energySpent += costOfPurchase

            setItemPurchased(costOfPurchase, energySpent)
        } else {
            Toast.makeText(this, R.string.insufficient, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setItemPurchased(robotPurchaseMade : Int, robotEnergySpent : Int) {
        /*
            Responsible for creating an Intent with extras that represent the purchase made and energy spent.
            Setting the result of the current activity (RobotPurchase) with the Intent to send data back to the calling activity (Main Activity).
            - Send the most recent purchase made, to raise a toast in MainActivity
            - Send total energy spent by current robot this session, to update that robot's energy total
        */
        val data = Intent().apply{
            putExtra(EXTRA_ROBOT_ITEM_PURCHASED, robotPurchaseMade)
            putExtra(EXTRA_ROBOT_ENERGY_SPENT, robotEnergySpent)
        }
        setResult(Activity.RESULT_OK, data)
    }
}