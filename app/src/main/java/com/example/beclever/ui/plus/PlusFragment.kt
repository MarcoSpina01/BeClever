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
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.FragmentPlusBinding
import com.google.android.material.textfield.TextInputEditText

class PlusFragment : Fragment() {
    private var _binding: FragmentPlusBinding? = null

    private lateinit var subjectInput: TextInputEditText
    private lateinit var targetInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var locationInput : TextInputEditText
    private lateinit var provinceList: Array<String>
    private lateinit var filteredProvinceList: java.util.ArrayList<String>

    private lateinit var  moneyInput : TextInputEditText

    // This property is only valid between onCreateView and
    // onDestroyView.
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

//        val textView: TextView = binding.textNotifications
//        plusViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

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
            val subject = binding.textInputEditText1.text.toString()
            val date = binding.textInputEditText2.text.toString()
            val target = binding.textInputEditText3.text.toString()
            val location = binding.textInputEditText4.text.toString()
            val cost = binding.textInputEditText5.text.toString()

            lessonViewModel.createLesson(subject, date, target, location, cost) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Lezione creata", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Errore durante la creazione", Toast.LENGTH_SHORT).show()
                }
            }


        }

        return root
    }

    private fun showTargetOptions() {
        val targetOptions = arrayOf("Universita", "Scuola Superiore", "Scuola Media")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleziona il Target")
        builder.setItems(targetOptions) { _, which ->
            targetInput.setText(targetOptions[which])
        }
        builder.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            // Set the selected date in the input text field
            dateInput.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year))
        }, year, month, day)

        datePickerDialog.show()
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}