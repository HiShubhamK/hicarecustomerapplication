package com.ab.hicareservices.ui.handler

import android.widget.ImageView

interface onCartClickedHandler {
    fun setondeleteclicklistener(position: Int, cartId: Int?, userId: Int?)
    fun setonaddclicklistener(position: Int, productid: Int, i: Int, imgadd: ImageView)
}