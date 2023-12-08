package com.ab.hicareservices.ui.handler

interface onAddressClickedHandler {
    fun setonaddclicklistener(position: Int, id: Int?, b: Boolean)
     fun setradiobuttonclicklistern(position: Int)
    fun setItemClickLister(
        position: Int,
        id: Int?,
        b: Boolean,
        toString: String,
        toString1: String,
        toString2: String
    )
}