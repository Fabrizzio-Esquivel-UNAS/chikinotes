package com.example.chikinotes.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.chikinotes.R
import com.example.chikinotes.Util.makeLink
import com.example.chikinotes.databinding.FragmentBienvenidaBinding

class BienvenidaFragment : Fragment() {
    private var _binding : FragmentBienvenidaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBienvenidaBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.bienvenidaLogin.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_bienvenidaFragment_to_loginFragment)
        }
        binding.bienvenidaRegistro.setOnClickListener{
            Navigation.findNavController(view).navigate(R.id.action_bienvenidaFragment_to_registroFragment)
        }
        binding.creadoresText.makeLink({
            Navigation.findNavController(view).navigate(R.id.action_bienvenidaFragment_to_creditosFragment)
        }, "bellísimos creadores!")
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}