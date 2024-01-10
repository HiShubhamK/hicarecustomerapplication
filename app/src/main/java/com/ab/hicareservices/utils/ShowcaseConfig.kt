//package com.ab.hicareservices.utils
//
//import android.graphics.Color
//import android.graphics.Typeface
//import android.graphics.drawable.shapes.Shape
//
//class ShowcaseConfig {
//    val DEFAULT_MASK_COLOUR = "#dd335075"
//
//
//    private var mDelay: Long = -1
//    private var mMaskColour = 0
//    private var mDismissTextStyle: Typeface? = null
//
//    private var mContentTextColor = 0
//    private var mDismissTextColor = 0
//    private var mFadeDuration: Long = -1
//    private var mShape: Shape? = null
//    private var mShapePadding = -1
//    private var renderOverNav: Boolean? = null
//
//    fun ShowcaseConfig() {
//        mMaskColour = Color.parseColor(DEFAULT_MASK_COLOUR)
//        mContentTextColor = Color.parseColor("#ffffff")
//        mDismissTextColor = Color.parseColor("#ffffff")
//    }
//
//    fun getDelay(): Long {
//        return mDelay
//    }
//
//    fun setDelay(delay: Long) {
//        mDelay = delay
//    }
//
//    fun getMaskColor(): Int {
//        return mMaskColour
//    }
//
//    fun setMaskColor(maskColor: Int) {
//        mMaskColour = maskColor
//    }
//
//    fun getContentTextColor(): Int {
//        return mContentTextColor
//    }
//
//    fun setContentTextColor(mContentTextColor: Int) {
//        this.mContentTextColor = mContentTextColor
//    }
//
//    fun getDismissTextColor(): Int {
//        return mDismissTextColor
//    }
//
//    fun setDismissTextColor(dismissTextColor: Int) {
//        mDismissTextColor = dismissTextColor
//    }
//
//    fun getDismissTextStyle(): Typeface? {
//        return mDismissTextStyle
//    }
//
//    fun setDismissTextStyle(dismissTextStyle: Typeface?) {
//        mDismissTextStyle = dismissTextStyle
//    }
//
//    fun getFadeDuration(): Long {
//        return mFadeDuration
//    }
//
//    fun setFadeDuration(fadeDuration: Long) {
//        mFadeDuration = fadeDuration
//    }
//
//    fun getShape(): Shape? {
//        return mShape
//    }
//
//    fun setShape(shape: Shape?) {
//        mShape = shape
//    }
//
//    fun setShapePadding(padding: Int) {
//        mShapePadding = padding
//    }
//
//    fun getShapePadding(): Int {
//        return mShapePadding
//    }
//
//    fun getRenderOverNavigationBar(): Boolean? {
//        return renderOverNav
//    }
//
//    fun setRenderOverNavigationBar(renderOverNav: Boolean) {
//        this.renderOverNav = renderOverNav
//    }
//}