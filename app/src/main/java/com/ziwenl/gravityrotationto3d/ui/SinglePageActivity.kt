package com.ziwenl.gravityrotationto3d.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ziwenl.gravityrotationto3d.databinding.ActivitySinglepageBinding
import com.ziwenl.library.GravityRotationHelper

/**
 * PackageName : com.ziwenl.gravityrotationto3d.ui
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 15:26
 * Introduction :
 */
class SinglePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivitySinglepageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        GravityRotationHelper(this).attachViews(viewBinding.ivFront, viewBinding.ivBack)
    }
}