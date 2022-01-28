package io.flaterlab.meshexam.uikit.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.flaterlab.meshexam.uikit.R

class Divider : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) :
            super(context, attrs, defStyleRes)

    init {
        setBackgroundResource(R.drawable.background_divider)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = resources.getDimension(R.dimen.divider_height).toInt()
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        )
    }
}