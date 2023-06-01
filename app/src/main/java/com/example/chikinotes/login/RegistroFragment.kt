package com.example.chikinotes.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.chikinotes.R
import com.example.chikinotes.databinding.FragmentRegistroBinding
import com.example.chikinotes.Util.makeLink
import com.example.chikinotes.Util.showToast
import com.google.firebase.auth.FirebaseAuth

class RegistroFragment : Fragment() {
    private var _binding : FragmentRegistroBinding? = null
    private val binding get() = _binding!!
    private var esperando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.registroLogin.makeLink({
            if (!esperando)
                Navigation.findNavController(view).navigate(R.id.action_registroFragment_to_loginFragment)
        },"¡Inicia sesión!")
        binding.registroButton.setOnClickListener{
            confirmarRegistro(view)
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
            binding.registroCargando.visibility = View.VISIBLE
            binding.registroButton.visibility = View.GONE
        } else {
            binding.registroCargando.visibility = View.GONE
            binding.registroButton.visibility = View.VISIBLE
        }
    }
    
    private fun confirmarRegistro(view: View){
        val correo: String = binding.registroCorreo.text.toString()
        val clave: String = binding.registroClave.text.toString()
        val confirmarClave: String = binding.registroConfirmarClave.text.toString()
        //Validar integridad de datos
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.registroCorreo.error = "Correo no valido"
            return
        } else if (clave.length < 6) {
            binding.registroClave.error = "Contraseña muy corta"
            return
        } else if (clave != confirmarClave) {
            binding.registroConfirmarClave.error = "Contraseñas no coinciden"
            return
        }

        //Validar en base de datos
        cargando(true)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(correo, clave).addOnCompleteListener(requireActivity()) { task ->
            cargando(false)
            if (task.isSuccessful) {
                activity?.showToast("¡Cuenta creada con éxito! Compruebe su bandeja de spam para verificar", Toast.LENGTH_LONG)
                firebaseAuth.currentUser!!.sendEmailVerification()
                firebaseAuth.signOut()
                Navigation.findNavController(view).navigate(R.id.action_registroFragment_to_loginFragment)

            } else {
                activity?.showToast(task.exception!!.localizedMessage)
            }
        }
    }
}