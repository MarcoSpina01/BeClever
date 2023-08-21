package com.example.beclever.ui.home

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
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
import com.example.beclever.databinding.FragmentHomeBinding
import java.util.*
import com.example.beclever.ui.plus.FilteredLessonFragment
import com.example.beclever.ui.plus.Lesson
import com.example.beclever.ui.profile.ModifyProfileFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var provinceList: Array<String>
    private lateinit var filteredProvinceList: ArrayList<String>
    private lateinit var filteredLessonsList: List<Lesson>

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        bindingView.viewModel = homeViewModel // Collega il ViewModel al binding
        bindingView.lifecycleOwner = viewLifecycleOwner
        return bindingView.root

       // Importante per osservare i LiveData
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindingView.textInputEditTextMateria.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(bindingView.textInputEditTextMateria.windowToken, 0)
                bindingView.textInputEditTextMateria.clearFocus()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        bindingView.textInputEditTextTarget.isFocusable = false
        bindingView.textInputEditTextTarget.setOnClickListener(){
            bindingView.textInputEditTextMateria.clearFocus()
            showTargetOptions()
            bindingView.textInputEditTextTarget.clearFocus()
        }

        bindingView.textInputEditTextData.isFocusable = false
        bindingView.textInputEditTextData.setOnClickListener {
            bindingView.textInputEditTextMateria.clearFocus()
            showDatePicker()
            bindingView.textInputEditTextData.clearFocus()
        }

        bindingView.textInputEditTextLocalita.isFocusable = false
        bindingView.textInputEditTextLocalita.setOnClickListener(){
            bindingView.textInputEditTextMateria.clearFocus()
            showProvinceDialog()
            bindingView.textInputEditTextLocalita.clearFocus()
        }


        bindingView.buttonCerca.setOnClickListener {
            val subject = bindingView.textInputEditTextMateria.text.toString().trim()
            val date = bindingView.textInputEditTextData.text.toString()
            val target = bindingView.textInputEditTextTarget.text.toString()
            val location = bindingView.textInputEditTextLocalita.text.toString()

            // Chiamata al metodo checkIfLessonExists del ViewModel
            if (subject.isNotEmpty() || date.isNotEmpty() || target.isNotEmpty() || location.isNotEmpty()) {
                homeViewModel.checkIfLessonExists(
                    requireContext().applicationContext, subject, date, target, location)
            } else {
                Toast.makeText(context, "Inserisci un campo per la ricerca!", Toast.LENGTH_SHORT).show()
            }
            homeViewModel.filteredLessonsList.observe(viewLifecycleOwner) { filteredLessonsList ->
                if (filteredLessonsList.isNotEmpty()) {
                    val filteredLessonsFragment = FilteredLessonFragment()
                    val bundle = Bundle()
                    bundle.putSerializable("filteredLessonsList", ArrayList(filteredLessonsList))
                    filteredLessonsFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, filteredLessonsFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    // Mostra un messaggio che indica che non ci sono lezioni corrispondenti
                }
            }
        }

        bindingView.Clean.setOnClickListener {
            bindingView.textInputEditTextMateria.text?.clear()
            bindingView.textInputEditTextData.text?.clear()
            bindingView.textInputEditTextTarget.text?.clear()
            bindingView.textInputEditTextLocalita.text?.clear()
        }


        view.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            view.clearFocus()
            bindingView.textInputEditTextMateria.clearFocus()
            bindingView.textInputEditTextData.clearFocus()
            bindingView.textInputEditTextTarget.clearFocus()
            bindingView.textInputEditTextLocalita.clearFocus()
        }
    }

    private fun showTargetOptions() {
        val targetOptions = arrayOf( "Scuola Superiore", "Scuola Media", "Universita")

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

    override fun onResume() {
        super.onResume()

        // Pulisci i campi di input e i dati filtrati
        bindingView.textInputEditTextMateria.text?.clear()
        bindingView.textInputEditTextData.text?.clear()
        bindingView.textInputEditTextTarget.text?.clear()
        bindingView.textInputEditTextLocalita.text?.clear()
        homeViewModel.clearFilteredLessonsList()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}