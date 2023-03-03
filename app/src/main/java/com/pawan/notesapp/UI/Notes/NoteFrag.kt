package com.pawan.notesapp.UI.Notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.pawan.notesapp.Model.NoteRequest
import com.pawan.notesapp.Model.NoteResponse
import com.pawan.notesapp.R
import com.pawan.notesapp.databinding.FragmentNoteBinding
import com.pawan.notesapp.utils.NetwordResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFrag : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private val noteViewModel by viewModels<NoteViewModel>()
    private var note: NoteResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetwordResult.Sucess -> {
                    findNavController().popBackStack()
                }
                is NetwordResult.Error -> {

                }
                is NetwordResult.Loading -> {

                }
            }
        })
    }

    private fun bindHandlers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }
        binding.apply {
            btnSubmit.setOnClickListener {
                val title = txtTitle.text.toString()
                val description = txtDescription.text.toString()
                val noteRequest = NoteRequest(description, title)
                if (note == null) {
                    noteViewModel.createNote(noteRequest)
                } else {
                    noteViewModel.updateNote(note!!._id, noteRequest)
                }
            }
        }
    }

    private fun setInitialData() {
        val jsonNote = arguments?.getString("note")
        if (jsonNote != null) {
            note = Gson().fromJson<NoteResponse>(jsonNote, NoteResponse::class.java)
            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.description)
            }
        }
        else{
            binding.addEditText.text = resources.getString(R.string.add_note)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}