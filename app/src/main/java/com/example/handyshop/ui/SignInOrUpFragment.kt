package com.example.handyshop.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.handyshop.R
import com.example.handyshop.databinding.FragmentSignInOrUpBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SignInOrUpFragment : Fragment() {

    lateinit var binding: FragmentSignInOrUpBinding
    private var param1: String? = null
    private var param2: String? = null

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
        binding = FragmentSignInOrUpBinding.inflate(layoutInflater)

        binding.kirishBtn.setOnClickListener {

            findNavController().navigate(R.id.action_signInOrUpFragment_to_loginFragment)

        }
        binding.royhatdanOtishTxt.setOnClickListener {

            findNavController().navigate(R.id.action_signInOrUpFragment_to_registrationFragment)

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignInOrUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}