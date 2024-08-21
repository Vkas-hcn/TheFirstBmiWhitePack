package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiRepository
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.UserWeightBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityHistoryBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.weight.WeightAdapter
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.calculateBMI
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.calculateBMIState
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.navigateTo


class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private var historyBeanList: MutableList<BmiBean> = ArrayList()
    val bmiRepository = BmiRepository(BmiApp.appContext)
    private var isAllDelete = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        clickEvent()
        initAdapter()
    }

    private fun initData() {
        binding.noRecord = historyBeanList.isEmpty()
        if (historyBeanList.isEmpty()) {
            binding.tvAvData.text = "0.00"
            binding.tvHiData.text = "0.00"
            binding.tvLoData.text = "0.00"
            binding.tvLData.text = "0"
            binding.tvNData.text = "0"
            binding.tvHData.text = "0"
            binding.tvOData.text = "0"
            return
        }
        val (averageBMI, maxBMI, minBMI) = calculateBmiStats(historyBeanList)
        binding.tvAvData.text = String.format("%.2f", averageBMI)
        binding.tvHiData.text = String.format("%.2f", maxBMI)
        binding.tvLoData.text = String.format("%.2f", minBMI)
        val bmiStateCounts = countBMIStates(historyBeanList)
        Log.e("TAG", "BMI State Counts: $bmiStateCounts" )
        binding.tvLData.text = bmiStateCounts["Underweight"].toString()
        binding.tvNData.text = bmiStateCounts["Normal"].toString()
        binding.tvHData.text = bmiStateCounts["Overweight"].toString()
        binding.tvOData.text = bmiStateCounts["Obesity"].toString()
    }

    private fun clickEvent() {
        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.imgCheck.setOnClickListener {
            isAllDelete = !isAllDelete
            showAllDelete(isAllDelete)
        }
        binding.tvDelete.setOnClickListener {
            val selectedItems = binding.rvHis.slidOutPositions.sortedDescending()
            try {
                selectedItems.forEach { position ->
                    bmiRepository.deleteRecord(historyBeanList[position].timestamp)
                    historyBeanList.remove(historyBeanList[position])
                    adapter.notifyItemRemoved(position)
                }
                binding.rvHis.slidOutPositions.clear()
                initData()
                showAllDelete(false)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        binding.tvCancel.setOnClickListener {
            binding.checkAll = false
        }
    }

    private fun showAllDelete(isAllDelete:Boolean){
        binding.checkAll = isAllDelete
        if (isAllDelete) {
            binding.rvHis.slideAllItems()
        } else {
            binding.rvHis.closeAllMenus()
        }
    }

    private fun initAdapter() {
        historyBeanList = bmiRepository.getAllRecords()
        initData()
        adapter = HistoryAdapter(historyBeanList)
        binding.rvHis.layoutManager = LinearLayoutManager(this)
        binding.rvHis.adapter = adapter
        binding.rvHis.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        adapter.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener {
            override fun onItemClick(bean: BmiBean) {
                navigateTo<ResultActivity> {
                    putExtra("weight", bean.weight)
                    putExtra("height", bean.height)
                    putExtra("remark", bean.remark)
                    putExtra("timestamp", bean.timestamp)
                }
            }

            override fun onItemDeleteClick(bean: BmiBean, position: Int) {
                deleteItem(bean)
            }
        })
    }
    private fun deleteItem(bean: BmiBean) {
        bmiRepository.deleteRecord(bean.timestamp)
        historyBeanList = bmiRepository.getAllRecords()
        adapter.upDataListData(historyBeanList)
        binding.rvHis.closeAllMenus()
        initData()
    }
    fun calculateBmiStats(bmiList: MutableList<BmiBean>): Triple<Double, Double, Double> {
        val bmiValues = bmiList.mapNotNull { bean ->
            bean.height.let { height ->
                bean.weight.let { weight ->
                    calculateBMI(height, weight).toDoubleOrNull()
                }
            }
        }

        val averageBMI = bmiValues.average()
        val maxBMI = bmiValues.maxOrNull() ?: 0.0
        val minBMI = bmiValues.minOrNull() ?: 0.0

        return Triple(averageBMI, maxBMI, minBMI)
    }
    fun countBMIStates(bmiList: MutableList<BmiBean>): Map<String, Int> {
        val stateCounts = mutableMapOf(
            "Underweight" to 0,
            "Normal" to 0,
            "Overweight" to 0,
            "Obesity" to 0
        )

        bmiList.forEach { bean ->
            val height = bean.height
            val weight = bean.weight
            if (!height.isNullOrEmpty() && !weight.isNullOrEmpty()) {
                val state = calculateBMIState(height, weight)
                stateCounts[state] = stateCounts[state]?.plus(1) ?: 1
            }
        }

        return stateCounts
    }
}