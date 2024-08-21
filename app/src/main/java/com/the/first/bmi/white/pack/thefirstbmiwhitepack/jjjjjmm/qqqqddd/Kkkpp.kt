package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.qqqqddd

import android.content.Intent
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityKpBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi.ResultActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.zzzzyyy.MainActivity
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BBBLLL
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.navigateTo
import kotlin.system.exitProcess

class Kkkpp : AppCompatActivity() {
    private lateinit var binding: ActivityKpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityKpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        startProgressBar()
        CoroutineScope(Dispatchers.IO).launch {
            BBBLLL.getBlackList(this@Kkkpp)
        }
        onBackPressedDispatcher.addCallback {
        }
    }


    private fun ProgressBar.startProgressFlow(duration: Long, onFinish: () -> Unit): Job {
        val scope = CoroutineScope(Dispatchers.Main)
        val progressFlow = flow {
            val stepTime = duration / 100
            for (i in 0..100) {
                emit(i)
                delay(stepTime)
            }
        }

        return scope.launch {
            progressFlow.collect { progress ->
                this@startProgressFlow.progress = progress
                if (progress == 100) {
                    onFinish()
                }
            }
        }
    }

    // Usage example
    private fun startProgressBar() {
        binding.proBar.startProgressFlow(3000L) {
            // Navigate to another fragment or activity when the progress is complete
            toMain()
        }
    }

    fun toMain() {
        navigateTo<MainActivity>(finishCurrent = true)
    }
}