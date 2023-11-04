package com.example.handyshop.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Comment
import com.example.handyshop.databinding.FragmentCommentBinding
import farrukh.remotely.adapter.CommentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CommentFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCommentBinding
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

        binding = FragmentCommentBinding.inflate(layoutInflater)

        var api = APIClient.getInstance().create(APIService::class.java)

        if(arguments?.containsKey("comment_id")==true){

        api.getAllComments(requireArguments().getInt("comment_id")).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(
                call: Call<List<Comment>>,
                response: Response<List<Comment>>
            ) {
                var adapter = CommentAdapter(response.body()!!)
                var layoutManager = GridLayoutManager(requireContext(),1,
                    LinearLayoutManager.VERTICAL,false)

                binding.commentsRv.adapter = adapter
                binding.commentsRv.layoutManager = layoutManager
                Log.d("TAG", "onResponse:${response.body()} ")

            }

            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })}

        binding.addNewComment.setOnClickListener {
            val bundle = bundleOf("newComment_id" to arguments?.getInt("comment_id"))

            findNavController().navigate(R.id.newCommentFragment,bundle)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}