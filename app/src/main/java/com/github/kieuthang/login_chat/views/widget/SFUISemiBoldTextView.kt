package com.github.kieuthang.login_chat.views.widget


import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class SFUISemiBoldTextView : TextView {
    constructor(context: Context) : super(context) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setFont()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "fonts/sfuitext_semibold.ttf")
        setTypeface(font, Typeface.NORMAL)
    }
}