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

class MainActivity : AppCompatActivity() {

    private lateinit var redRobotImage: ImageView
    private lateinit var whiteRobotImage: ImageView
    private lateinit var yellowRobotImage: ImageView
    private lateinit var messageBox: TextView
    private lateinit var makePurchase: Button

    private lateinit var robotImages : MutableList<ImageView>

    private val robots = listOf(
        Robot(R.string.red_robot_message, false, R.drawable.king_of_detroit_robot_red_large, R.drawable.king_of_detroit_robot_red_small),
        Robot(R.string.white_robot_message, false, R.drawable.king_of_detroit_robot_white_large, R.drawable.king_of_detroit_robot_white_small),
        Robot(R.string.yellow_robot_message, false, R.drawable.king_of_detroit_robot_yellow_large, R.drawable.king_of_detroit_robot_yellow_small)
    )

    private val robotViewModel : RobotViewModel by viewModels()

    /*
        purchaseLauncher is registered to handle the result of starting an activity. It processes the result when the launched activity (RobotPurchase) finishes.
        Lambda - if the RobotPurchase activity exits ok (hit the back button, don't swipe up), handle the Extra's that are sent back from RobotPurchase.

        - EXTRA_ROBOT_ITEM_PURCHASED = the latest amount purchased by the current robot
        - EXTRA_DISABLED_BUTTONS = a list of buttons (stored as strings) to be marked as bought/disabled
        - EXTRA_ROBOT_ENERGY_SPENT = the total amount spent by current robot while in the RobotPurchase Activity (to update it)
     */
    private val purchaseLauncher = registerForActivityResult(
        /*
            Capture the data/extras of the purchase activity.
                1: Get the list of strings of purchases a robot just made (e.g. A, B, C).
                   Store them in the current robot's list of total purchases (to make a toast later)

                2: Get the list of buttons to disable (I took their text as strings) and
                   add them each into a list of strings in the robotViewModel so no robot
                   can buy them after one has bought them.

                3: Get the total amount of energy the robot just spent in PurchaseActivity.
                   Update how much energy the current robot has in the robotViewModel.
                   Make a toast of all the robot purchases.
         */

        ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {

            // 1
            val listOfPurchases = result.data?.getStringArrayListExtra(EXTRA_ROBOT_ITEM_PURCHASED) ?: mutableListOf<String>()
            for (purchase in listOfPurchases) { robotViewModel.addPurchaseToList(robotViewModel.turnCount - 1, purchase) }

            // 2
            val listOfDisabledButtons = result.data?.getStringArrayListExtra(EXTRA_DISABLED_BUTTONS) ?: mutableListOf<String>()
            for (buttonID in listOfDisabledButtons) { robotViewModel.addDisabledButton(buttonID) }

            // 3
            val totalEnergySpent = result.data?.getIntExtra(EXTRA_ROBOT_ENERGY_SPENT, 0) ?: 0
            robotViewModel.updateEnergy(robotViewModel.turnCount - 1, -1 * (totalEnergySpent))
            makeRobotToast()
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

        redRobotImage.setOnClickListener { _: View -> toggleImage() }
        whiteRobotImage.setOnClickListener { _: View -> toggleImage() }
        yellowRobotImage.setOnClickListener { _: View -> toggleImage() }

        /*
            Set on click event listener for the make purchase button that launches the Robot Purchase Activity
            We should only launch the Activity if any robot has had a turn (when turnCount != 0 ... it is initially 0, but never again after a single turn)
            Conditionally launch an activity using purchaseLauncher. The intent for launching the activity is created using NewRobotPurchase.newIntent (static method)
            NewRobotPurchase.newIntent requires 3 extras
                - the energy of current robot (stored in the view model)
                - the id of largeImageResource for current robot (to set the correct image in new activity)
                - a set of buttons that have been clicked (stored in view model), and therefore should be disabled if chosen in purchaseActivity
         */
        makePurchase.setOnClickListener { _ : View ->
            if ((robotViewModel.turnCount) != 0) {
                val intent = NewRobotPurchase.newIntent(this,
                    robotViewModel.energyList[robotViewModel.turnCount - 1],
                    robots[robotViewModel.turnCount - 1].largeImageResource,
                    robotViewModel.disabledButtons)
                purchaseLauncher.launch(intent)
            }
        }

        // Logic to Set the Correct Robot to Large when MainActivity recreated
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
        robotViewModel.increaseEnergy()
        setRobotImages()
        makeRobotToast()
    }

    private fun updateMessageBox() {
        messageBox.setText(robots[robotViewModel.turnCount - 1].messageResource)
    }

    private fun setRobotTurn() {
        for(robot in robots) { robot.myTurn = false }
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

    private fun makeRobotToast() {
        /*
            When it is a robot's turn, a toast must show a list of all its purchases.

            robotViewModel stores a list with 3 nested lists for each robots purchase history.
                - (e.g. [ [A, B] [C,], [F, D] ] for Red, White, Yellow respectively)

           If current robot has made at least one purchase (i.e. their list isn't empty),
           iterate through their list of purchases, concatenate a string, and make toast.
         */
        if (robotViewModel.purchases[robotViewModel.turnCount - 1].isNotEmpty()) {
            var listPurchaseString = ""
            for (purchase in robotViewModel.purchases[robotViewModel.turnCount - 1]) {
                listPurchaseString += "$purchase, "
            }
            Toast.makeText(this, "Purchase List: $listPurchaseString", Toast.LENGTH_SHORT).show()
        }
    }
}
