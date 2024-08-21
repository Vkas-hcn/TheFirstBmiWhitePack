package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.add

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiRepository
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityAddBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityMainBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi.HistoryActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi.ResultActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.navigateTo

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    val bmiRepository = BmiRepository(BmiApp.appContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initPut()
    }

    private fun initPut() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.tvInquire.setOnClickListener {
            addSaveFun()
        }
    }

    private fun addSaveFun() {
        val we = binding.aetWe.text?.trim().toString()
        val he = binding.aetHe.text?.trim().toString()
        val notes = binding.aetNotes.text?.trim().toString()
        if (we.isBlank() || he.isBlank() || !BmiUtils.isValidNumber(we) || !BmiUtils.isValidNumber(he)) {
            Toast.makeText(this, "Please enter the correct value", Toast.LENGTH_SHORT).show()
            return
        }
        val bean = BmiBean()
        bean.height = he
        bean.weight = we
        bean.remark = notes
        bean.timestamp = System.currentTimeMillis()
        bmiRepository.addRecord(bean)
        navigateTo<ResultActivity>(finishCurrent = true) {
            putExtra("weight", bean.weight)
            putExtra("height", bean.height)
            putExtra("remark", bean.remark)
            putExtra("timestamp", bean.timestamp)
        }
    }
}