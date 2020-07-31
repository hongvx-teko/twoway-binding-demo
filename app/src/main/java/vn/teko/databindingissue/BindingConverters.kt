package vn.teko.databindingissue

import android.view.View
import androidx.databinding.BindingConversion
import androidx.databinding.InverseMethod

object BindingConverters {

    @BindingConversion
    @JvmStatic
    fun booleanToVisibility(isVisible: Boolean): Int {
        return if (isVisible) View.VISIBLE else View.GONE
    }

    @InverseMethod("formattedMoneyToLong")
    @JvmStatic
    fun longToFormattedMoney(value: Long): String {
        // convert long to formatted money
        return TextUtils.formatNumber(value)
    }

    @JvmStatic
    fun formattedMoneyToLong(value: String): Long {
        // convert formatted money to long
        return TextUtils.getNumber(value) ?: 0L
    }
}