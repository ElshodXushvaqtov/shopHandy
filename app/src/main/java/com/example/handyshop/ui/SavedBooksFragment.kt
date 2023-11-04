package com.example.handyshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyshop.R
import com.example.handyshop.adapter.SavedAdapter
import com.example.handyshop.databinding.FragmentSavedBooksBinding
import com.example.handyshop.preference.SharedPreference

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SavedBooksFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSavedBooksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedBooksBinding.inflate(inflater, container, false)
        SharedPreference.newInstance(requireContext())

        binding.savedRecycler.adapter =
            SavedAdapter(requireContext(), object : SavedAdapter.OnClicked {
                override fun onClicked(id: Int) {
                    val bundle = bundleOf("book" to id)
                    findNavController().navigate(R.id.bookInfoFragment, bundle)
                }
            })
        binding.savedRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.savedBackToHome.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedBooksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}