package com.example.jm.jokes.data.api

import com.example.jm.network.RetrofitBuilder

public class JokesService(private val retrofitBuilder: RetrofitBuilder) {

    fun getJokesService():JokesApi = retrofitBuilder.retrofit.create(JokesApi::class.java)

}