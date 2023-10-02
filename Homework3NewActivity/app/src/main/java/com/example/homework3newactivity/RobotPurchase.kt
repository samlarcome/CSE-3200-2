package com.example.homework3newactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_ROBOT_ENERGY = "com.bignerdranch.android.robot.current_robot_energy"
private const val EXTRA_ROBOT_ITEM_PURCHASED2 = "com.bignerdranch.android.robot.robot_item_purchased"

class RobotPurchase : AppCompatActivity() {

    private lateinit var rewardButtonA : Button
    private lateinit var rewardButtonB : Button
    private lateinit var rewardButtonC : Button
    private lateinit var robotEnergyAvailable : TextView
    private var robotEnergy = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.purchase_activity)


        rewardButtonA = findViewById(R.id.buy_reward_a)
        rewardButtonB = findViewById(R.id.buy_reward_b)
        rewardButtonC = findViewById(R.id.buy_reward_c)
        robotEnergyAvailable = findViewById(R.id.robot_energy_to_spend)


        // set event listeners
    }

    // going to define a function that w
    // hiding the details of what is getting sent along
    companion object {
        fun newIntent(packageContext : Context, robotEnergy : Int) : Intent {
            return Intent(packageContext, RobotPurchase::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robotEnergy)
            }
        }
    }

    private fun makePurchase(costOfPurchase: Int) {
        if(robotEnergy >= costOfPurchase) {
            val s1 = when (costOfPurchase) {
                1 -> getString(R.string.reward_a_purchase)
                2 -> getString(R.string.reward_b_purchase)
                3 -> getString(R.string.reward_c_purchase)
                else -> getString(R.string.error_reward)
            }

            Toast.makeText(this, s1, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setItemPurchased(robotPurchaseMade : Int) {
        val data = Intent().apply{
            putExtra(EXTRA_ROBOT_ITEM_PURCHASED, robotPurchaseMade)
        }
        setResult(Activity.RESULT_OK, data)
        // Activity.RESULT_CANCELED -- maybe scrolling up inside the purchase activity
    }

}