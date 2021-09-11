package com.example.jm.memes.data.repository

import com.example.jm.jokes.data.api.JokesService

class UserRepository(private val jokesService: JokesService) {

    suspend fun getUserData() = jokesService.getUserService().getUserData()

}