package com.the.first.bmi.white.pack.thefirstbmiwhitepack.jjjjjmm.weight

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

class WeightAdapter(private var itemList: MutableList<UserWeightBean>) : RecyclerView.Adapter<WeightAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(bean: UserWeightBean)
        fun onItemDeleteClick(bean: UserWeightBean,position: Int)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWeDate: TextView = itemView.findViewById(R.id.tv_we_date)
        val tvWeValue: TextView = itemView.findViewById(R.id.tv_we_value)
        val imgDelete: ImageView = itemView.findViewById(R.id.img_delete)
        val imgNote: ImageView = itemView.findViewById(R.id.img_note)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_weight_list, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = itemList[position]
        holder.tvWeValue.text = bean.weight
        holder.tvWeDate.text =BmiUtils.formatTimestamp(bean.timestamp)
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



    fun upDataListData(listData:MutableList<UserWeightBean>){
        itemList.clear()
        itemList = listData
        notifyDataSetChanged()
    }
}
