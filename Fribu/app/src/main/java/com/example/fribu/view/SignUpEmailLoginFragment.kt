package com.example.fribu.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation

import com.example.fribu.databinding.FragmentSignUpEmailLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class SignUpEmailLoginFragment : Fragment() {
    private var _binding: FragmentSignUpEmailLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpEmailLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnloginRegister.setOnClickListener { girisYap(it) }
        binding.txtnotmember.setOnClickListener {kayitOl(it)}
        binding.txtforgotpassword.setOnClickListener {sifremiUnuttum(it)}

        val gunceluser = auth.currentUser
        if (gunceluser!=null){
            val action = SignUpEmailLoginFragmentDirections.actionSignUpEmailLoginFragmentToFragmentFeed()
            Navigation.findNavController(view).navigate(action)
        }
    }
    fun girisYap(view: View){
        val email = binding.edittextloginMail.text.toString()
        val password = binding.edittextloginPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val action = SignUpEmailLoginFragmentDirections.actionSignUpEmailLoginFragmentToFragmentFeed()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }
    fun kayitOl(view: View){
        val action = SignUpEmailLoginFragmentDirections.actionSignUpEmailLoginFragmentToKayitEkraniFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun sifremiUnuttum(view: View){
        val action = SignUpEmailLoginFragmentDirections.actionSignUpEmailLoginFragmentToForgotPasswordFragment()
        Navigation.findNavController(view).navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}