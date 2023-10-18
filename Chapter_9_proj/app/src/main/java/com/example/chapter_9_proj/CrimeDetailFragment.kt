package com.example.chapter_9_proj

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.chapter_9_proj.databinding.FragmentCrimeDetailBinding
import java.util.Date
import java.util.UUID

class CrimeDetailFragment : Fragment() {
    // back binding?
    private var _binding : FragmentCrimeDetailBinding? = null
    private lateinit var crime : Crime

    private val binding : FragmentCrimeDetailBinding
        get() = checkNotNull(_binding) {
            "ERROR: Can we see the view"
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // we do NOT call setContentView(R.layout.activity______)
        crime = Crime(
            id = UUID.randomUUID(),
            title ="Bad Stuff Crime",
            date = Date(),
            isSolved = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeDetailBinding.inflate(layoutInflater, container, false) /// left off here
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply { // all binding/wireUps go here
            crimeTitle.doOnTextChanged{ text, _, _, _ ->
                crime = crime.copy(title = text.toString())
                //crime.title = text.toString()
            }

//            crimeDate.setText(crime.title)
//            crimeDate.setOnClickListener{ view :View ->
//                crimeDate.setText(crime.title)
//            }

            crimeDate.apply{
                text = crime.date.toString()
                crimeDate.isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener{ _, isChecked ->
                crime = crime.copy(isSolved = isChecked)
            }
        }
    }
}