package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.zzzzyyy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityKpBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityMainBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.add.AddActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi.HistoryActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.weight.WeightActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.weight.WeightAdapter
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.navigateTo
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwzz.Wwwwzzz
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        clickEvent()
    }

    private fun clickEvent() {
        onBackPressedDispatcher.addCallback(this) {
            if (binding.dlHome.isOpen) {
                binding.dlHome.close()
                return@addCallback
            }
            finish()
            exitProcess(-1)
        }
        binding.imgBmi.setOnClickListener {
            navigateTo<AddActivity>()
        }
        binding.imgHis.setOnClickListener {
            navigateTo<HistoryActivity>()
        }
        binding.imgAr.setOnClickListener {
            navigateTo<Wwwwzzz>()
        }
        binding.imgWe.setOnClickListener {
            navigateTo<WeightActivity>()
        }
        binding.aivMenu.setOnClickListener {
            binding.dlHome.open()
        }


        binding.tvShare.setOnClickListener {
            BmiUtils.shareText(this)
        }
        binding.tvRate.setOnClickListener {
            startActivity(Intent("android.intent.action.VIEW", Uri.parse("https://baidu.com/")))
        }
        binding.tvUserTerms.setOnClickListener {
            startActivity(Intent("android.intent.action.VIEW", Uri.parse("https://baidu.com/")))
        }
        binding.tvPrivacyPolicy.setOnClickListener {
            startActivity(
                Intent(
                    "android.intent.action.VIEW", Uri.parse("https://baidu.com/")
                )
            )
        }
    }
}