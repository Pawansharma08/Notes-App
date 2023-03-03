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
import com.pawan.notesapp.databinding.FragmentRegister2Binding
import com.pawan.notesapp.utils.NetwordResult
import com.pawan.notesapp.utils.TokenManager

import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okio.Timeout
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFrag : Fragment() {

    private  var _binding: FragmentRegister2Binding? = null
    private val binding get() = _binding!!
    private  val authViewModel by viewModels<AuthViewModel>()

    private val client = OkHttpClient();

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegister2Binding.inflate(inflater,container, false)

        if(tokenManager.getToken()!=null)
        {
            findNavController().navigate(R.id.action_registerFrag_to_mainFrag)
        }

                return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val validationResult = validateUserInput()
            if(validationResult.first){

                authViewModel.registerUser(getUserRequest())
            }
            else{
                binding.txtError.text = validationResult.second
            }
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFrag_to_loginFrag)
        }
        bindObservers()
    }

    private fun getUserRequest(): UserRequest{
        val emailAdder = binding.txtEmail.text.toString()
        val password = binding.txtEmail.text.toString()
        val username = binding.txtEmail.text.toString()
        return UserRequest(emailAdder,password,username)
    }


    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateCredentials(userRequest.username,userRequest.email,userRequest.password,false)
    }

    private fun bindObservers() {
        authViewModel._userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when(it){
                is NetwordResult.Sucess->{
                    tokenManager.safeToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFrag_to_mainFrag)
                }
                is NetwordResult.Error->{
                    binding.txtError.text = it.message
                }
                is NetwordResult.Loading->{
                    binding.progressBar.isVisible = true
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}