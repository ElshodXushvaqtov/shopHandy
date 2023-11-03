package com.example.handyshop.preference

import android.content.Context
import com.example.handyshop.data.Book
import com.example.handyshop.data.UserToken
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreference private constructor(context: Context){
    val sharedPreferences = context.getSharedPreferences("data", 0)
    val edit = sharedPreferences.edit()
    val gson = Gson()

    companion object{
        private var instance:SharedPreference? = null
        fun newInstance(contexT: Context): SharedPreference {
            if (instance == null){
                instance = SharedPreference(contexT)
            }
            return instance!!
        }
    }

    fun setLoginData(mutableList: MutableList<UserToken>){
        edit.putString("Login", gson.toJson(mutableList)).apply()
    }
    fun getLoginData(): MutableList<UserToken>{
        val data: String = sharedPreferences.getString("Login", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<UserToken>>(){}.type
        return gson.fromJson(data, typeToken)
    }


    fun GetSelectedBooks(): MutableList<Book>{
        val data: String = sharedPreferences.getString("Selected", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<Book>>(){}.type
        return gson.fromJson(data, typeToken)
    }
    fun SetSelectedBooks(mutableList: MutableList<Book>){
        edit.putString("Selected", gson.toJson(mutableList)).apply()
    }

    fun getInProgressBook(): MutableList<Book>{
        val data: String = sharedPreferences.getString("InProgress", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<Book>>(){}.type
        return gson.fromJson(data, typeToken)
    }
    fun setInProgressBook(mutableList: MutableList<Book>){
        edit.putString("InProgress", gson.toJson(mutableList)).apply()
    }

    fun getFinishedBook(): MutableList<Book>{
        val data: String = sharedPreferences.getString("Finished", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<Book>>(){}.type
        return gson.fromJson(data, typeToken)
    }
    fun setFinishedBook(mutableList: MutableList<Book>){
        edit.putString("Finished", gson.toJson(mutableList)).apply()
    }

}