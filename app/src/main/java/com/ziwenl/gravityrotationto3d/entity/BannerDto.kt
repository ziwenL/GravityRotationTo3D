package com.ziwenl.gravityrotationto3d.entity

import androidx.annotation.DrawableRes

/**
 * PackageName : com.ziwenl.gravityrotationto3d.entity
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 15:15
 * Introduction :
 */
data class BannerDto(
    @DrawableRes val backRes: Int,
    @DrawableRes val middleRes: Int,
    @DrawableRes val frontRes: Int,
    var isShowMiddle: Boolean = true
)