package com.example.handyshop.ui

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.handyshop.R
import com.example.handyshop.adapter.UserBookListAdapter
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.databinding.FragmentUserProfilBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class UserProfilFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentUserProfilBinding
    lateinit var mySharedPreferences: SharedPreferences
    private val api = APIClient.getInstance().create(APIService::class.java)
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
    ): View? {

        binding = FragmentUserProfilBinding.inflate(layoutInflater)

        binding.profileSaqlanganKitoblarRecycler.adapter = UserBookListAdapter(requireContext())



        binding.profileBackToHome.setOnClickListener {

            findNavController().navigate(R.id.homeScreenFragment)

        }

//        binding.inReadingView.setOnClickListener {
//
//            findNavController().navigate()
//
//        }
//        binding.outReadingView.setOnClickListener {
//            parentFragmentManager.beginTransaction().replace(R.id.main, OutReadingFragment()).addToBackStack("Profile").commit()
//        }
        binding.savedView.setOnClickListener {
            findNavController().navigate(R.id.savedBooksFragment)
        }

        return inflater.inflate(R.layout.fragment_user_profil, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserProfilFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}