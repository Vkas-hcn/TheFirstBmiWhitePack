package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.weight

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiRepository
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.UserWeightBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.databinding.ActivityWeightBinding
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.calculateBMI
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils.calculateBMIState


class WeightActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeightBinding
    private lateinit var adapter: WeightAdapter
    private var weightBeanList: MutableList<UserWeightBean> = ArrayList()
    private var isAllDelete = false
    val weightRepository = BmiRepository(BmiApp.appContext)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWeightBinding.inflate(layoutInflater)
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
        binding.noRecord = weightBeanList.isEmpty()
        if (weightBeanList.isEmpty()) {
            binding.tvChData.text = "0.00"
            binding.tvCurrentData.text = "0.00"
            binding.tvStartData.text = "0.00"
            binding.tvAverageData.text = "0.00"
            return
        }
        val weightStats = calculateWeightStats(weightBeanList)
        binding.tvChData.text = String.format("%.2f", weightStats["Change"])
        binding.tvCurrentData.text = String.format("%.2f", weightStats["Current"])
        binding.tvStartData.text = String.format("%.2f", weightStats["Start"])
        binding.tvAverageData.text = String.format("%.2f", weightStats["Average"])
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
            val selectedItems = binding.rvHis.slidOutPositions.sortedDescending() // 从大到小排序
            selectedItems.forEach { position ->
                weightRepository.deleteWeightRecord(weightBeanList[position].timestamp)
                weightBeanList.remove(weightBeanList[position])
                adapter.notifyItemRemoved(position)
            }
            binding.rvHis.slidOutPositions.clear()
            initData()
            showAllDelete(false)
        }

        binding.tvCancel.setOnClickListener {
            showAllDelete(false)
        }
        binding.imgAdd.setOnClickListener {
            binding.aetWe.setText("")
            binding.edNotes.setText("")
            binding.tvSave.isVisible = true
            binding.aetWe.setFocusableInTouchMode(true)
            binding.aetWe.isFocusable = true
            binding.aetWe.requestFocus()
            binding.edNotes.setFocusableInTouchMode(true)
            binding.edNotes.isFocusable = true
            binding.edNotes.requestFocus()
            showDialog()
        }
        binding.conAddDialog.setOnClickListener {
            disShowDialog()
        }
        binding.llDialog.setOnClickListener {
        }
        binding.tvSave.setOnClickListener {
            saveWeightData()
        }
    }

    private fun showAllDelete(isAllDelete: Boolean) {
        binding.checkAll = isAllDelete
        binding.imgAdd.isVisible = !isAllDelete
        if (isAllDelete) {
            binding.rvHis.slideAllItems()
        } else {
            binding.rvHis.closeAllMenus()
        }
    }

    private fun initAdapter() {
        weightBeanList = weightRepository.getAllWeightRecords()
        initData()
        adapter = WeightAdapter(weightBeanList)
        binding.rvHis.layoutManager = LinearLayoutManager(this)
        binding.rvHis.adapter = adapter
        binding.rvHis.setLayoutManager(
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        adapter.setOnItemClickListener(object : WeightAdapter.OnItemClickListener {
            override fun onItemClick(bean: UserWeightBean) {
                showItemWeightData(bean)
            }

            override fun onItemDeleteClick(bean: UserWeightBean, position: Int) {
                deleteItem(bean)
            }
        })

    }

    private fun deleteItem(bean: UserWeightBean) {
        weightBeanList.remove(bean)
        weightRepository.deleteWeightRecord(bean.timestamp)
        weightBeanList = weightRepository.getAllWeightRecords()
        adapter.upDataListData(weightBeanList)
        binding.rvHis.closeAllMenus()
        initData()
    }

    private fun calculateWeightStats(bmiList: MutableList<UserWeightBean>): Map<String, Double> {
        if (bmiList.isEmpty()) {
            return mapOf(
                "Start" to 0.0,
                "Current" to 0.0,
                "Change" to 0.0,
                "Average" to 0.0
            )
        }

        val startWeight = bmiList.last().weight.toDoubleOrNull() ?: 0.0
        val currentWeight = bmiList.first().weight.toDoubleOrNull() ?: 0.0
        val changeWeight = currentWeight - startWeight
        val averageWeight = bmiList.mapNotNull { it.weight.toDoubleOrNull() }.average()

        return mapOf(
            "Start" to startWeight,
            "Current" to currentWeight,
            "Change" to changeWeight,
            "Average" to averageWeight
        )
    }

    private fun saveWeightData() {
        val weData = binding.aetWe.text.toString().trim()
        if (weData.isBlank() || !BmiUtils.isValidNumber(weData)) {
            Toast.makeText(this, "Please enter your weight", Toast.LENGTH_SHORT).show()
            return
        }
        val weightBean = UserWeightBean()
        weightBean.weight = weData
        weightBean.remark = binding.edNotes.text.toString()
        weightBean.timestamp = System.currentTimeMillis()
        weightRepository.addWeightRecord(weightBean)
        weightBeanList = weightRepository.getAllWeightRecords()
        adapter.upDataListData(weightBeanList)
        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
        disShowDialog()
        initData()
        adapter.notifyDataSetChanged()
    }

    fun showItemWeightData(bean: UserWeightBean) {
        binding.tvSave.isVisible = false
        binding.aetWe.isFocusable = false
        binding.aetWe.setFocusableInTouchMode(false)
        binding.edNotes.isFocusable = false
        binding.edNotes.setFocusableInTouchMode(false)
        binding.conAddDialog.isVisible = true
        binding.aetWe.setText(bean.weight)
        binding.edNotes.setText(bean.remark)
    }


    private fun disShowDialog() {
        binding.conAddDialog.isVisible = false
        BmiUtils.hideKeyboard(binding.aetWe)
        BmiUtils.hideKeyboard(binding.edNotes)
    }

    private fun showDialog() {
        binding.conAddDialog.isVisible = true
        BmiUtils.showKeyboard(binding.aetWe)
    }
}