package io.flaterlab.meshexam.uikit.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.view.setPadding
import io.flaterlab.meshexam.uikit.R

class BottomSheetDialogTopView : ImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int) :
            super(context, attrs, defStyleRes)

    init {
        setImageResource(R.drawable.ic_dialog_handler)
        val padding = resources.getDimension(R.dimen.bottom_sheet_dialog_handler_padding).toInt()
        setPadding(padding)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                resources.getDimension(R.dimen.bottom_sheet_dialog_handler_height).toInt(),
                MeasureSpec.EXACTLY
            )
        )
    }
}