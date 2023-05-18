package com.example.gymapp

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.gymapp.databinding.CreateExerciseDialogBinding
import com.google.android.material.textfield.TextInputEditText

//import com.example.gymapp.databinding.CreateExerciseDialogBinding

class CreateExerciseDialog: DialogFragment() {
    companion object {
        val TAG = "input_dialog"
    }

    interface InputDialogListener {
        fun onInputTextChanged (text: String) {
        }
    }

    lateinit var inputText: String
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // create the dialog popup
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.create_exercise_title_dialog)
        builder.setView(R.layout.create_exercise_dialog)


        // set up buttons
        builder.setPositiveButton(R.string.ok) { _, _ ->
            getInputText()
//            val listener = targetFragment as InputDialogListener
//            listener.onInputTextChanged(inputText)
            AppDataBase(this.requireContext()).insertTag(inputText)

        }
        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun getInputText() {
        // extract input text from EditText
        inputText = view?.findViewById<TextInputEditText>(R.id.exercise_name_input)?.text.toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Do other fragment initialization here if needed
        return inflater.inflate(R.layout.create_exercise_dialog, container, false)
    }
//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // add any additional behavior to UI widgets
//        view.findViewById<EditText>(R.id.exercise_name_input)?.setText(inputText)
    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}