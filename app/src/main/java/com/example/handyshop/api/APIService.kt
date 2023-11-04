package com.example.handyshop.api
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import com.example.handyshop.data.Book
import com.example.handyshop.data.CategoryData
import com.example.handyshop.data.Comment
import com.example.handyshop.data.CommentData
import com.example.handyshop.data.CommentDataOrigin
import com.example.handyshop.data.Login
import com.example.handyshop.data.User
import com.example.handyshop.data.UserToken

interface APIService {

    @GET("/book-api")
    fun getAllBooks(): Call<List<Book>>

    @POST("/book-api/login")
    fun login(@Body login: Login): Call<UserToken>

    @GET("/book-api/view")
    fun getBook(@Query("id") id: Int): Call<Book>

    @POST("/book-api/register")
    fun register(@Body user: User): Call<UserToken>

    @GET("/book-api/all-category")
    fun getAllCategory():Call<List<CategoryData>>

    @GET("/book-api/category")
    fun getBookByCategory(@Query("name")name: String):Call<List<Book>>

    @GET("/book-api/search-name")
    fun search(@Query("name") name: String):Call<List<Book>>

    @GET("/book-api/main-book")
    fun getMainBook():Call<Book>

    @GET("/book-api/comment")
    fun getAllComments(@Query("id") id: Int):Call<List<Comment>>


    @POST("/comment-api/create")
    fun giveCommentToTheBook(@Body commentData: CommentDataOrigin):Call<CommentData>


}