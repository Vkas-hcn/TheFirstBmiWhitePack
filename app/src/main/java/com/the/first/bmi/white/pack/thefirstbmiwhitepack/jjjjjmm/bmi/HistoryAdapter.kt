package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.bmi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiRepository
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.UserWeightBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils

class HistoryAdapter(private var itemList: MutableList<BmiBean>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(bean: BmiBean)
        fun onItemDeleteClick(bean: BmiBean, position: Int)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvBmiType: TextView = itemView.findViewById(R.id.tv_bmi_type)
        val tvBmiValue: TextView = itemView.findViewById(R.id.tv_bmi_value)
        val tvBmiDate: TextView = itemView.findViewById(R.id.tv_bmi_date)
        val imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
        val imgNote: ImageView = itemView.findViewById(R.id.img_note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_bmi_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = itemList[position]
        holder.tvBmiType.text = BmiUtils.calculateBMIState(bean.height,bean.weight)
        holder.tvBmiType.setTextColor(ContextCompat.getColor(BmiApp.appContext, BmiUtils.calculateBMIColor(holder.tvBmiType.text.toString())))

        holder.tvBmiValue.text =BmiUtils.calculateBMI(bean.height,bean.weight)

        holder.tvBmiDate.text =BmiUtils.formatTimestamp(bean.timestamp)
        holder.imgNote.isVisible = bean.remark.isNotBlank()
        holder.imgDelete.setOnClickListener {
            listener?.onItemDeleteClick(bean,position)

        }
        holder.itemView.setOnClickListener {
            listener?.onItemClick(bean)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun upDataListData(listData:MutableList<BmiBean>){
        itemList.clear()
        itemList = listData
        notifyDataSetChanged()
    }
}
