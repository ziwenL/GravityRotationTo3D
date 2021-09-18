package com.ziwenl.gravityrotationto3d.base.recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 15:15
 * Introduction : widgets
 */
abstract class BaseAdapter<T, VB : ViewBinding>(val dataList: List<T>) :
    RecyclerView.Adapter<BaseAdapter<T, VB>.BaseViewHolder>() {

    var callBack: CallBack<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return holder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.build(dataList[position], position, dataList)
    }

    override fun getItemCount(): Int = dataList.size

    abstract fun holder(parent: ViewGroup, viewType: Int): BaseViewHolder

    abstract inner class BaseViewHolder(binding: VB) :
        RecyclerView.ViewHolder(binding.root) {

        abstract fun build(dto: T, position: Int, dataList: List<T>)
    }

    interface CallBack<T> {
        fun onClickItemView(position: Int, dto: T)
    }
}

