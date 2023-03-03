package com.pawan.notesapp.UI.Login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.pawan.notesapp.Model.UserRequest
import com.pawan.notesapp.R
import com.pawan.notesapp.databinding.FragmentLoginBinding

import com.pawan.notesapp.utils.NetwordResult
import com.pawan.notesapp.utils.TokenManager

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFrag : Fragment() {

    private var _binding : FragmentLoginBinding?= null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validationResult = validateUserInput()
            if(validationResult.first) {
                authViewModel.loginUser(getUserRequest())
            }
            else{
                binding.txtError.text = validationResult.second
            }
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
        bindObserver()
    }

    private fun bindObserver() {
        authViewModel._userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false

            when(it){

                is NetwordResult.Sucess->{
                    tokenManager.safeToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFrag_to_mainFrag)
                }
                is NetwordResult.Error->{
                    binding.txtError.text = it.message
                }
                is NetwordResult.Loading->
                {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.email,userRequest.password,true)
    }

    private fun getUserRequest(): UserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return UserRequest(emailAddress,password,"")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}