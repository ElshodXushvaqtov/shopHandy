package com.example.handyshop.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Login
import com.example.handyshop.data.UserToken
import com.example.handyshop.databinding.FragmentLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
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
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.kirishBtn.setOnClickListener {

            val api = APIClient.getInstance().create(APIService::class.java)

            val login =
                Login(binding.emailKirish.text.toString(), binding.parolKirish.text.toString())

            api.login(login).enqueue(object : Callback<UserToken> {
                override fun onResponse(call: Call<UserToken>, response: Response<UserToken>) {
                    val bundle = bundleOf("user" to response.body()!!.username)
                    findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment,bundle)
                }

                override fun onFailure(call: Call<UserToken>, t: Throwable) {
                    Log.d("BBB", "onFailure: $t")
                }
            })

        }

        binding.royhatdanOtishKirish.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)

        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}