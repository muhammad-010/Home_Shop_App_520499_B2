package com.example.uasolshop.network

import com.example.uasolshop.dataclass.Products
import com.example.uasolshop.dataclass.Users
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("users")
    fun createUser(@Body user: Users): Call<Users>
    @GET("users")
    fun getAllUsers(): Call<List<Users>>
    @GET("produkDB")
    fun getAllProducts(): Call<List<Products>>

    @POST("produkDB")
    fun uploadProduk(@Body rawJson: RequestBody): Call<Products>

    @POST("produkDB/{id}")
    fun updateProduk(@Path("id") bookId:String, @Body rawJson: RequestBody): Call<Products>

    @DELETE("produkDB/{id}")
    fun deleteProduct(
        @Path("id") id: String
    ):Call<Void>
//            Call<Void>
//    Call<Products>
//    @PUT("produkGB/{id}")
//    fun decreaseStock(
//        @Path("id") id: String
//    ): Call<Void>
}