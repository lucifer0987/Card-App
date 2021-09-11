package com.example.jm.memes.data.api

import com.example.jm.jokes.data.model.JokesModel
import retrofit2.Response
import retrofit2.http.GET

interface UserApi {

    @GET("users")
    suspend fun getUserData(): Response<JokesModel>

}