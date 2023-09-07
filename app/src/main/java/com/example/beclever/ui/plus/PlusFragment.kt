package com.example.beclever.ui.plus

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.app.TimePickerDialog
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.FragmentPlusBinding
import com.google.android.material.textfield.TextInputEditText

/**
 * Fragment per la creazione di una nuova lezione.
 */
class PlusFragment : Fragment() {
    private var _binding: FragmentPlusBinding? = null

    private lateinit var subjectInput: TextInputEditText
    private lateinit var targetInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var locationInput : TextInputEditText
    private lateinit var hourInput : TextInputEditText
    private lateinit var provinceList: Array<String>
    private lateinit var filteredProvinceList: java.util.ArrayList<String>

    private lateinit var  moneyInput : TextInputEditText

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val lessonViewModel =
            ViewModelProvider(this).get(LessonViewModel::class.java)

        _binding = FragmentPlusBinding.inflate(inflater, container, false)
        val root: View = binding.root

        subjectInput = _binding!!.textInputEditText1
        subjectInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                subjectInput.clearFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(subjectInput.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        targetInput = _binding!!.textInputEditText3
        targetInput.isFocusable = false
        targetInput.setOnClickListener {
            subjectInput.clearFocus()
            showTargetOptions()
        }

        dateInput = _binding!!.textInputEditText2
        dateInput.isFocusable = false
        dateInput.setOnClickListener {
            subjectInput.clearFocus()
            showDatePicker()
        }

        locationInput = _binding!!.textInputEditText4
        locationInput.isFocusable = false
        locationInput.setOnClickListener {
            subjectInput.clearFocus()
            showProvinceDialog()
        }

        moneyInput = _binding!!.textInputEditText5
        moneyInput.isFocusable = false
        moneyInput.setOnClickListener {
            subjectInput.clearFocus()
            showMoneyOptions()
        }

        hourInput = _binding!!.textInputEditText6
        hourInput.isFocusable = false
        hourInput.setOnClickListener{
            subjectInput.clearFocus()
            showTimePicker()
        }


        root.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            root.clearFocus()
            subjectInput.clearFocus()
            targetInput.clearFocus()
            dateInput.clearFocus()
            locationInput.clearFocus()
            moneyInput.clearFocus()
        }

        binding.buttonCrea.setOnClickListener {
            createLesson(lessonViewModel)
        }

        return root
    }

    private fun createLesson(lessonViewModel: LessonViewModel) {
        val subject = subjectInput.text.toString()
        val date = dateInput.text.toString()
        val target = targetInput.text.toString()
        val location = locationInput.text.toString()
        val cost = moneyInput.text.toString()
        val hour = hourInput.text.toString()

        if (subject.isNotEmpty() && date.isNotEmpty() && target.isNotEmpty() && location.isNotEmpty() && cost.isNotEmpty()) {
            lessonViewModel.createLesson(subject, date, target, location, cost, hour) { success ->
                if (success) {
                    showToast("Lezione creata")
                    // Resetta i campi di input dopo la creazione
                    clearInputFields()
                } else {
                    showToast("Errore durante la creazione")
                }
            }
        } else {
            showToast("Inserire tutti i campi")
        }
    }

    // Funzione per mostrare le opzioni del campo "Target"
    private fun showTargetOptions() {
        val targetOptions = arrayOf("Universita", "Scuola Superiore", "Scuola Media")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleziona il Target")
        builder.setItems(targetOptions) { _, which ->
            targetInput.setText(targetOptions[which])
        }
        builder.show()
    }

    // Funzione per mostrare il selettore della data
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            // Imposta la data selezionata nel campo di input
            dateInput.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year))
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedHour = String.format("%02d:%02d", hourOfDay, minute)
                hourInput.setText(selectedHour)
            },
            currentHour,
            currentMinute,
            true
        )

        timePickerDialog.show()
    }

    private fun showProvinceDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_province)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val listView = dialog.findViewById<ListView>(R.id.list_view)
        val inputSearch = dialog.findViewById<EditText>(R.id.edit_search)

        provinceList = resources.getStringArray(R.array.province)
        filteredProvinceList = ArrayList(provinceList.toList())
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, filteredProvinceList)
        listView.adapter = adapter

        locationInput.isFocusable = true
        locationInput.requestFocus()

        listView.setOnItemClickListener { _, _, position, _ ->
            val province = filteredProvinceList[position]
            locationInput.setText(province)
            dialog.dismiss()
        }

        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filteredProvinceList.clear()
                for (province in provinceList) {
                    if (province.contains(s.toString(), true)) {
                        filteredProvinceList.add(province)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        dialog.show()
    }

    private fun showMoneyOptions() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Inserisci la cifra")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ ->
            val enteredAmount = input.text.toString()
            moneyInput.setText("$enteredAmount €")
        }

        builder.setNegativeButton("Annulla") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    // Funzione per mostrare un messaggio Toast
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Funzione per ripulire i campi di input
    private fun clearInputFields() {
        subjectInput.text = null
        dateInput.text = null
        targetInput.text = null
        locationInput.text = null
        moneyInput.text = null
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}