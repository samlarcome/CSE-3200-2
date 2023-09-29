package com.example.robotgameinclass

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val EXTRA_ROBOT_ENERGY = "com.bignerdranch.android.robot.current_robot_energy"


class RobotPurchase : AppCompatActivity() {

    private lateinit var reward_button_a : Button
    private lateinit var reward_button_b : Button
    private lateinit var reward_button_c : Button
    private lateinit var robot_energy_available : TextView
    private var robot_energy = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.purchase_activity)


        reward_button_a = findViewById(R.id.buy_reward_a)
        reward_button_b = findViewById(R.id.buy_reward_b)
        reward_button_c = findViewById(R.id.buy_reward_c)
        robot_energy_available = findViewById(R.id.robot_energy_to_spend)
    }

    // going to define a function that w
    // hiding the details of what is getting sent along
    companion object {
        fun newIntent(packageContext : Context, robot_energy : Int) : Intent {
            return Intent(packageContext, RobotPurchase::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robot_energy)
            }
        }
    }

    private fun makePurchase(costOfPurchase: Int) {
        if(robot_energy == costOfPurchase) {
            val s1 = when{
                costOfPurchase == 1 -> getString(R.string.reward_a)
                costOfPurchase == 2 -> getString(R.string.reward_b)
                costOfPurchase == 3 -> getString(R.string.reward_c)
                else -> getString(R.string.error_reward)
            }

            val s2 = getString(R.string.purchased)
        }
    }

}