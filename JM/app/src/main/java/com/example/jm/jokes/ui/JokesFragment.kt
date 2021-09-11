package com.example.jm.jokes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.jm.databinding.FragmentJokesBinding
import com.example.jm.databinding.FragmentMemesBinding
import com.example.jm.jokes.viewmodel.JokesViewModel
import com.example.jm.memes.viewmodel.MemesViewModel

class JokesFragment : Fragment() {

    lateinit var binding: FragmentJokesBinding
    val jokesViewModel: JokesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJokesBinding.inflate(layoutInflater)
        return binding.root
    }

    //we need to add this, it means all the views has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jokesViewModel.fetchData()

        jokesViewModel.jokesLiveData.observe(viewLifecycleOwner, Observer {
            with(binding){
                joke.text = it.joke
            }
        })

        binding.refresh.setOnClickListener{
            jokesViewModel.fetchData()
        }


    }

}