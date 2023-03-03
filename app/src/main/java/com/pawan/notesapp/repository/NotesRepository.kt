package com.pawan.notesapp.repository

import androidx.lifecycle.MutableLiveData
import com.pawan.notesapp.Model.NoteRequest
import com.pawan.notesapp.Model.NoteResponse
import com.pawan.notesapp.api.NotesApi
import com.pawan.notesapp.utils.NetwordResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NotesRepository @Inject constructor(private val noteAPI: NotesApi) {

    private val _notesLiveData = MutableLiveData<NetwordResult<List<NoteResponse>>>()
    val notesLiveData get() = _notesLiveData

    private val _statusLiveData = MutableLiveData<NetwordResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetwordResult.Loading())
        val response = noteAPI.createNote(noteRequest)
        handleResponse(response, "Note Created")
    }

    suspend fun getNotes() {
        _notesLiveData.postValue(NetwordResult.Loading())
        val response = noteAPI.getNotes()
        if (response.isSuccessful && response.body() != null) {
            _notesLiveData.postValue(NetwordResult.Sucess(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetwordResult.Error(errorObj.getString("message")))
        } else {
            _notesLiveData.postValue(NetwordResult.Error("Something Went Wrong"))
        }
    }

    suspend fun updateNote(id: String, noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetwordResult.Loading())
        val response = noteAPI.updateNote(id, noteRequest)
        handleResponse(response, "Note Updated")
    }

    suspend fun deleteNote(noteId: String) {
        _statusLiveData.postValue(NetwordResult.Loading())
        val response = noteAPI.deleteNote(noteId)
        handleResponse(response, "Note Deleted")
    }

    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetwordResult.Sucess(Pair(true, message)))
        } else {
            _statusLiveData.postValue(NetwordResult.Sucess(Pair(false, "Something went wrong")))
        }
    }
}