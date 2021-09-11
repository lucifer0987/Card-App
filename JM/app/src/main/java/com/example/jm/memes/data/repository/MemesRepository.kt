package com.example.jm.memes.data.repository

import com.example.jm.jokes.data.api.JokesService
import com.example.jm.memes.data.api.MemesService

class MemesRepository(private val memesService: MemesService) {

    suspend fun getMemesData() = memesService.getMemesService().getMemes("https://meme-api.herokuapp.com/gimme/")

}