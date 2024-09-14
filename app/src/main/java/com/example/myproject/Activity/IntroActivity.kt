package com.example.myproject.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myproject.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding;
    private var isActivityStarted = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if(!isActivityStarted){
                isActivityStarted = true
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }
        }, 5000)

        binding.apply {
            startBtn.setOnClickListener{
                if(!isActivityStarted){
                    isActivityStarted = true
                    startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }

    }
}