package com.haruki.kaopifeatharuki.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.widget.ImageView
import com.google.android.material.R
import com.google.android.material.imageview.ShapeableImageView
import com.haruki.kaopifeatharuki.util.dp

@Deprecated("实现出来效果不好，还是用图片作为图框吧")
class RainbowShapedImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
//        private val DEF_STYLE_RES: Int = R.style.Widget_MaterialComponents_ShapeableImageView
    }

    private var strokeWidth = 0f
    private var borderPaint:Paint
    private var borderRect = RectF()
    private val rainbowColors =  intArrayOf(
        Color.RED,
        Color.rgb(255, 100, 0), // 红橙
        Color.rgb(255, 150, 0), // 橙
        Color.rgb(255, 200, 0), // 橙黄
        Color.YELLOW,
        Color.rgb(200, 255, 0), // 黄绿
        Color.GREEN,
        Color.rgb(0, 255, 150), // 绿青
        Color.CYAN,
        Color.rgb(0, 200, 255), // 青蓝
        Color.BLUE,
        Color.rgb(100, 0, 255), // 蓝紫
        Color.MAGENTA,
        Color.rgb(255, 0, 200), // 紫粉
        Color.rgb(255, 0, 150), // 粉红
        Color.rgb(255, 0, 100), // 红粉
        Color.RED, // 闭环
        Color.rgb(255, 100, 0), // 二次过渡
        Color.YELLOW  // 平滑连接
    )

    private val positions = floatArrayOf(
    0.0f, 0.1f, 0.15f, 0.2f, 0.25f,
    0.3f, 0.35f, 0.4f, 0.45f, 0.5f,
    0.55f, 0.6f, 0.65f, 0.7f, 0.75f,
    0.8f, 0.85f, 0.9f,1.0f
    )

    init {
//        val attributes =
//            context.obtainStyledAttributes(
//                attrs, R.styleable.ShapeableImageView, defStyleAttr, DEF_STYLE_RES
//            )

        strokeWidth = 2.dp

        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            this.strokeWidth = this@RainbowShapedImageView.strokeWidth
        }
        borderPaint.color = Color.RED

    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        // 计算描边区域（向内缩进一半描边宽度）
        val halfBorder = strokeWidth / 2
        borderRect.set(
            halfBorder,
            halfBorder,
            width - halfBorder,
            height - halfBorder
        )

        borderPaint.shader = SweepGradient(
            width / 2f,
            height / 2f,
            rainbowColors,
            positions
        )
    }

    override fun onDraw(canvas: Canvas) {
        // 先绘制彩虹边框
        canvas.drawRect(borderRect, borderPaint)
        // 再绘制图片
        super.onDraw(canvas)
    }
}