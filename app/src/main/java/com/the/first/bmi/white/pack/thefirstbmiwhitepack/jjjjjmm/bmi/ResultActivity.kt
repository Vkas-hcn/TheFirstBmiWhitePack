package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityMainBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityResultBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.navigateTo

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initPut()
        initData()
    }

    private fun initPut() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.recordLink.setOnClickListener {
            navigateTo<HistoryActivity>()
        }
    }

    private fun initData() {
        val weight = intent.getStringExtra("weight")
        val height = intent.getStringExtra("height")
        val remark = intent.getStringExtra("remark")
        val timestamp = intent.getLongExtra("timestamp", 0)
        binding.llNote.isVisible = remark?.isNotBlank() == true
        binding.tvBmiValue.text = BmiUtils.calculateBMI(height!!, weight!!)
        binding.tvState.text = BmiUtils.calculateBMIState(height, weight)

        binding.colorState = BmiUtils.calculateBMIState(binding.tvState.text.toString())
        binding.tvState.setTextColor(ContextCompat.getColor(BmiApp.appContext, BmiUtils.calculateBMIColor(binding.tvState.text.toString())))
        binding.tvDate.text = BmiUtils.formatTimestamp(timestamp)
        binding.tvNotes.text = remark
    }
}