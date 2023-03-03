package com.pawan.notesapp.utils

import android.util.Pair.create
import com.pawan.notesapp.UI.Login.RegisterFrag
import com.pawan.notesapp.utils.TokenManager_Factory.create
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.addHeaderLenient
import okhttp3.internal.isSensitiveHeader
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object Constants {

    const val TAG = "My Note"
    const val BASE_URL = "https://note-node-module-f.onrender.com/"
    const val PREFS_TOKEN_FILE = "PREFS_TOKEN_FILE"
    const val USER_TOKEN = "USER_TOKEN"

}