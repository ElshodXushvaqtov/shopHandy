package com.example.handyshop.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Book
import com.example.handyshop.data.CategoryData
import com.example.handyshop.databinding.FragmentHomeScreenBinding
import com.example.handyshop.preference.SharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeScreenFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var selectedBooks: MutableList<Book>
    lateinit var mySharedPreferences: SharedPreference
    lateinit var currentcategory: String
    lateinit var binding: FragmentHomeScreenBinding
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
        binding = FragmentHomeScreenBinding.inflate(layoutInflater)
        mySharedPreferences = SharedPreference.newInstance(requireContext())
        selectedBooks = mySharedPreferences.GetSelectedBooks()
        var books = mutableListOf<Book>()
        val api = APIClient.getInstance().create(APIService::class.java)
        val categories = mutableListOf<CategoryData>()
        currentcategory = ""

        api.getMainBook().enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                binding.homeMainBookText.setText(response.body()!!.author + "ning " + response.body()!!.name + " asari")
                binding.homeMainBookImage.load(response.body()!!.image)
                binding.homeMainBookReadNowMb.setOnClickListener {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_bookInfoFragment)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


//    fun drawerListener() {
//        binding.apply {
//            navigationView.setNavigationItemSelectedListener {
//                when (it.itemId) {
//                    R.id.menu_profile -> {
//                        parentFragmentManager.beginTransaction().replace(R.id.main_frame, ProfileFragment()).commit()
//                    }
//                    R.id.menu_home-> {
//                        parentFragmentManager.beginTransaction().replace(R.id.main_frame, HomeFragment()).commit()
//                    }
//                    R.id.menu_saved-> {
//                        parentFragmentManager.beginTransaction().replace(R.id.main_frame, SavedBooksFragment()).commit()
//                    }
//                    R.id.menu_logout -> {
//                        val shared = SharedPreference.newInstance(requireContext())
//                        shared.setLoginData(mutableListOf())
//                        parentFragmentManager.beginTransaction().replace(R.id.main_frame, SignInFragment()).commit()
//                    }
//                }
//                drawer.closeDrawer(GravityCompat.START)
//                true
//            }
//        }
//    }
}