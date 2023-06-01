package com.example.chikinotes.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.chikinotes.databinding.FragmentCreditosBinding

class CreditosFragment : Fragment() {
    private var _binding : FragmentCreditosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreditosBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.creditosRegresar.setOnClickListener{
            Navigation.findNavController(view).popBackStack()
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}