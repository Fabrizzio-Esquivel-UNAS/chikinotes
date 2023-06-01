package com.example.chikinotes.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chikinotes.R
import com.example.chikinotes.Util.makeLink
import com.example.chikinotes.Util.showToast
import com.example.chikinotes.databinding.FragmentRecuperarBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarFragment : Fragment() {
    private var _binding: FragmentRecuperarBinding? = null
    private val binding get() = _binding!!
    private var esperando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecuperarBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.recuperarRegistrar.makeLink({
            if (!esperando)
                Navigation.findNavController(view).navigate(R.id.action_recuperarFragment_to_registroFragment)
        }, "¡Registrate ahora!")
        binding.recuperarButton.setOnClickListener{
            enviarRecuperacion(view)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargando(enEspera: Boolean) {
        esperando = enEspera
        if (enEspera) {
            binding.recuperarCargando.visibility = View.VISIBLE
            binding.recuperarButton.visibility = View.GONE
        } else {
            binding.recuperarCargando.visibility = View.GONE
            binding.recuperarButton.visibility = View.VISIBLE
        }
    }

    private fun enviarRecuperacion(view: View) {
        val correo = binding.recuperarCorreo.text.toString()
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    activity?.showToast("Correo de restauración de contraseña enviado")
                    Navigation.findNavController(view).navigate(R.id.action_recuperarFragment_to_loginFragment)
                } else {
                    activity?.showToast(task.exception!!.localizedMessage)
                }
            }
    }
}