package com.example.handyshop.ui

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.handyshop.R
import com.example.handyshop.api.APIClient
import com.example.handyshop.api.APIService
import com.example.handyshop.data.Book
import com.example.handyshop.databinding.FragmentMediaBinding
import retrofit2.Call
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MediaFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentMediaBinding
    lateinit var runnable: Runnable
    private var handler = Handler()
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

        binding = FragmentMediaBinding.inflate(layoutInflater)

        val api = APIClient.getInstance().create(APIService::class.java)

        if (arguments?.containsKey("book") == true) {

            api.getBook(arguments?.getInt("id")!!).enqueue(object : retrofit2.Callback<Book> {
                override fun onResponse(call: Call<Book>, response: Response<Book>) {
                    if (response.isSuccessful && response.body() != null)
                        binding.avatar.load(response.body()?.image) {
                            transformations(
                                CircleCropTransformation()
                            )
                        }
                    binding.name.text = response.body()?.name
                    binding.author.text = response.body()?.author
                    binding.rating.text = response.body()?.reyting.toString()
                    val mp = MediaPlayer.create(
                        requireContext(),
                        Uri.parse(response.body()!!.audio.toString())
                    )
                    binding.seekbar.progress = 0
                    binding.seekbar.max = mp.duration
                    Log.d("MED", "onResponse: ${mp.duration}")
                    binding.play.setOnClickListener {
                        if (!mp.isPlaying) {
                            mp.start()
                            binding.play.setImageResource(R.drawable.baseline_pause_24)
                        } else {
                            mp.pause()
                            binding.play.setImageResource(R.drawable.baseline_play_arrow_24)
                        }
                    }

                    binding.seekbar.setOnSeekBarChangeListener(object :
                        SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(
                            seekBar: SeekBar?,
                            progress: Int,
                            fromUser: Boolean
                        ) {
                            if (mp != null && fromUser) {
                                mp.seekTo(progress)
                                Log.d("TAG", progress.toString())
                            }
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {
                            TODO("Not yet implemented")
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            TODO("Not yet implemented")
                        }
                    })

                    runnable = Runnable {
                        binding.seekbar.progress = mp.currentPosition
                        handler.postDelayed(runnable, 1000)
                    }
                    handler.postDelayed(runnable, 1000)

                    mp.setOnCompletionListener {
                        binding.play.setImageResource(R.drawable.baseline_play_arrow_24)
                        binding.seekbar.progress = 0
                    }
                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MediaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}