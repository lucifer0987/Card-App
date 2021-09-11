package com.example.jm.memes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.jm.jokes.viewmodel.JokesViewModel
import com.example.jm.databinding.FragmentMemesBinding
import com.example.jm.memes.viewmodel.MemesViewModel

class MemesFragment : Fragment() {

    lateinit var binding: FragmentMemesBinding
    val memesViewModel: MemesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMemesBinding.inflate(layoutInflater)
        return binding.root
    }

    //we need to add this, it means all the views has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        memesViewModel.fetchData()

        memesViewModel.memesLiveData.observe(viewLifecycleOwner, Observer {
            with(binding){
                memeTitle.text = it.title
                activity?.let { it1 -> Glide.with(it1).load(it.preview.get(it.preview.size - 1)).into(memeImage) }
            }
        })

        binding.refresh.setOnClickListener{
            memesViewModel.fetchData()
        }

    }

}