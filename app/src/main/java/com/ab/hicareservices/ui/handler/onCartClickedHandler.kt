package com.ab.hicareservices.ui.handler

interface onCartClickedHandler {
    fun setondeleteclicklistener(position: Int, cartId: Int?, userId: Int?)
    fun setonaddclicklistener(position: Int, productid: Int, i: Int)
}