package vn.teko.databindingissue

import java.text.DecimalFormat
import java.util.*

object TextUtils {

    fun getNumber(amount: String) : Long? {
        return amount.replace(".", "").toLongOrNull()
    }

    fun formatNumber(amount: Long): String {
        val locale = Locale("vi", "VN")
        val formatter = DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        val symbol = formatter.decimalFormatSymbols
        symbol.currencySymbol = ""
        formatter.decimalFormatSymbols = symbol

        return formatter.format(amount).trim()
    }

}