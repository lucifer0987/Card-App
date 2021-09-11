package com.example.jm.memes.data.api

import com.example.jm.memes.data.model.MemesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface MemesApi {

    @GET
    suspend fun getMemes(@Url url:String): Response<MemesModel>

}