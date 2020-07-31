package vn.teko.databindingissue

import android.content.Context
import android.util.TypedValue

internal fun Context.dpToPx(dp: Float): Float {
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}

internal fun Context.getScreenHeight(): Int {
  return resources.displayMetrics.heightPixels
}

internal fun Context.spToPx(sp: Float): Float {
  return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
}