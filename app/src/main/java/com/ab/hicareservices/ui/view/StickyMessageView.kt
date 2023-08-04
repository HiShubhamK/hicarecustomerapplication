package com.ab.hicareservices.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.ab.hicareservices.R

class StickyMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val messageTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_sticky_message, this, true)
        messageTextView = findViewById(R.id.stickyMessageTextView)
    }

    fun setMessage(message: String) {
        messageTextView.text = message
    }
}