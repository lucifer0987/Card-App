package com.example.jm.memes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jm.memes.data.api.MemesService
import com.example.jm.memes.data.model.MemesModel
import com.example.jm.memes.data.repository.MemesRepository
import com.example.jm.network.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemesViewModel: ViewModel() {

    private val repository: MemesRepository = MemesRepository(MemesService(RetrofitBuilder))

    private var _memesLiveData = MutableLiveData<MemesModel>()

    val memesLiveData: LiveData<MemesModel>
        get() = _memesLiveData


    fun fetchData(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getMemesData()
            if(response.isSuccessful){
                _memesLiveData.postValue(response.body())
            }
        }

    }


}