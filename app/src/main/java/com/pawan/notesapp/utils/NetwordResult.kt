package com.pawan.notesapp.utils

sealed class NetwordResult<T>(val data: T? = null, val message:String?= null){

    class Sucess<T>(data :T) : NetwordResult<T>(data)
    class Error<T>(message: String?, data:T? = null):NetwordResult<T>(data,message)
    class Loading<T>:NetwordResult<T>()
}
