package com.example.fribu.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.fribu.databinding.FragmentSignInPageBinding


class SignInPageFragment : Fragment() {
    private var _binding: FragmentSignInPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signGoogle.setOnClickListener{
            signGoogle(it)
        }
        binding.signEmail.setOnClickListener{
            signMail(it)
        }
        binding.signAnonym.setOnClickListener{
            signAnonymus(it)
        }
    }
    private fun signGoogle(view: View){
        val action = SignInPageFragmentDirections.actionSignInPageFragmentToFragmentFeed()
        Navigation.findNavController(view).navigate(action)
    }
    private fun signMail(view: View){
        val action = SignInPageFragmentDirections.actionSignInPageFragmentToSignUpEmailLoginFragment()
        Navigation.findNavController(view).navigate(action)
    }
    private fun signAnonymus(view: View){
        val action = SignInPageFragmentDirections.actionSignInPageFragmentToFragmentFeed2()
        Navigation.findNavController(view).navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}