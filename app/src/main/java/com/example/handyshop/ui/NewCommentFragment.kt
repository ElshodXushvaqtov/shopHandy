package com.example.handyshop.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.CommentData
import com.example.handyshop.data.CommentDataOrigin
import com.example.handyshop.databinding.FragmentNewCommentBinding
import com.example.handyshop.preference.SharedPreference
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NewCommentFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentNewCommentBinding
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

        binding = FragmentNewCommentBinding.inflate(layoutInflater)

        var api = APIClient.getInstance().create(APIService::class.java)
        val shared = SharedPreference.newInstance(requireContext())
        val user = shared.getLoginData()

        binding.jonatish.setOnClickListener {
            Log.d("AASD", arguments?.getInt("newComment_id").toString())
            if (arguments?.containsKey("newComment_id") == true) {

                var commentData = CommentDataOrigin(
                    book_id = requireArguments().getInt("newComment_id"),
                    reyting = binding.ratingBar.rating.toInt(),
                    text = binding.commentsss.text.toString(),
                    user_id = user[0].id
                )


                api.giveCommentToTheBook(commentData).enqueue(object : Callback<CommentData> {
                    override fun onResponse(
                        call: Call<CommentData>,
                        response: Response<CommentData>
                    ) {
                        Log.d("TAG6", "onResponse: ${response.body()}")
                        Toast.makeText(requireContext(), "sent", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.commentFragment)
                    }

                    override fun onFailure(call: Call<CommentData>, t: Throwable) {
                        Log.d("TAG", "onFailure: $t")
                    }

                })
            }
        }




        return inflater.inflate(R.layout.fragment_new_comment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewCommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}