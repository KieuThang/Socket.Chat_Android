package com.github.kieuthang.login_chat.views.widget


import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.TextInputEditText
import android.util.AttributeSet

class SanfranciscoRegularEditText : TextInputEditText {
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
        val font = Typeface.createFromAsset(context.assets, "fonts/sanfrancisco_regular.otf")
        setTypeface(font, Typeface.NORMAL)
    }
}
