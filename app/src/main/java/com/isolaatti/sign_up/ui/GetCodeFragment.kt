package com.isolaatti.sign_up.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isolaatti.databinding.FragmentGetCodeBinding

class GetCodeFragment : Fragment() {

    private lateinit var binding: FragmentGetCodeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetCodeBinding.inflate(inflater)
        return binding.root
    }
}