package com.ab.hicareservices.ui.handler

interface ValidateAccountListener {
    fun onSuccess(data: String)
    fun onError(message: String)
}