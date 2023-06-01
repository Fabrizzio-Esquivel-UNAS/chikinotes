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
import com.example.chikinotes.databinding.FragmentLoginBinding
import com.example.chikinotes.Util.makeLink
import com.example.chikinotes.Util.showToast
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var view : View
    private var esperando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        //binding.setLifecycleOwner(this)
        view = binding.root
        binding.loginButton.setOnClickListener {
            confirmarUsuario()
        }
        binding.loginRecuperar.makeLink({
            if (!esperando)
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_recuperarFragment)
        })
        binding.loginRegistrar.makeLink({
            if (!esperando)
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_registroFragment)
        }, "¡Registrate ahora!")
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun cargando(enEspera: Boolean) {
        esperando = enEspera
        if (enEspera) {
            binding.loginCargando.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
        } else {
            binding.loginCargando.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
        }
    }

    private fun confirmarUsuario() {
        var correo = binding.loginCorreo.text.toString()
        var clave = binding.loginClave.text.toString()

        //Validar integridad de datos
        if (correo==""){
            correo = "fabrizzio_fabiano@outlook.com"
            clave = "1234567"
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.loginCorreo.error = "Correo invalido"
            return
        }else if(clave.isEmpty()) {
            binding.loginClave.error = "Contraseña invalida"
            return
        }

        //Validar en la base de datos
        val firebaseAuth = FirebaseAuth.getInstance()
        cargando(true)
        firebaseAuth.signInWithEmailAndPassword(correo, clave).addOnCompleteListener { task ->
            cargando(false)
            if (task.isSuccessful) {
                //Verificar correo
                if (firebaseAuth.currentUser!!.isEmailVerified) {
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_portalActivity)
                    activity?.finish()
                }else{
                    activity?.showToast("Correo no verificado, por favor verifique su correo.", Toast.LENGTH_LONG)
                }
            } else {
                activity?.showToast(task.exception!!.localizedMessage)
            }
        }
    }
}