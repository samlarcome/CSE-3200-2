package com.example.homework3newactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import java.util.Random

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
const val EXTRA_DISABLED_BUTTONS = "com.bignerdranch.android.robot.disabled_buttons"

class NewRobotPurchase : AppCompatActivity() {

    private val purchaseViewModel : PurchaseViewModel by viewModels()

    private lateinit var rewardButton1 : Button
    private lateinit var rewardButton2 : Button
    private lateinit var rewardButton3 : Button

    private lateinit var reward1Price : TextView
    private lateinit var reward2Price : TextView
    private lateinit var reward3Price : TextView

    private lateinit var robotImage : ImageView
    private lateinit var robotEnergyAvailable : TextView

    private var imageResource = 0
    private var energySpent = 0

    // Keep track of all the rewards purchased in this instance
//    private val listOfPurchases = ArrayList<Int>()

    /*
        Use PurchaseButton Data Class to store relevant information for each purchase button.
            - buttonString = the string that would be displayed on that button (e.g. "Reward F")
            - costString = the string that indicates how much that button costs (e.g. "4")
            - costPrice = the integer that indicates how much that button costs (e.g. 4)

       Note: Strings are stored as Int in the data class because R stores the IDs as integers
     */
    private val rewardButtons = listOf(
        PurchaseButton(R.string.reward_a_button, R.string.one, 1),
        PurchaseButton(R.string.reward_b_button, R.string.two, 2),
        PurchaseButton(R.string.reward_c_button, R.string.three, 3),
        PurchaseButton(R.string.reward_d_button, R.string.three, 3),
        PurchaseButton(R.string.reward_e_button, R.string.four, 4),
        PurchaseButton(R.string.reward_f_button, R.string.four, 4),
        PurchaseButton(R.string.reward_g_button, R.string.seven, 7)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_robot_purchase)

        // clear the array of purchases
        //listOfPurchases.clear()
//
//        Toast.makeText(this, "LOP: ${ArrayList(purchaseViewModel.purchases).size}", Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, "ES: ${purchaseViewModel.energySpent}", Toast.LENGTH_SHORT).show()
//        Toast.makeText(this, "DB: ${purchaseViewModel.disabledButtons.size}", Toast.LENGTH_SHORT).show()


        rewardButton1 = findViewById(R.id.buy_reward_1)
        rewardButton2 = findViewById(R.id.buy_reward_2)
        rewardButton3 = findViewById(R.id.buy_reward_3)

        reward1Price = findViewById(R.id.oneTextView)
        reward2Price = findViewById(R.id.twoTextView)
        reward3Price = findViewById(R.id.threeTextView)

        robotImage = findViewById(R.id.robot_image)
        robotEnergyAvailable = findViewById(R.id.robot_energy_to_spend)

        // set energySpent to zero each time RobotPurchase Activity is created, want to track spent in this session.
        energySpent = purchaseViewModel.energySpent

        // get the extra energy passed from main and update the energy display
        if (purchaseViewModel.energy == 0) {
            purchaseViewModel.energy = intent.getIntExtra(EXTRA_ROBOT_ENERGY, 0)

            /* Make 3 Random Buttons (From A - G) ******************************************************************************************************
                The idea is to find 3 unique indices of the rewardsButton list (which is created in alphabetical order)
                Keep pulling random integers from 0 - 6 (size of rewardsButton list) until len of uniqueIndices = 3
                uniqueIndices is a set (unique cuz hashed).
             */
            val random = Random()

            // Initialize a set to store unique random indices
            val uniqueIndices = mutableSetOf<Int>()

            // Choose three unique random strings ( So we don't repeat buttons on the purchase screen)
            while (uniqueIndices.size < 3) {
                val randomIndex = random.nextInt(rewardButtons.size)
                uniqueIndices.add(randomIndex)
            }

            // Sort them in order so we can sort buttons in order alphabetically
            val sortedIndices = uniqueIndices.sorted()
            purchaseViewModel.updateIdxList(0, sortedIndices[0])
            purchaseViewModel.updateIdxList(1, sortedIndices[1])
            purchaseViewModel.updateIdxList(2, sortedIndices[2])
            // ****************************************************************************************************************************************

            val disabledButtons = intent.getStringArrayListExtra(EXTRA_DISABLED_BUTTONS) ?: mutableListOf<String>()
            for(buttonId in disabledButtons) { purchaseViewModel.addDisabledButton(buttonId) }
        }

        robotEnergyAvailable.text = purchaseViewModel.energy.toString()

        // Set the text for each of the random 3 buttons (e.g "Reward B")
        rewardButton1.text = getString(rewardButtons[purchaseViewModel.uniqueIdxList[0]].buttonString)
        rewardButton2.text = getString(rewardButtons[purchaseViewModel.uniqueIdxList[1]].buttonString)
        rewardButton3.text = getString(rewardButtons[purchaseViewModel.uniqueIdxList[2]].buttonString)

        // Set the text for the cost of each of random 3 buttons (e.g. "4")
        reward1Price.text = getString(rewardButtons[purchaseViewModel.uniqueIdxList[0]].costString)
        reward2Price.text = getString(rewardButtons[purchaseViewModel.uniqueIdxList[1]].costString)
        reward3Price.text = getString(rewardButtons[purchaseViewModel.uniqueIdxList[2]].costString)

        // Disable buttons if they have been disable before rotating
        if (purchaseViewModel.isDisabled(rewardButton1.text.toString())) { rewardButton1.isEnabled = false }
        if (purchaseViewModel.isDisabled(rewardButton2.text.toString())) { rewardButton2.isEnabled = false }
        if (purchaseViewModel.isDisabled(rewardButton3.text.toString())) { rewardButton3.isEnabled = false }


        /*
            Set onClickListener for each of the 3 random buttons.
            The cost price can be found from the PurchaseButton data class.
            Pass the reward button of type Button to makePurchase() fxn to disable if purchased.
         */
        rewardButton1.setOnClickListener { _ : View ->
            makePurchase(rewardButtons[purchaseViewModel.uniqueIdxList[0]].costPrice, rewardButton1)
        }
        rewardButton2.setOnClickListener { _ : View ->
            makePurchase(rewardButtons[purchaseViewModel.uniqueIdxList[1]].costPrice, rewardButton2)
        }
        rewardButton3.setOnClickListener { _ : View ->
            makePurchase(rewardButtons[purchaseViewModel.uniqueIdxList[2]].costPrice, rewardButton3)
        }




        // get the extra imageResource (which image should be set) from main and update the imageView
        imageResource = intent.getIntExtra(EXTRA_ROBOT_IMAGE_RESOURCE, 0)
        robotImage.setImageResource(imageResource)

        setItemPurchased(ArrayList(purchaseViewModel.purchases), purchaseViewModel.energySpent, purchaseViewModel.disabledButtons)
    }

    /*
        ENCAPSULATION: no reason for MainActivity to know the implementation details of what RobotPurchase Activity
                        expects as extras on its Intent (the amount the robot has & the image resource)

        A companion object allows you to access functions w/out having an instance of the class
     */
    companion object {
        fun newIntent(packageContext : Context, robotEnergy : Int, imageResource: Int, disabledButtons: Set<String>) : Intent {
            return Intent(packageContext, NewRobotPurchase::class.java).apply {
                putExtra(EXTRA_ROBOT_ENERGY, robotEnergy)
                putExtra(EXTRA_ROBOT_IMAGE_RESOURCE, imageResource)
                putStringArrayListExtra(EXTRA_DISABLED_BUTTONS, ArrayList(disabledButtons.toList()))
            }
        }
    }


    private fun makePurchase(costOfPurchase: Int, buttonToDisable : Button) {
        /*
            Handle the logic when the current robot wants to make a purchase.
            A robot can only make a purchase if they have enough energy (>= costOfPurchase).
            Make a toast equal depending on how much the current robot spends (set string in when statement).
            Subtract the energy spent from the robotEnergy, and update the energy counter on screen.
            Add the amount spent to energySpent, to keep track of total energy spend (to send back to Main Activity).
            Create an Intent with robotEnergy & energySpent as extras and set the result of the current activity with that Intent

            Disables the button if it is purchased.
        */
        if(purchaseViewModel.energy >= costOfPurchase) {
            val s1 = when (costOfPurchase) {
                1 -> getString(R.string.reward_1_purchase)
                2 -> getString(R.string.reward_2_purchase)
                3 -> getString(R.string.reward_3_purchase)
                4 -> getString(R.string.reward_4_purchase)
                7 -> getString(R.string.reward_7_purchase)
                else -> getString(R.string.error_reward)
            }

            Toast.makeText(this, s1, Toast.LENGTH_SHORT).show()

            purchaseViewModel.makePurchase(costOfPurchase)
            robotEnergyAvailable.text = purchaseViewModel.energy.toString()

            purchaseViewModel.addPurchaseToList(costOfPurchase)
            buttonToDisable.isEnabled = false
            purchaseViewModel.addDisabledButton(buttonToDisable.text.toString())

            setItemPurchased(ArrayList(purchaseViewModel.purchases), purchaseViewModel.energySpent, purchaseViewModel.disabledButtons)

        } else {
            Toast.makeText(this, R.string.insufficient, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setItemPurchased(listOfPurchases : ArrayList<Int>, robotEnergySpent : Int, disabledButtons : Set<String>) {
        /*
            Responsible for creating an Intent with extras that represent the purchase made and energy spent.
            Setting the result of the current activity (RobotPurchase) with the Intent to send data back to the calling activity (Main Activity).
            - Send the most recent purchase made, to raise a toast in MainActivity
            - Send total energy spent by current robot this session, to update that robot's energy total
        */

        val data = Intent().apply{
            putExtra(EXTRA_ROBOT_ITEM_PURCHASED, listOfPurchases.toIntArray())
            putExtra(EXTRA_ROBOT_ENERGY_SPENT, robotEnergySpent)
            putStringArrayListExtra(EXTRA_DISABLED_BUTTONS, ArrayList(disabledButtons.toList()))
        }
        setResult(Activity.RESULT_OK, data)
    }
}