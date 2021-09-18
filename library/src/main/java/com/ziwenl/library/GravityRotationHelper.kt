package com.ziwenl.library

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.ziwenl.library.GravityRotationImageView.Companion.MOVING_RANGE_DEFAULT
import org.jetbrains.annotations.NotNull

/**
 * PackageName : com.ziwenl.library
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 14:13
 * Introduction : 重力感应帮助类
 * 实例磁场和加速度传感器，并注册监听
 * 通过 lifecycle 绑定宿主生命周期实现注册反注册
 * 传递磁场和加速度传感器获得的数据给 GravityRotationImageView
 *
 * @param context 上下文对象，必须已实现 LifecycleOwner
 */
class GravityRotationHelper(context: Context) {


    /**
     * 加速度与磁场传感器检测值集
     */
    private var mAccelerationValues: FloatArray? = null
    private var mMagneticValues: FloatArray? = null

    /**
     * 前景、后景
     * 通过前景后景反向移动，实现裸眼 3D 效果
     */
    private var mFrontView: GravityRotationImageView? = null
    private var mBackView: GravityRotationImageView? = null

    /**
     * 最大可移动范围（前景后景的可移动范围）
     */
    private var mMaxMovingRange = 0f


    /**
     * 传感器管理器与监听
     */
    private var mSensorManager: SensorManager? = null
    private var mSensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    //加速度
                    mAccelerationValues = event.values
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    //磁场
                    mMagneticValues = event.values
                }
            }
            if (mAccelerationValues != null && mMagneticValues != null) {
                if (mFrontView != null && mBackView !== null) {
                    mFrontView?.handleSensorChangedValues(
                        mAccelerationValues!!,
                        mMagneticValues!!,
                        mMaxMovingRange
                    )
                    mBackView?.handleSensorChangedValues(
                        mAccelerationValues!!,
                        mMagneticValues!!,
                        mMaxMovingRange
                    )
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    init {
        if (context is LifecycleOwner) {
            //获取传感器管理类实例
            mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            //加速度传感器实例
            val accelerationSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            //磁场传感器
            val magneticSensor = mSensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
            context.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                fun onResume(@NotNull owner: LifecycleOwner) {
                    //注册监听
                    mSensorManager?.registerListener(
                        mSensorEventListener,
                        accelerationSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                    mSensorManager?.registerListener(
                        mSensorEventListener,
                        magneticSensor,
                        SensorManager.SENSOR_DELAY_GAME
                    )
                }

                @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                fun onPause(@NotNull owner: LifecycleOwner) {
                    mSensorManager?.unregisterListener(mSensorEventListener)
                }
            })
        } else {
            Log.e(
                "GravityRotationHelper",
                "GravityRotationHelper init error : context is LifecycleOwner = false "
            )
        }
    }


    /**
     * 添加需要实现裸眼 3D 效果的视图组
     * 旋转移动过程中，前景后景随旋转角度偏移
     * @param frontView 前景
     * @param backView 后景
     * @param maxMovingRange 最大可移动范围 dp
     */
    fun attachViews(
        frontView: GravityRotationImageView,
        backView: GravityRotationImageView,
        maxMovingRange: Float = MOVING_RANGE_DEFAULT
    ) {
        //旧持有前景后景 View 不为空时，记录并重置对应 scroll 值
        val oldFrontViewScrollX = mFrontView?.scrollX ?: 0
        val oldFrontViewScrollY = mFrontView?.scrollY ?: 0
        val oldBackViewScrollX = mBackView?.scrollX ?: 0
        val oldBackViewScrollY = mBackView?.scrollY ?: 0
        val oldRotationAngleX = mFrontView?.rotationAngleX ?: 0
        val oldRotationAngleY = mFrontView?.rotationAngleY ?: 0
        mFrontView = frontView
        mBackView = backView
        mFrontView?.rotationAngleX = oldRotationAngleX
        mFrontView?.rotationAngleY = oldRotationAngleY
        mBackView?.rotationAngleX = oldRotationAngleX
        mBackView?.rotationAngleY = oldRotationAngleY
        //继承上一组前景后景 View 的 scroll 值
        mFrontView?.scrollTo(oldFrontViewScrollX, oldFrontViewScrollY)
        mBackView?.scrollTo(oldBackViewScrollX, oldBackViewScrollY)
        mMaxMovingRange = maxMovingRange
    }
}