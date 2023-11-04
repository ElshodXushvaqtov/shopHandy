package com.example.handyshop.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Book
import com.example.handyshop.databinding.FragmentBookInfoBinding
import com.example.handyshop.preference.SharedPreference
import farrukh.remotely.adapter.BookAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class BookInfoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentBookInfoBinding
    lateinit var selectedBooks: MutableList<Book>
    lateinit var mySharedPreferences: SharedPreference
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

        binding = FragmentBookInfoBinding.inflate(layoutInflater)
        mySharedPreferences = SharedPreference.newInstance(requireContext())
        selectedBooks = mySharedPreferences.GetSelectedBooks()

        val api = APIClient.getInstance().create(APIService::class.java)
        if (arguments?.containsKey("book") == true) {
            api.getBook(arguments?.getInt("book")!!).enqueue(object : Callback<Book> {
                override fun onResponse(call: Call<Book>, response: Response<Book>) {
                    if (response.isSuccessful && response.body() != null)

                        binding.appCompatImageView.load(response.body()!!.image)
                    binding.textView5.text = response.body()!!.name
                    binding.description.text = response.body()!!.description

                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                    Log.d("INFO_ERROR", "onFailure: $t")
                }
            })
        }
        api.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
//                books = response.body()!!.toMutableList()
                var layoutManager = GridLayoutManager(
                    requireContext(), 2,
                    LinearLayoutManager.VERTICAL, false
                )

                val adapter =
                    BookAdapter(response.body()!!.toMutableList(), object : BookAdapter.ItemClick {
                        override fun OnItemClick(id: Int) {

                            val bundle = bundleOf("book" to id)

                            findNavController().navigate(R.id.bookInfoFragment, bundle)

                        }

                    }, object : BookAdapter.OnSelected {
                        override fun onSelected(book: Book) {
                            if (book in selectedBooks) {
                                selectedBooks.remove(book)
                            } else {
                                selectedBooks.add(book)
                            }
                            mySharedPreferences.SetSelectedBooks(selectedBooks)
                        }
                    })

                binding.tavsiyalar.adapter = adapter
                binding.tavsiyalar.layoutManager = layoutManager
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.d("TAG", "onFailure: $t")
            }

        })

        binding.pdfViewBtn.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://handybook.uz/frontend/web/file/701697625957.pdf") )
            startActivity(intent)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}