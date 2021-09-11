package com.example.jm.jokes.data.api

import com.example.jm.jokes.data.model.JokesModel
import com.example.jm.memes.data.model.MemesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface JokesApi {

    @Headers("Accept: application/json")
    @GET
    suspend fun getJokes(@Url url:String): Response<JokesModel>

}