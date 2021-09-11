package com.example.jm.jokes.data.repository

import com.example.jm.jokes.data.api.JokesService

class JokesRepository(private val jokesService: JokesService) {

    suspend fun getUserData() = jokesService.getUserService().getUserData()

}