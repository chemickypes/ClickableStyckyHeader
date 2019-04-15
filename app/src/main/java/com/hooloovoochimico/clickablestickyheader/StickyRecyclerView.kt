package com.hooloovoochimico.clickablestickyheader

import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent

class StickyRecyclerView: RecyclerView, GestureDetector.OnGestureListener {


    constructor(context: Context) : super(context){
        gestureDetector = GestureDetectorCompat(context,this)
    }
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet){
        gestureDetector = GestureDetectorCompat(context,this)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyle:Int): super(context,attributeSet,defStyle){
        gestureDetector = GestureDetectorCompat(context,this)
    }

    private var recyclerSticky : RecyclerSticky? = null

    private var gestureDetector: GestureDetectorCompat? = null


    fun setRecyclerStiky(madapter:AdapterStickyHeader?) {
        madapter?.let {
            adapter = it
            recyclerSticky = RecyclerSticky(it)
            addItemDecoration(recyclerSticky!!)
        }?: kotlin.run {
            try {
                recyclerSticky?.let {
                    removeItemDecoration(it)
                }

                recyclerSticky = null
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    fun removeRecyclerSticky(){
        setRecyclerStiky(null)
    }


    override fun onTouchEvent(e: MotionEvent?): Boolean {
        performClick()
        gestureDetector?.onTouchEvent(e)
        return super.onTouchEvent(e)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        recyclerSticky?.propagateTap()
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean  = true

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean  = true

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean  = true

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {

        return recyclerSticky?.let {

            if(e?.y?:0.0f in x..(x+(recyclerSticky?.mStickyHeaderHeight?:0))){
                true
            }else {
                super.onInterceptTouchEvent(e)
            }
        }?: kotlin.run {
            return super.onInterceptTouchEvent(e)
        }

    }
}

abstract class AdapterStickyHeader : RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerSticky.StickyHeaderInterface
