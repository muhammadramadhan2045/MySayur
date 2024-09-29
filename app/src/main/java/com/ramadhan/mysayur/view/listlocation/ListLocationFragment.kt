package com.ramadhan.mysayur.view.listlocation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramadhan.mysayur.core.ui.adapter.LocationAdapter
import com.ramadhan.mysayur.databinding.FragmentListLocationBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListLocationFragment : Fragment() {


    private var _binding: FragmentListLocationBinding? = null
    private val binding get() = _binding!!

    private val listLocViewModel: ListLocationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListLocationBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locations = listLocViewModel.getAllLocations()

        val locationAdapter = LocationAdapter()


        binding.rvLocation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = locationAdapter
        }
        locationAdapter.submitList(locations)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}