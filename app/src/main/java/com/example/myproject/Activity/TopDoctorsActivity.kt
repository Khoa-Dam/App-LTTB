package com.example.myproject.Activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myproject.Adapter.TopDoctorAdapter
import com.example.myproject.Adapter.TopDoctorAdapter2
import com.example.myproject.R
import com.example.myproject.ViewModel.MainViewModel
import com.example.myproject.databinding.ActivityTopDoctorsBinding
import kotlinx.coroutines.MainScope

class TopDoctorsActivity : BaseActivity() {
    private lateinit var binding: ActivityTopDoctorsBinding
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopDoctorsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTopDoctors()

    }
    private fun initTopDoctors() {
        binding.apply {
            progressBarTopDoctor.visibility = View.VISIBLE
            viewModel.doctors.observe(this@TopDoctorsActivity, Observer{
                viewTopDoctorList.layoutManager = LinearLayoutManager(this@TopDoctorsActivity, LinearLayoutManager.VERTICAL, false)
                viewTopDoctorList.adapter = TopDoctorAdapter2(it)
                progressBarTopDoctor.visibility = View.GONE
            })
            viewModel.loadDoctors()

            backBtn.setOnClickListener{ finish() }
        }
    }
}