package com.ziwenl.gravityrotationto3d

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ziwenl.gravityrotationto3d.databinding.ActivityMainBinding
import com.ziwenl.gravityrotationto3d.ui.BannerActivity
import com.ziwenl.gravityrotationto3d.ui.SinglePageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.btnOpenSingle.setOnClickListener {
            startActivity(Intent(this, SinglePageActivity::class.java))
        }
        viewBinding.btnOpenBanner.setOnClickListener {
            startActivity(Intent(this, BannerActivity::class.java))
        }
    }
}