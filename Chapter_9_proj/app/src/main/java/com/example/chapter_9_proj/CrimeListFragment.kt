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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chapter_9_proj.databinding.FragmentCrimeDetailBinding
import com.example.chapter_9_proj.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    val crimeListViewModel : CrimeListViewModel by viewModels()
    private var _binding : FragmentCrimeListBinding? = null

    private val binding
        get() = checkNotNull(_binding){
            "Error: can we see this"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(layoutInflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch{

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                /* repeatOnLifeCycle is a suspending fxn.
                   can execute your code while it is in a specific LifeCycle state ( we want to execute it here in onViewCreated )
                   collect crimes from flow here
                 */

                // Lambda FXN to COLLECT CRIMES FROM FLOW AND UPDATE UI
                crimeListViewModel.crimes.collect{crimes ->
                    binding.crimeRecyclerView.adapter = CrimeListAdapter(crimes) { crimeId ->
                        findNavController().navigate(
                            // id for where we are going -- R.id.show_crime_detail
                            CrimeListFragmentDirections.showCrimeDetail(crimeId)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // TODO
    }

}