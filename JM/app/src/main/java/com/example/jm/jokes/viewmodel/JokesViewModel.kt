package com.example.jm.jokes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jm.jokes.data.api.JokesService
import com.example.jm.jokes.data.model.JokesModel
import com.example.jm.jokes.data.repository.JokesRepository
import com.example.jm.network.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JokesViewModel: ViewModel() {

    private val repository: JokesRepository = JokesRepository(JokesService(RetrofitBuilder))

    private var _jokesLiveData = MutableLiveData<JokesModel>()

    val jokesLiveData: LiveData<JokesModel>
        get() = _jokesLiveData


    fun fetchData(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getUserData()
            if(response.isSuccessful){
                _jokesLiveData.postValue(response.body())
            }
        }

    }


}