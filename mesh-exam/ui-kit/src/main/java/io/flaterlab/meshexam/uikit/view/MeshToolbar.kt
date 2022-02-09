package io.flaterlab.meshexam.uikit.view

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import io.flaterlab.meshexam.uikit.R
import io.flaterlab.meshexam.uikit.databinding.ViewMeshToolbarBinding
import io.flaterlab.meshexam.uikit.ext.activity
import io.flaterlab.meshexam.uikit.ext.getColorAttr
import io.flaterlab.meshexam.uikit.ext.getColorCompat
import io.flaterlab.meshexam.uikit.ext.obtainStyledAttributes


class MeshToolbar : AppBarLayout {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    val toolbar get() = binding.idViewMeshToolbar

    private var subtitle: String? = null
    private lateinit var binding: ViewMeshToolbarBinding

    private fun init(attrs: AttributeSet? = null) {
        binding = ViewMeshToolbarBinding.inflate(LayoutInflater.from(context), this, false)
        addView(binding.root, 0)

        var title = ""
        var titleColor = 0
        var toolbarBackground = 0

        elevation = 0F
        targetElevation = 0F

        attrs.obtainStyledAttributes(context, R.styleable.MeshToolbar) {
            title = it.getString(R.styleable.MeshToolbar_toolbarTitle) ?: ""
            titleColor = it.getColor(
                R.styleable.MeshToolbar_toolbarTitleColor, context.getColorAttr(
                    R.attr.colorOnBackground
                )
            )
            toolbarBackground = it.getColor(
                R.styleable.MeshToolbar_toolbarBackground, context.getColorAttr(
                    android.R.attr.colorBackground
                )
            )
        }

        setBackgroundColor(toolbarBackground)
        toolbar.setTitleTextColor(titleColor)
        setTitle(title)
    }

    fun setTitleTextColor(titleColor: Int?) {
        titleColor?.apply {
            toolbar.setTitleTextColor(context.getColorCompat(titleColor))
        } ?: toolbar.setTitleTextColor(context.getColorAttr(R.attr.colorOnBackground))
    }

    fun setNavigationIcon(@DrawableRes icon: Int?) {
        icon?.apply { toolbar.setNavigationIcon(icon) }
            ?: toolbar.setNavigationIcon(R.drawable.ic_back)
    }

    fun setOnBackButtonClickListener(onClick: (View) -> Unit) {
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener(onClick)
    }

    fun hideBackButton() {
        toolbar.navigationIcon = null
    }

    fun setTitle(title: String) {
        toolbar.title = title
    }

    fun setTitle(@StringRes title: Int) {
        toolbar.title = context.getString(title)
    }

    fun setSubtitle(subtitle: String) {
        this.subtitle = subtitle
        showSubtitle()
    }

    fun showSubtitle() {
        toolbar.layoutTransition = LayoutTransition()
        toolbar.subtitle = subtitle
    }

    fun hideSubtitle() {
        toolbar.layoutTransition = null
        toolbar.subtitle = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (activity != null && activity?.supportActionBar == null) {
            setupToolbar(
                text = toolbar.title.toString()
            )
        }
    }

    fun setupToolbar(
        text: String? = null,
        @DrawableRes icon: Int? = R.drawable.ic_back,
        onNavigationClickListener: () -> Unit = { activity?.onBackPressed() },
    ) {
        setupToolbar(
            toolbar,
            activity,
            text,
            icon,
            onNavigationClickListener
        )

        text?.let { setTitle(it) }
    }

    companion object {
        /**
         * Convenient method to setup toolbar if it's inside fragment
         * @param toolbar fragment toolbar
         * @param text toolbar title text
         * @param icon toolbar navigation icon
         * @param onNavigationClickListener listener of toolbar navigation button
         * @param vToolbarTextView if toolbar layouts have textView for title centered
         */
        fun setupToolbar(
            toolbar: Toolbar,
            activity: AppCompatActivity?,
            text: String? = null,
            @DrawableRes icon: Int? = R.drawable.ic_back,
            onNavigationClickListener: () -> Unit = { activity?.onBackPressed() },
            vToolbarTextView: AppCompatTextView? = null,
        ) {
            activity?.apply {
                activity.supportActionBar?.apply {
                    setDisplayShowTitleEnabled(false)
                    setDisplayShowHomeEnabled(true)
                }
            }

            text?.apply {
                if (vToolbarTextView != null) {
                    vToolbarTextView.text = text
                } else {
                    toolbar.title = text
                }
            }

            if (icon != null) {
                toolbar.setNavigationIcon(icon)
            } else {
                toolbar.navigationIcon = null
            }
            toolbar.setNavigationOnClickListener { onNavigationClickListener() }
        }
    }
}