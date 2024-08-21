package com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwzz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.R
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.BmiRepository
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.bean.UserWeightBean
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BmiUtils

class WzAdapter(private var itemList: List<String>) : RecyclerView.Adapter<WzAdapter.MyViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgWz: ImageView = itemView.findViewById(R.id.img_wz)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_wz, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean = itemList[position]
        holder.imgWz.setImageResource(WzData.wzImageList[position])
        holder.tvTitle.text =bean
        holder.tvMessage.text =WzData.contentShrinkage[position]

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}
