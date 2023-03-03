package com.pawan.notesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pawan.notesapp.Model.UserRequest
import com.pawan.notesapp.Model.UserResponse
import com.pawan.notesapp.api.UserAPI
import com.pawan.notesapp.utils.NetwordResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetwordResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetwordResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetwordResult.Loading())
        val response = userAPI.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetwordResult.Loading())
        val response =userAPI.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetwordResult.Sucess(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetwordResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetwordResult.Error("Something Went Wrong"))
        }
    }
}