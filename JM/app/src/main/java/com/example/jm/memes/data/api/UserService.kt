package com.example.jm.memes.data.api

import com.example.jm.network.RetrofitBuilder
import com.example.jm.jokes.data.api.JokesApi

public class UserService(private val retrofitBuilder: RetrofitBuilder) {

    fun getUserService(): JokesApi = retrofitBuilder.retrofit.create(JokesApi::class.java)

}