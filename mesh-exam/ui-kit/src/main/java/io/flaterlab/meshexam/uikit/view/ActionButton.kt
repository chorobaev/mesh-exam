package io.flaterlab.meshexam.uikit.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity.CENTER
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.google.android.material.card.MaterialCardView
import io.flaterlab.meshexam.uikit.R
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import io.flaterlab.meshexam.uikit.ext.obtainStyledAttributes

class ActionButton : MaterialCardView {

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleRes
    ) {
        init(attrs, defStyleRes)
    }

    private val imageView = ImageView(context)

    private fun init(attrs: AttributeSet?, defStyleRes: Int = 0) {
        var imageRes = 0

        attrs.obtainStyledAttributes(context, R.styleable.ActionButton, defStyleRes) {
            imageRes = it.getResourceId(R.styleable.ActionButton_android_src, 0)
        }

        setupCard()
        setupImage()
        setImageResource(imageRes)
        addView(imageView)
    }

    private fun setupCard() {
        val side: Int = resources.getDimension(R.dimen.action_button_width).toInt()
        minimumHeight = side
        minimumWidth = side
        radius = resources.getDimension(R.dimen.radius_medium)
        setCardBackgroundColor(context.getColorAttr(R.attr.colorPrimary))
    }

    private fun setupImage() = with(imageView) {
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
            gravity = CENTER
        }
        val padding: Int = resources.getDimension(R.dimen.margin_small).toInt()
        setPadding(padding, padding, padding, padding)
    }

    fun setImageResource(@DrawableRes imageRes: Int) {
        imageView.setImageResource(imageRes)
    }

    fun setImageDrawable(drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }
}