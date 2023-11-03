package com.example.handyshop.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Book
import com.example.handyshop.data.CategoryData
import com.example.handyshop.databinding.FragmentHomeScreenBinding
import com.example.handyshop.preference.SharedPreference
import farrukh.remotely.adapter.BookAdapter
import farrukh.remotely.adapter.CategoryAdapter
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

                    val bundle = bundleOf("mainBook" to response.body())

                    findNavController().navigate(R.id.action_homeScreenFragment_to_bookInfoFragment,bundle)
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


        api.getAllCategory().enqueue(object : Callback<List<CategoryData>> {
            override fun onResponse(
                call: Call<List<CategoryData>>,
                response: Response<List<CategoryData>>
            ) {
                for (i in 0 until response.body()!!.size) {
                    categories.add(response.body()!![i])
                }
                if (categories.isNotEmpty()) {
                    val adapter =
                        CategoryAdapter(
                            categories,
                            requireContext(),
                            object : CategoryAdapter.ItemClick {
                                override fun OnItemClick(category: String) {
                                    currentcategory = category
                                    if (!response.body()!!.contains(CategoryData(category))) {
                                        api.getAllBooks().enqueue(object : Callback<List<Book>> {
                                            override fun onResponse(
                                                call: Call<List<Book>>,
                                                response: Response<List<Book>>
                                            ) {
                                                binding.booksRv.visibility = View.VISIBLE
                                                books = response.body()!!.toMutableList()
                                                binding.booksRv.adapter = BookAdapter(
                                                    response.body()!!.toMutableList(),
                                                    object : BookAdapter.ItemClick {
                                                        override fun OnItemClick(book: Book) {
                                                            val bundle = bundleOf("book" to book)
                                                            findNavController().navigate(R.id.action_homeScreenFragment_to_bookInfoFragment, bundle)
                                                        }
                                                    }, object : BookAdapter.OnSelected {
                                                        override fun onSelected(book: Book) {
                                                            if (book in selectedBooks) {
                                                                selectedBooks.remove(book)
                                                            } else {
                                                                selectedBooks.add(book)
                                                            }
                                                            mySharedPreferences.SetSelectedBooks(
                                                                selectedBooks
                                                            )
                                                        }
                                                    })
                                            }

                                            override fun onFailure(
                                                call: Call<List<Book>>,
                                                t: Throwable
                                            ) {
                                                Log.d("AAB", "onFailure: $t")
                                            }

                                        })
                                    } else {
                                        api.getBookByCategory(category)
                                            .enqueue(object : Callback<List<Book>> {
                                                override fun onResponse(
                                                    call: Call<List<Book>>,
                                                    response: Response<List<Book>>
                                                ) {
                                                    if (response.body()?.isNotEmpty()!!) {
                                                        binding.booksRv.visibility = View.VISIBLE

                                                        binding.booksRv.adapter = BookAdapter(
                                                            response.body()!!.toMutableList(),
                                                            object : BookAdapter.ItemClick {
                                                                override fun OnItemClick(book: Book) {
                                                                    val bundle = bundleOf("book" to book)
                                                                    findNavController().navigate(R.id.action_homeScreenFragment_to_bookInfoFragment,
                                                                        bundle)
                                                                }

                                                            }, object : BookAdapter.OnSelected {
                                                                override fun onSelected(book: Book) {
                                                                    if (book in selectedBooks) {
                                                                        selectedBooks.remove(book)
                                                                    } else {
                                                                        selectedBooks.add(book)
                                                                    }
                                                                    mySharedPreferences.SetSelectedBooks(
                                                                        selectedBooks
                                                                    )
                                                                }
                                                            })
                                                    } else {
                                                        binding.booksRv.visibility = View.VISIBLE
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<List<Book>>,
                                                    t: Throwable
                                                ) {
                                                    Log.d("", "onFailure: $t")
                                                }

                                            })
                                    }

                                }
                            })

                    val manager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    binding.category.layoutManager = manager
                    binding.category.adapter = adapter
                }
            }

            //
            override fun onFailure(call: Call<List<CategoryData>>, t: Throwable) {
                Log.d("AAC", "onFailure: $t")
            }
        })

        api.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(
                call: Call<List<Book>>,
                response: Response<List<Book>>
            ) {

                books = response.body()!!.toMutableList()
                var layoutManager =
                    GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)


                val adapter =
                    BookAdapter(response.body()!!.toMutableList(), object : BookAdapter.ItemClick {
                        override fun OnItemClick(book: Book) {
                            Log.d("BOOOK", "${book.name}")
                            val bundle = bundleOf("book" to book)
                            findNavController().navigate(R.id.bookInfoFragment,bundle)
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

                binding.booksRv.visibility = View.VISIBLE

                binding.booksRv.layoutManager = layoutManager
                binding.booksRv.adapter = adapter

            }

            override fun onFailure(
                call: Call<List<Book>>,
                t: Throwable
            ) {
                Log.d("ABA", "onFailure: $t")
            }
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    api.search(newText).enqueue(object : Callback<List<Book>> {
                        override fun onResponse(
                            call: Call<List<Book>>,
                            response: Response<List<Book>>
                        ) {
                            if (response.body()?.isNotEmpty()!!) {
                                binding.booksRv.adapter = BookAdapter(
                                    response.body()!!.toMutableList(),
                                    object : BookAdapter.ItemClick {
                                        override fun OnItemClick(book: Book) {
                                            val bundle = bundleOf("book" to book)
                                            findNavController().navigate(R.id.action_homeScreenFragment_to_bookInfoFragment,bundle)

                                        }

                                    },
                                    object : BookAdapter.OnSelected {
                                        override fun onSelected(book: Book) {
                                            if (book in selectedBooks) {
                                                selectedBooks.remove(book)
                                            } else {
                                                selectedBooks.add(book)
                                            }
                                            mySharedPreferences.SetSelectedBooks(selectedBooks)
                                        }
                                    })
                                binding.booksRv.visibility = View.VISIBLE
                            } else {
                                binding.booksRv.visibility = View.VISIBLE
                            }
                        }

                        override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                            Log.d("SearchTAG", "onFailure: $t")
                        }

                    })
                    return true
                }
                binding.booksRv.visibility = View.VISIBLE
                return false
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


    fun drawerListener() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_profile -> {
                        findNavController().navigate(R.id.userProfilFragment)
                    }

                    R.id.menu_home -> {
                        findNavController().navigate(R.id.homeScreenFragment)
                    }

                    R.id.menu_saved -> {
                        findNavController().navigate(R.id.savedBooksFragment)
                    }

                    R.id.menu_logout -> {
                        val shared = SharedPreference.newInstance(requireContext())
                        shared.setLoginData(mutableListOf())
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }
    }
}