package vn.teko.databindingissue

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.*
import androidx.databinding.adapters.ListenerUtil

@InverseBindingMethods(
    InverseBindingMethod(
        type = CommonInput::class,
        attribute = "inputText"
    )
)
object CommonInputBindingAdapter {

    @JvmStatic
    @BindingAdapter("onInputTextChange", "inputTextAttrChanged", requireAll = false)
    fun setInputTextListener(
        commonInput: CommonInput,
        onInputTextChange: CommonInput.OnInputTextChangeListener?,
        textAttrChanged: InverseBindingListener?
    ) {
        if (textAttrChanged == null) {
            commonInput.setOnInputTextChangedListener(onInputTextChange)
        } else {
            commonInput.setOnInputTextChangedListener(object :
                CommonInput.OnInputTextChangeListener {
                override fun onValueChange(picker: CommonInput?, oldVal: String, newVal: String) {
                    onInputTextChange?.onValueChange(picker, oldVal, newVal)
                    textAttrChanged.onChange()
                }
            })
        }
    }

    @JvmStatic
    @BindingAdapter("inputText")
    fun setInputText(commonInput: CommonInput, text: String?) {
        // prevent loop
        if (commonInput.getInputText() != text) {
            commonInput.setInputText(text)
        }
    }

    @JvmStatic
    @BindingAdapter("money")
    fun setMoney(view: EditText, money: String? = null) {
        // prevent loop
        if (view.text.toString() != money) {
            view.setText(money)
            view.setSelection(money?.length ?: 0)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "money", event = "moneyAttrChanged")
    fun getMoney(view: EditText): String = view.text.toString()

    @JvmStatic
    @BindingAdapter("moneyAttrChanged")
    fun setInputTextListener(
        view: EditText,
        textAttrChanged: InverseBindingListener?
    ) {
        val newValue: TextWatcher? =
            if (textAttrChanged == null) {
                null
            } else {
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        textAttrChanged.onChange()
                    }

                }
            }

        val oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher)
        if (oldValue != null)
            view.removeTextChangedListener(oldValue)

        if (newValue != null) {
            view.addTextChangedListener(newValue)
        }
    }

}