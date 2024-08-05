package com.example.fribu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.fribu.R
import com.example.fribu.databinding.FragmentKayitEkraniBinding
import com.example.fribu.databinding.FragmentSignUpEmailLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class KayitEkraniFragment : Fragment() {
    private var _binding: FragmentKayitEkraniBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKayitEkraniBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener { kayitol(it) }
    }
    fun kayitol(view: View){
        val email = binding.edttxtregistermail.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        val cpassword = binding.editTextTextPasswordConfirm.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty() && cpassword == password){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val action = KayitEkraniFragmentDirections.actionKayitEkraniFragmentToSignUpEmailLoginFragment()
                    Navigation.findNavController(requireView()).navigate(action) }
            }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show() }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}