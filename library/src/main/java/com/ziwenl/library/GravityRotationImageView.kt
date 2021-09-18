package com.ziwenl.library

import android.content.Context
import android.hardware.SensorManager
import android.util.AttributeSet
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs

/**
 * PackageName : com.ziwenl.library
 * Author : Ziwen Lan
 * Date : 2021/9/18
 * Time : 14:13
 * Introduction : 重力感应 ImageView
 * 通过从 GravityRotationHelper 处得到的磁场及加速度传感器数据，计算设备倾斜角度进行移动当前 view
 */
class GravityRotationImageView : AppCompatImageView {

    companion object {
        /**
         * 最大旋转角度
         */
        private const val MAX_ROTATION_ANGLE = 60
        private const val MAX_ROTATION_ANGLE_Y = 100

        /**
         * 默认可移动范围 20dp
         */
        internal const val MOVING_RANGE_DEFAULT = 20f

        /**
         * 角度变化响应值范围，通过该范围来过滤抖动与跳动
         */
        private const val RESPONSE_ANGLE_CHANGE_MIN = 10
        private const val RESPONSE_ANGLE_CHANGE_MAX = 35
    }


    /**
     * 最大可移动范围（前景后景的可移动范围）
     */
    private var mMaxMovingRange = 0

    /**
     * 移动方向
     */
    private var mDirection = -1


    /**
     * 分别记录沿 X、Y轴旋转角度
     * mRotationAngleX ：沿 X 轴旋转角度，手机正面往上竖起来时，值越小
     * mRotationAngleY ：沿 Y 轴旋转角度，手机正面往右竖起来时，值越小
     */
    internal var rotationAngleX = 0
    internal var rotationAngleY = 0

    /**
     * 滚动辅助对象
     */
    private lateinit var mScroller: Scroller


    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs, defStyleAttr)
    }

    private fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        mScroller = Scroller(context)
        mMaxMovingRange = dip2px(context, MOVING_RANGE_DEFAULT)
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.GravityRotationImageView,
                defStyleAttr,
                0
            )
        val isBack = typedArray.getBoolean(R.styleable.GravityRotationImageView_isBack, false)
        isBack(isBack)
        typedArray.recycle()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.currX, mScroller.currY)
            invalidate()
        }
    }

    /**
     * 平滑移动 view
     */
    private fun smoothScroll(dx: Int, dy: Int) {
        mScroller.startScroll(scrollX, scrollY, dx, dy, 650)
        invalidate()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 设置当前 view 为前景或后景
     * @param isBack true 后景 ; false 前景
     */
    fun isBack(isBack: Boolean) {
        /**
         * 判断该 view 用作前景还是后景
         * 后景则需调整放大倍数使内容滚动时不会出现白边
         * 并根据前后景记录对应的滚动方向
         */
        if (isBack) {
            mDirection = 1
            scaleType = ScaleType.CENTER_CROP
            scaleX = 1.1f
            scaleY = 1.2f
        } else {
            mDirection = -1
        }
    }

    /**
     * 处理传感器得到的数据，过滤后再根据倾斜角度移动当前 view
     * 旋转移动过程中，前景后景随旋转角度偏移
     */
    internal fun handleSensorChangedValues(
        gravity: FloatArray,
        geomagnetic: FloatArray,
        maxMovingRange: Float = MOVING_RANGE_DEFAULT
    ) {
        if (maxMovingRange != MOVING_RANGE_DEFAULT) {
            mMaxMovingRange = dip2px(this.context, maxMovingRange)
        }
        //旋转角度值集
        val orientationValues = FloatArray(3)
        //旋转矩阵
        val rotationMatrix = FloatArray(9)
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            gravity,
            geomagnetic
        )
        SensorManager.getOrientation(rotationMatrix, orientationValues)
        // z 轴的偏转角度
        orientationValues[0] = Math.toDegrees(orientationValues[0].toDouble()).toFloat()
        // x 轴的偏转角度
        orientationValues[1] = Math.toDegrees(orientationValues[1].toDouble()).toFloat()
        // y 轴的偏转角度
        orientationValues[2] = Math.toDegrees(orientationValues[2].toDouble()).toFloat()
        val newAngleX = orientationValues[1].toInt()
        val newAngleY = orientationValues[2].toInt()
        // x 、 y 轴角度变化值
        val rotationAngleXChangeValue = abs(newAngleX - rotationAngleX)
        val rotationAngleYChangeValue = abs(newAngleY - rotationAngleY)
        var targetX = mScroller.finalX
        var targetY = mScroller.finalY
        if (rotationAngleYChangeValue in (RESPONSE_ANGLE_CHANGE_MIN + 1) until RESPONSE_ANGLE_CHANGE_MAX
            || rotationAngleXChangeValue in (RESPONSE_ANGLE_CHANGE_MIN + 1) until RESPONSE_ANGLE_CHANGE_MAX
        ) {
            if (newAngleX <= 0 && newAngleX > -MAX_ROTATION_ANGLE || newAngleX in 1 until MAX_ROTATION_ANGLE) {
                targetY = mMaxMovingRange * -mDirection * newAngleX / MAX_ROTATION_ANGLE_Y
            }
            if (newAngleY <= 0 && newAngleY > -MAX_ROTATION_ANGLE || newAngleY in 1 until MAX_ROTATION_ANGLE) {
                targetX = mMaxMovingRange * mDirection * newAngleY / MAX_ROTATION_ANGLE
            }
            val dx = targetX - scrollX
            val dy = targetY - scrollY
            smoothScroll(dx, dy)
            //更新角度
            rotationAngleX = newAngleX
            rotationAngleY = newAngleY
        }
    }
}