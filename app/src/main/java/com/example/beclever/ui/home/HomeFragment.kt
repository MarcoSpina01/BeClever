package com.example.beclever.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import android.widget.AutoCompleteTextView
import com.example.beclever.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var subjectInput: TextInputEditText

    private lateinit var targetInputLayout: TextInputLayout
    private lateinit var targetInput: TextInputEditText

    private lateinit var dateInputLayout: TextInputLayout
    private lateinit var dateInput: EditText

    private lateinit var locationInputLayout : TextInputLayout
    private lateinit var locationInput : TextInputEditText
    private lateinit var provinceList: Array<String>
    private lateinit var filteredProvinceList: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        subjectInput = _binding!!.textInputEditText1

        targetInputLayout = _binding!!.textInputLayout3
        targetInput = _binding!!.textInputEditText3

        targetInput.isFocusable = false
        targetInput.setOnClickListener {
            showTargetOptions()
        }

        dateInputLayout = _binding!!.textInputLayout2
        dateInput = _binding!!.textInputEditText2

        dateInput.isFocusable = false
        dateInput.setOnClickListener {
            showDatePicker()
        }

        locationInputLayout = _binding!!.textInputLayout4
        locationInput = _binding!!.textInputEditText4

        locationInput.isFocusable = false
        locationInput.setOnClickListener {
            showProvinceDialog()
        }

        root.setOnClickListener {
            subjectInput.clearFocus()
            targetInput.clearFocus()
            dateInput.clearFocus()
            locationInput.clearFocus()
        }

        subjectInput.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                textView.clearFocus()
            }
            false
        }

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun showTargetOptions() {
        val targetOptions = arrayOf("Università", "Scuola Superiore", "Scuola Media")

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}