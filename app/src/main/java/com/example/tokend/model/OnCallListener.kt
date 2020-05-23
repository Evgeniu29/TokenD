package com.example.tokend.model

interface OnCallListener<T> {

    fun onCall(t: T)

    fun onMessage(t: T)
}