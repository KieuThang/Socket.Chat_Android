package com.github.kieuthang.login_chat.views.common

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.github.kieuthang.login_chat.common.utils.ViewPressEffectHelper
import com.github.kieuthang.login_chat.views.widget.SFUIButton
import com.github.kieuthang.login_chat.views.widget.SanfranciscoMediumTextView
import com.github.kieuthang.login_chat.R


class PopupDialogFragment : DialogFragment() {
    private var mBuilder: Builder? = null
    private var mListener: IPopupDialogFragment? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = activity.layoutInflater.inflate(R.layout.layout_dialog_popup, null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(v)
        builder.setTitle("")
        builder.setCancelable(mBuilder!!.isCancelable)

        val icDialog = v.findViewById<ImageView>(R.id.ic_dialog)
        if (mBuilder!!.mIcon == 0 && type != Type.ERROR)
            icDialog.visibility = View.GONE
        else {
            if (type == Type.ERROR || mBuilder!!.mIcon != 0) {
                icDialog.visibility = View.VISIBLE
                if (mBuilder!!.mIcon == 0) {
                    mBuilder!!.mIcon = R.drawable.ic_dialog_error
                }
                icDialog.setImageResource(mBuilder!!.mIcon!!)
            }
        }

        val tvDialogContent = v.findViewById<SanfranciscoMediumTextView>(R.id.tv_dialog_content)
        val tvDialogSubContent = v.findViewById<SanfranciscoMediumTextView>(R.id.tv_sub_content)
        tvDialogContent.text = mBuilder!!.mContent
        if (type == Type.ERROR) {
            tvDialogContent.setTextColor(activity.resources.getColor(R.color.colorDongleInvalid))
        }

        tvDialogSubContent.text = mBuilder!!.mSubContent
        if (TextUtils.isEmpty(mBuilder!!.mSubContent))
            tvDialogSubContent.visibility = View.GONE

        val btnPositive = v.findViewById<SFUIButton>(R.id.btn_positive)
        btnPositive.text = mBuilder!!.positiveText
        btnPositive.setOnClickListener {
            dismiss()
            if (mListener == null)
                return@setOnClickListener
            mListener!!.clickPositiveText(mBuilder!!.requestCode)
        }
        ViewPressEffectHelper.attach(btnPositive)
        if (TextUtils.isEmpty(mBuilder!!.positiveText)) {
            btnPositive.visibility = View.GONE
        }

        val btnNegative = v.findViewById<SFUIButton>(R.id.btn_negative)
        if (TextUtils.isEmpty(mBuilder!!.negativeText)) {
            btnNegative.visibility = View.GONE
        }
        ViewPressEffectHelper.attach(btnNegative)
        btnNegative.text = mBuilder!!.negativeText
        btnNegative.setOnClickListener {
            dismiss()
            if (mListener == null)
                return@setOnClickListener
            mListener!!.clickNegativeText(mBuilder!!.requestCode)
        }

        return builder.create()
    }

    companion object Builder {
        var mContent: String? = null
        var mSubContent: String? = null
        var mIcon: Int? = 0
        var positiveText: String? = null
        var negativeText: String? = null
        var requestCode: Int = 0
        var isCancelable: Boolean = false
        var type: Type? = null
    }

    enum class Type {
        NOTIFY, WARNING, ERROR, NOTIFY_YES_NO
    }

    fun setBuilder(mBuilder: Builder) {
        this.mBuilder = mBuilder
    }

    fun setListener(listener: IPopupDialogFragment?) {
        this.mListener = listener
    }
}