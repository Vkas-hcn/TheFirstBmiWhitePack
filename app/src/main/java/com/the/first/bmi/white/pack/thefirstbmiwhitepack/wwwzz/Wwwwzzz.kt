package com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwzz

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.UserWeightBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityWzBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.weight.WeightAdapter

class Wwwwzzz : AppCompatActivity() {
    private lateinit var binding: ActivityWzBinding
    private lateinit var adapter: WzAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWzBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.imgBack.setOnClickListener {
            if(binding.wzState!=0){
                binding.wzState = 0
                return@setOnClickListener
            }
            finish()
        }
        onBackPressedDispatcher.addCallback {
            if(binding.wzState!=0){
                binding.wzState = 0
                return@addCallback
            }
            finish()
        }
        initAdapter()
    }

    private fun initAdapter() {
        binding.wzState = 0
        adapter = WzAdapter(WzData.wzTitleList)
        binding.rvWz.layoutManager = LinearLayoutManager(this)
        binding.rvWz.adapter = adapter
        binding.rvWz.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        adapter.setOnItemClickListener(object : WzAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                binding.wzState = position+1
            }
        })
    }
}