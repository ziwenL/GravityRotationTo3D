package com.ziwenl.gravityrotationto3d.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ziwenl.gravityrotationto3d.base.recyclerview.BaseAdapter
import com.ziwenl.gravityrotationto3d.databinding.ItemBannerBinding
import com.ziwenl.gravityrotationto3d.entity.BannerDto

/**
 * PackageName : com.ziwenl.gravityrotationto3dhelper.ui.adapter
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 15:15
 * Introduction :
 */
class ImageAdapter(bannerList: List<BannerDto>) :
    BaseAdapter<BannerDto, ItemBannerBinding>(bannerList) {
    override fun holder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(
            ItemBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class ViewHolder(binding: ItemBannerBinding) : BaseViewHolder(binding) {
        private val ivBack = binding.ivBack
        private val ivMiddle = binding.ivMiddle
        private val ivFront = binding.ivFront

        override fun build(dto: BannerDto, position: Int, dataList: List<BannerDto>) {
            ivBack.setImageResource(dto.backRes)
            ivMiddle.setImageResource(dto.middleRes)
            ivFront.setImageResource(dto.frontRes)
            if (dto.isShowMiddle) {
                ivMiddle.visibility = View.VISIBLE
            } else {
                ivMiddle.visibility = View.INVISIBLE
            }
        }
    }
}