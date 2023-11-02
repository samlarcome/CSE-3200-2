package com.example.chapter_9_proj

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chapter_9_proj.databinding.FragmentCrimeDetailBinding
import com.example.chapter_9_proj.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    val crimeListViewModel : CrimeListViewModel by viewModels()
    private var _binding : FragmentCrimeListBinding? = null

    private val binding
        get() = checkNotNull(_binding){
            "Error: can we see this"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Num Crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
            val crimes = crimeListViewModel.crimes
            val adapter = CrimeListAdapter(crimes)
            binding.crimeRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                val crimes = crimeListViewModel.loadCrimes()
                binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // TODO
    }

}