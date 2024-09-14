package com.example.myproject.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.Adapter.CategoryAdapter
import com.example.myproject.Adapter.TopDoctorAdapter
import com.example.myproject.ViewModel.MainViewModel
import com.example.myproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")
        binding.textName.text = "Hi, $name"

        initCategory()
        initTopDoctors()
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                filterCategories(query)
                filterDoctors(query)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.Account.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            startActivity(intent)
        }
    }

    private fun initTopDoctors() {
        binding.apply {
            progressBarTopDoctor.visibility = View.VISIBLE
            viewModel.doctors.observe(this@MainActivity, Observer{
                recyclerViewTopDoctor.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
                recyclerViewTopDoctor.adapter = TopDoctorAdapter(it)
                progressBarTopDoctor.visibility = View.GONE
            })
            viewModel.loadDoctors()

            doctorListTxt.setOnClickListener {
                startActivity(Intent(this@MainActivity, TopDoctorsActivity::class.java))
            }
            WishList.setOnClickListener{
                startActivity(Intent(this@MainActivity, TopDoctorsActivity::class.java))
            }

        }
    }

private fun initCategory() {
    binding.progressBarCategory.visibility = View.VISIBLE
    viewModel.category.observe(this, Observer { categoryList ->
        binding.viewCategory.layoutManager =
            LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

        binding.viewCategory.adapter = CategoryAdapter(categoryList) { selectedCategory ->
            filterDoctorsByCategory(selectedCategory.Name)
        }

        binding.progressBarCategory.visibility = View.GONE
    })
    viewModel.loadCategory()
}

    private fun filterDoctorsByCategory(categoryName: String) {
        viewModel.doctors.observe(this, Observer { doctorList ->
            val filteredDoctors = doctorList.filter { it.Special == categoryName }

            binding.recyclerViewTopDoctor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewTopDoctor.adapter = TopDoctorAdapter(filteredDoctors.toMutableList())

            binding.progressBarTopDoctor.visibility = View.GONE
        })
    }
    private fun filterCategories(query: String) {
        viewModel.category.observe(this, Observer { categoryList ->
            val filteredCategories = categoryList.filter {
                it.Name.contains(query, ignoreCase = true)
            }
            binding.viewCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.viewCategory.adapter = CategoryAdapter(filteredCategories.toMutableList()) { selectedCategory ->
                filterDoctorsByCategory(selectedCategory.Name)
            }
        })
    }

    private fun filterDoctors(query: String) {
        viewModel.doctors.observe(this, Observer { doctorList ->
            val filteredDoctors = doctorList.filter {
                it.Name.contains(query, ignoreCase = true) || it.Special.contains(query, ignoreCase = true)
            }
            binding.recyclerViewTopDoctor.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.recyclerViewTopDoctor.adapter = TopDoctorAdapter(filteredDoctors.toMutableList())
        })
    }

}