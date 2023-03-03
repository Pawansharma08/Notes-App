package com.pawan.notesapp.utils

import android.content.Context
import com.pawan.notesapp.utils.Constants.PREFS_TOKEN_FILE
import com.pawan.notesapp.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context:Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun safeToken(token: String)
    {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN,token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN,null)
    }

}