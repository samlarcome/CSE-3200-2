package com.example.chapter_9_proj

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.chapter_9_proj.databinding.FragmentCrimeDetailBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID

//private const val TAG = "crimeDetailFragment"

class CrimeDetailFragment : Fragment() {

    private var _binding : FragmentCrimeDetailBinding? = null
    private val binding : FragmentCrimeDetailBinding
        get() = checkNotNull(_binding) {
            "ERROR: Can we see the view"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels{
        CrimeDetailViewModelFactory(args.crimeId)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
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
                //crime = crime.copy(title = text.toString())
                crimeDetailViewModel.updateCrime{oldCrime ->
                    oldCrime.copy(title = text.toString())
                }
            }

            crimeDate.apply{
                // text = crime.date.toString()
                crimeDate.isEnabled = false
            }

            crimeSolved.setOnCheckedChangeListener{ _, isChecked ->
                //crime = crime.copy(isSolved = isChecked)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    crimeDetailViewModel.crime.collect{crime->
                        crime?.let {
                            updateUi(it)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(crime: Crime) {
        binding.apply {
            if(crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            crimeDate.text = crime.date.toString()
            crimeSolved.isChecked = crime.isSolved
        }
    }

    private fun parseContactSelection(contractUri: Uri) {

    }
}