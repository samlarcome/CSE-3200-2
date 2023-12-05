package com.example.chapter_9_proj

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.Calendar
import java.util.GregorianCalendar

class DatePickerFragment: DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }

    // Dialog itself is responsible for displaying itself
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener = DatePickerDialog.OnDateSetListener {
            _ : DatePicker, year: Int, month: Int, day: Int ->
                val resultDate = GregorianCalendar(year, month, day).time
                setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
        }
        val calendar = Calendar.getInstance()
        calendar.time = args.crimeDate
        val initalYear = calendar.get(Calendar.YEAR)
        val initalMonth = calendar.get(Calendar.MONTH)
        val initalDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initalYear,
            initalMonth,
            initalDay
        )
    }
}