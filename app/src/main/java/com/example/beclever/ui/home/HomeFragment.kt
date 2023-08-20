package com.example.beclever.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import android.widget.AutoCompleteTextView
import com.example.beclever.databinding.FragmentHomeBinding
import com.example.beclever.databinding.FragmentProfilenewBinding
import com.example.beclever.ui.profile.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var provinceList: Array<String>
    private lateinit var filteredProvinceList: ArrayList<String>

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        bindingView.viewModel = homeViewModel // Collega il ViewModel al binding
        bindingView.lifecycleOwner = viewLifecycleOwner // Importante per osservare i LiveData



        bindingView.textInputEditTextMateria.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                bindingView.textInputEditTextMateria.clearFocus()
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(bindingView.textInputEditTextMateria.windowToken, 0)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        bindingView.textInputEditTextTarget.isFocusable = false
        bindingView.textInputEditTextTarget.setOnClickListener(){
            showTargetOptions()
            bindingView.textInputEditTextTarget.clearFocus()
        }

        bindingView.textInputEditTextData.isFocusable = false
        bindingView.textInputEditTextData.setOnClickListener {
            showDatePicker()
            bindingView.textInputEditTextData.clearFocus()
        }

        bindingView.textInputEditTextLocalita.isFocusable = false
        bindingView.textInputEditTextLocalita.setOnClickListener(){
            showProvinceDialog()
            bindingView.textInputEditTextLocalita.clearFocus()
        }

        bindingView.textInputEditTextPrezzo.isFocusable = false
        bindingView.textInputEditTextPrezzo.setOnClickListener(){
            showMoneyOptions()
            bindingView.textInputEditTextPrezzo.clearFocus()
        }

        bindingView.buttonCerca.setOnClickListener {
            val subject = bindingView.textInputEditTextMateria.text.toString().trim()
            val date = bindingView.textInputEditTextData.text.toString()
            val target = bindingView.textInputEditTextTarget.text.toString()
            val location = bindingView.textInputEditTextLocalita.text.toString()
            val cost = bindingView.textInputEditTextPrezzo.text.toString()

            // Chiamata al metodo checkIfLessonExists del ViewModel
            if (subject.isNotEmpty() && date.isNotEmpty() && target.isNotEmpty() && location.isNotEmpty() && cost.isNotEmpty()) {
            homeViewModel.checkIfLessonExists(requireContext().applicationContext, subject, date, target, location, cost)
                }
            else {
                // Show an error message or handle the case when not all fields are filled
            }
        }



        root.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            root.clearFocus()
            bindingView.textInputEditTextMateria.clearFocus()
            bindingView.textInputEditTextData.clearFocus()
            bindingView.textInputEditTextTarget.clearFocus()
            bindingView.textInputEditTextLocalita.clearFocus()
            bindingView.textInputEditTextPrezzo.clearFocus()
        }
        return root
    }

    private fun showTargetOptions() {
        val targetOptions = arrayOf("Università", "Scuola Superiore", "Scuola Media")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleziona il Target")
        builder.setItems(targetOptions) { _, which ->
            bindingView.textInputEditTextTarget.setText(targetOptions[which])
        }
        builder.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            // Set the selected date i<n the input text field>
            bindingView.textInputEditTextData.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year))
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

        bindingView.textInputEditTextLocalita.isFocusable = true
        bindingView.textInputEditTextLocalita.requestFocus()

        listView.setOnItemClickListener { _, _, position, _ ->
            val province = filteredProvinceList[position]
            bindingView.textInputEditTextLocalita.setText(province)
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
        val targetOptions = arrayOf("0 - 10 €", "10 - 20 €", "20 + €", "Qualsiasi")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Seleziona la fascia di prezzo")
        builder.setItems(targetOptions) { _, which ->
            bindingView.textInputEditTextPrezzo.setText(targetOptions[which])
        }
        builder.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}