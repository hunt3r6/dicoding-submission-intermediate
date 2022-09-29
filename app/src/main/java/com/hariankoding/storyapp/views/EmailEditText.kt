package com.hariankoding.storyapp.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.hariankoding.storyapp.R

class EmailEditText : AppCompatEditText {

    private lateinit var bgEditText: Drawable
    private lateinit var bgErrorEditText: Drawable
    private var isError: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        bgEditText = ContextCompat.getDrawable(context, R.drawable.bg_edit_text) as Drawable
        bgErrorEditText =
            ContextCompat.getDrawable(context, R.drawable.bg_error_edit_text) as Drawable
        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        setHint(R.string.email)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && !Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    isError = true
                    error = context.getString(R.string.error_email)
                } else {
                    isError = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        background = if (!isError) bgEditText else bgErrorEditText
    }

}