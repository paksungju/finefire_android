//package com.finefire.finefire.util
//
//import android.animation.Animator
//import android.animation.AnimatorListenerAdapter
//import android.animation.ValueAnimator
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Matrix
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.opengl.ETC1
//import android.util.AttributeSet
//import androidx.appcompat.widget.AppCompatImageView
//import java.util.*
//
//
//class AnimatedImageView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
//    AppCompatImageView(context, attrs, defStyleAttr) {
//    // Listener values;
//    private var mFromScaleType: ScaleType
//    private var mToScaleType: ScaleType
//    private val mValueAnimator: ValueAnimator?
//    private var mStartDelay = 0
//    private var isViewLayedOut = false
//
//    // Constructors
//    constructor(context: Context?) : this(context, null) {}
//
//    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}
//
//    /**
//     * Sets the scale type we want to animate to
//     *
//     * @param toScaleType
//     */
//    fun setAnimatedScaleType(toScaleType: ScaleType) {
//        mToScaleType = toScaleType
//    }
//
//    /**
//     * Duration of the animation
//     *
//     * @param animationDuration
//     */
//    fun setAnimDuration(animationDuration: Int) {
//        mValueAnimator!!.duration = animationDuration.toLong()
//    }
//
//    /**
//     * Set the time delayed for the animation
//     *
//     * @param startDelay The delay (in milliseconds) before the animation
//     */
//    fun setStartDelay(startDelay: Int?) {
//        mStartDelay = startDelay ?: 0
//    }
//
//    override fun setScaleType(scaleType: ScaleType) {
//        super.setScaleType(scaleType)
//        mFromScaleType = scaleType
//    }
//
//    override fun setImageDrawable(drawable: Drawable?) {
//        super.setImageDrawable(drawable)
//        updateScaleType(mFromScaleType, false)
//    }
//
//    override fun setImageBitmap(bm: Bitmap?) {
//        super.setImageBitmap(bm)
//        updateScaleType(mFromScaleType, false)
//    }
//
//    override fun setImageResource(resId: Int) {
//        super.setImageResource(resId)
//        updateScaleType(mFromScaleType, false)
//    }
//
//    fun setImageURI(uri: Uri?) {
//        super.setImageURI(uri)
//        updateScaleType(mFromScaleType, false)
//    }
//
//    /**
//     * Animates the current view
//     * and updates it's current asset
//     */
//    fun startAnimation() { // This will run the animation with a delay (delay is defaulted at 0)
//        postDelayed(startDelay, mStartDelay.toLong())
//    }
//
//    override fun onLayout(
//        changed: Boolean,
//        left: Int,
//        top: Int,
//        right: Int,
//        bottom: Int
//    ) {
//        super.onLayout(changed, left, top, right, bottom)
//        // View has been laid out
//        isViewLayedOut = true
//        // Animate change when bounds are official
//        if (changed) {
//            updateScaleType(mToScaleType, false)
//        }
//    }
//
//    /**
//     * animate to scaleType
//     *
//     * @param toScaleType
//     */
//    private fun updateScaleType(
//        toScaleType: ScaleType,
//        animated: Boolean
//    ) { // Check if view is laid out
//        if (!isViewLayedOut) {
//            return
//        }
//        // Cancel value animator if its running
//        if (mValueAnimator != null && mValueAnimator.isRunning) {
//            mValueAnimator.cancel()
//            mValueAnimator.removeAllUpdateListeners()
//        }
//        // Set the  scale type
//        super.setScaleType(mFromScaleType)
//        // Get values for the current image matrix
//        setFrame(left, top, right, bottom)
//        val srcMatrix: Matrix = imageMatrix
//        val srcValues = FloatArray(9)
//        val destValues = FloatArray(9)
//        srcMatrix.getValues(srcValues)
//        // Set the scale type to the new type
//        super.setScaleType(toScaleType)
//        setFrame(left, top, right, bottom)
//        val destMatrix: Matrix = imageMatrix
//        if (toScaleType == ScaleType.FIT_XY) {
//            val sX =
//                ETC1.getWidth().toFloat() / drawable.intrinsicWidth
//            val sY =
//                ETC1.getHeight().toFloat() / drawable.intrinsicHeight
//            destMatrix.postScale(sX, sY)
//        }
//        destMatrix.getValues(destValues)
//        // Get translation values
//        val transX = destValues[2] - srcValues[2]
//        val transY = destValues[5] - srcValues[5]
//        val scaleX = destValues[0] - srcValues[0]
//        val scaleY = destValues[4] - srcValues[4]
//        // Set the scale type to a matrix
//        super.setScaleType(ScaleType.MATRIX)
//        // Listen to value animator changes
//        mValueAnimator!!.addUpdateListener { animation ->
//            val value = animation.animatedFraction
//            val currValues: FloatArray = Arrays.copyOf(srcValues, srcValues.size)
//            currValues[2] = srcValues[2] + transX * value
//            currValues[5] = srcValues[5] + transY * value
//            currValues[0] = srcValues[0] + scaleX * value
//            currValues[4] = srcValues[4] + scaleY * value
//            val matrix = Matrix()
//            matrix.setValues(currValues)
//            imageMatrix = matrix
//        }
//        // Save the newly set scale type after animation completes
//        mValueAnimator.addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator) {
//                super.onAnimationEnd(animation)
//                // Set the from scale type to the newly used scale type
//                mFromScaleType = toScaleType
//            }
//        })
//        // Start the animation
//        if (animated) {
//            mValueAnimator.start()
//        }
//    }
//
//    init {
//        // Set default original scale type
//        mFromScaleType = scaleType
//        // Set default scale type for animation
//        mToScaleType = scaleType
//        // Init value animator
//        mValueAnimator = ValueAnimator.ofFloat(0f, 1f)
//        // Set resource
//        updateScaleType(mFromScaleType, false)
//    }
//}