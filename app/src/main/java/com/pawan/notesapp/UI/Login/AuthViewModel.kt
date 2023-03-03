package com.pawan.notesapp.UI.Login

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawan.notesapp.Model.UserRequest
import com.pawan.notesapp.Model.UserResponse
import com.pawan.notesapp.repository.UserRepository
import com.pawan.notesapp.utils.NetwordResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor (private val userRepository: UserRepository):ViewModel(){

    val _userResponseLiveData :LiveData<NetwordResult<UserResponse>>
    get() = userRepository.userResponseLiveData

    fun registerUser(userRequest: UserRequest)
    {
        viewModelScope.launch {
            userRepository.registerUser(userRequest);
        }
    }

    fun loginUser(userRequest: UserRequest)
    {
        viewModelScope.launch {
            userRepository.loginUser(userRequest);
        }
    }

    fun validateCredentials(username:String, emailAddress : String, password:String, isLogin:Boolean):Pair<Boolean,String>
    {
        val len = 5
        var result = Pair(true,"")
        if(!isLogin && TextUtils.isEmpty(username) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){

            result = Pair(false , " Please Prove the credentials")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){

            result = Pair(false,"Please provide valid Email")
        }

        else if (!TextUtils.isEmpty(password) && password.length <= len){
            result = Pair(false, "Password Length is not Should be greater then 5")
        }
        return result
    }
}