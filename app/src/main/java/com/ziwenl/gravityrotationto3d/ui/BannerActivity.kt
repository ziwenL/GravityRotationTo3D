package com.ziwenl.gravityrotationto3d.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ziwenl.gravityrotationto3d.R
import com.ziwenl.gravityrotationto3d.databinding.ActivityBannerBinding
import com.ziwenl.gravityrotationto3d.entity.BannerDto
import com.ziwenl.gravityrotationto3d.ui.adapter.ImageAdapter
import com.ziwenl.library.GravityRotationHelper
import com.ziwenl.library.GravityRotationImageView
import java.util.*

/**
 * PackageName : com.ziwenl.gravityrotationto3d.ui
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 15:27
 * Introduction :
 */
class BannerActivity : AppCompatActivity() {

    private val mBannerList: List<BannerDto> = arrayListOf(
        BannerDto(R.mipmap.banner_a_back, R.mipmap.banner_a_middle, R.mipmap.banner_a_front),
        BannerDto(R.mipmap.banner_b_back, R.mipmap.banner_b_middle, R.mipmap.banner_b_front),
        BannerDto(R.mipmap.banner_c_back, R.mipmap.banner_c_middle, R.mipmap.banner_c_front),
        BannerDto(R.mipmap.banner_d_back, R.mipmap.banner_d_middle, R.mipmap.banner_d_front),
        BannerDto(R.mipmap.banner_e_back, R.mipmap.banner_e_middle, R.mipmap.banner_e_front),
        BannerDto(R.mipmap.banner_f_back, R.mipmap.banner_e_middle, R.mipmap.banner_f_front, false)
    )

    private var mCurrentPosition = 0
    private val mBannerTimer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityBannerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val gravityRotationHelper = GravityRotationHelper(this)

        viewBinding.vpMain.adapter = ImageAdapter(mBannerList)
        viewBinding.vpMain.offscreenPageLimit = mBannerList.size
        val pageRecyclerView = viewBinding.vpMain.getChildAt(0) as RecyclerView
        viewBinding.vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mCurrentPosition = position
                val pageView = pageRecyclerView.layoutManager?.findViewByPosition(position)
                val ivBack = pageView?.findViewById<GravityRotationImageView>(R.id.iv_back)
                val ivFront = pageView?.findViewById<GravityRotationImageView>(R.id.iv_front)
                val tvPosition = pageView?.findViewById<TextView>(R.id.tv_position)
                tvPosition?.text = position.toString()
                //拿到轮播图当前页需要实现伪 3D 效果的后景和前景 view ,通过帮助类实现伪 3D 效果
                if (ivFront != null && ivBack != null) {
                    gravityRotationHelper.attachViews(ivFront, ivBack)
                }
            }
        })

        mBannerTimer.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (mCurrentPosition >= mBannerList.size - 1) {
                        mCurrentPosition = 0
                    } else {
                        mCurrentPosition++
                    }
                    viewBinding.vpMain.currentItem = mCurrentPosition
                }
            }
        }, 3000, 3000)
    }

    override fun onStop() {
        mBannerTimer.cancel()
        super.onStop()
    }
}