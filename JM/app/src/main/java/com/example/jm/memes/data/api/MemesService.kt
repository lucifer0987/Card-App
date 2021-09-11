package com.example.jm.memes.data.api

import com.example.jm.network.RetrofitBuilder
import com.example.jm.jokes.data.api.JokesApi

public class MemesService(private val retrofitBuilder: RetrofitBuilder) {

    fun getMemesService(): MemesApi = retrofitBuilder.retrofit.create(MemesApi::class.java)

}