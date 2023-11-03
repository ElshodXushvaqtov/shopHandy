package com.example.handyshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.handyshop.R
import com.example.handyshop.preference.SharedPreference


class UserBookListAdapter(var context: Context): RecyclerView.Adapter<UserBookListAdapter.MyViewHolder>(){
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var rasm = view.findViewById<ImageView>(R.id.custom_book_image)
        var title = view.findViewById<TextView>(R.id.name_home)
        var authir = view.findViewById<TextView>(R.id.name_author_home)
        var saved_ic = view.findViewById<ImageView>(R.id.custom_selected)
        var rating = view.findViewById<TextView>(R.id.rating_custom)
    }

    val mySharedPreferences = SharedPreference.newInstance(context)
    var selectedBooks = mySharedPreferences.GetSelectedBooks()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_poem_category, parent, false))
    }

    override fun getItemCount(): Int {
        return selectedBooks.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = selectedBooks[position]
        holder.rasm.load(a.image)
        holder.authir.text = a.author
        holder.title.text = a.name
        holder.rating.text = a.reyting.toString()
        holder.saved_ic.setOnClickListener {
            selectedBooks.remove(a)
            notifyItemRemoved(position)
            mySharedPreferences.SetSelectedBooks(selectedBooks)
        }
    }


}