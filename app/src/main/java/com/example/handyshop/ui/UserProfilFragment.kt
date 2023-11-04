package com.example.handyshop.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.handyshop.R
import com.example.handyshop.adapter.CustomBooksList
import com.example.handyshop.adapter.SavedAdapter
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Book
import com.example.handyshop.databinding.FragmentUserProfilBinding
import com.example.handyshop.preference.SharedPreference

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class UserProfilFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentUserProfilBinding
    private lateinit var mySharedPreferences: SharedPreference
    private lateinit var selectedBooks: MutableList<Book>
    private lateinit var finishedBooks: MutableList<Book>
    private lateinit var inProgressBooks: MutableList<Book>
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
    ): View {

        binding = FragmentUserProfilBinding.inflate(layoutInflater)


        mySharedPreferences = SharedPreference.newInstance(requireContext())
        inProgressBooks = mySharedPreferences.getInProgressBook()
        finishedBooks = mySharedPreferences.getFinishedBook()
        selectedBooks = mySharedPreferences.GetSelectedBooks()

        binding.profileSaqlanganKitoblarRecycler.adapter =
            SavedAdapter(requireContext(), object : SavedAdapter.OnClicked {
                override fun onClicked(id: Int) {
                    val bundle = bundleOf("book" to id)
                    findNavController().navigate(R.id.bookInfoFragment, bundle)
                }
            })

        binding.inReadingCount.text = inProgressBooks.size.toString()
        binding.outReadingCount.text = finishedBooks.size.toString()
        binding.savedReadingCount.text = selectedBooks.size.toString()

        binding.profileOqilayotganKitoblarRecycler.adapter =
            CustomBooksList(inProgressBooks, object : CustomBooksList.OnClick {
                override fun onClick(book: Book) {
                    val bundle = bundleOf("book" to id)
                    findNavController().navigate(R.id.bookInfoFragment, bundle)
                }
            })

        binding.profileOqilganKitoblarRecycler.adapter =
            CustomBooksList(finishedBooks, object : CustomBooksList.OnClick {
                override fun onClick(book: Book) {
                    val bundle = bundleOf("book" to id)
                    findNavController().navigate(R.id.bookInfoFragment, bundle)
                }

            })

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

        return binding.root
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