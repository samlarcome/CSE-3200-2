package com.example.fragmentcrimech9

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.fragmentcrimech9.databinding.FragmentCrimeDetailBinding
import java.util.Date
import java.util.UUID

class CrimeFragmentDetail : Fragment() {
    private lateinit var binding : FragmentCrimeDetailBinding
    private lateinit var crime : Crime

    // EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // we do not call setContentView() <--- would get put in onViewCreate
        crime = Crime(
            id = UUID.randomUUID(),
            title ="Bad stuff crime",
            date = Date(),
            isSolved = false
        )
    }

    // JUST INFLATE THE VIEW
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
        // LEFT OFF HERE
        binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    // MOST OF OUR LOGIC GOES
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            crimeTitle.doOnTextChanged { text, _ , _ , _ ->
                // take the crime object and copy all data from it
                crime = crime.copy(title = text.toString() )

                //crime.title = text.toString()
            }

//            crimeDate.text = crime.title
//            crimeDate.setOnClickListener {_ : View ->
//                //crimeDate.text = crime.title
//                crimeDate.text = crime.isSolved.toString()
//            }

            crimeDate.apply {
                text = crime.date.toString()
                crimeDate.isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener{ _ , isChecked ->
                crime = crime.copy(isSolved = isChecked)
            }
        }
    }

}