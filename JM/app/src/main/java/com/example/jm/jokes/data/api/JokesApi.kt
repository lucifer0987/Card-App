package com.example.jm.jokes.data.api

import com.example.jm.jokes.data.model.JokesModel
import retrofit2.Response
import retrofit2.http.GET

interface JokesApi {

    @GET("users")
    suspend fun getUserData(): Response<JokesModel>

}