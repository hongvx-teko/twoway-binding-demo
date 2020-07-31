package vn.teko.databindingissue

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.core.widget.ImageViewCompat
import androidx.core.widget.TextViewCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.airbnb.paris.Paris

class CommonInput
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val ONLY_TEXT = 0
    private val INCLUDE_ACTION_ICON = 1
    private val ONLY_TEXT_WITH_LIMIT = 2
    private val INCLUDE_ACTION_ICON_WITH_LIMIT = 3

    private var inputHint: CharSequence = ""
    private var inputText: CharSequence = ""
    private var maxLength: Int = 100
    private var displayMode: Int = 0
    private var drawableTintColor: Int = 0
    private var drawableStateListColor: ColorStateList? = null
    private var actionIconRes: Int = 0
    private var inputStyleRes: Int = 0
    private var actionIconTintColor: Int = -1
    private var showActionIcon: Boolean = true
    private var inputEnabled: Boolean = true

    private var mText: EditText
    private var mActionIcon: ImageView
    private var mLengthLimit: TextView

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.common_input, this, true)
        mText = root.findViewById(R.id.input)
        mActionIcon = root.findViewById(R.id.actionIcon)
        mLengthLimit = root.findViewById(R.id.lengthLimit)

        val attr = context.obtainStyledAttributes(attrs, R.styleable.CommonInput, defStyleAttr, 0)
        inputText = attr.getString(R.styleable.CommonInput_inputText) ?: inputText
        inputHint = attr.getString(R.styleable.CommonInput_inputHint) ?: inputHint
        showActionIcon = attr.getBoolean(R.styleable.CommonInput_showActionIcon, showActionIcon)
        maxLength = attr.getInteger(R.styleable.CommonInput_inputMaxLength, maxLength)
        displayMode = attr.getInt(R.styleable.CommonInput_displayMode, displayMode)
        drawableTintColor =
            attr.getColor(R.styleable.CommonInput_drawableTintColor, drawableTintColor)
        drawableStateListColor =
            attr.getColorStateList(R.styleable.CommonInput_drawableStateListColor)
        actionIconRes = attr.getResourceId(R.styleable.CommonInput_actionIcon, actionIconRes)
        actionIconTintColor =
            attr.getColor(R.styleable.CommonInput_actionIconTintColor, actionIconTintColor)
        inputStyleRes = attr.getResourceId(R.styleable.CommonInput_inputStyle, inputStyleRes)
        inputEnabled = attr.getBoolean(R.styleable.CommonInput_inputEnabled, true)
        attr.recycle()

        updateMaxLength()
        updateDisplayMode()
        updateDrawableTintColor()
        updateDrawableStateListColor()
        updateActionIcon()
        updateActionIconTintColor()
        updateInputText()
        updateInputHint()
        updateInputStyle()
        updateInputTextEnabled()

        mText.doOnTextChanged { text, start, count, after ->
            val old = inputText
            inputText = text.toString()
            mOnValueChangeListener?.onValueChange(this, old.toString(), text.toString())
        }
//        mText.doAfterTextChanged { text ->
//            val len = text?.length ?: 0
//            mLengthLimit.text = "${len}/$maxLength"
//            mLengthLimit.setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    if (len == maxLength) R.color.system_error else R.color.neutral__placeholder
//                )
//            )
//        }
    }

    /**
     * Sets the listener to be notified on change of the current value.
     *
     * @param onValueChangedListener The listener.
     */
    fun setOnInputTextChangedListener(onValueChangedListener: OnInputTextChangeListener?) {
        mOnValueChangeListener = onValueChangedListener;
    }

    private var mOnValueChangeListener: OnInputTextChangeListener? = null

    /**
     * Interface to listen for changes of the current value.
     */
    interface OnInputTextChangeListener {
        /**
         * Called upon a change of the current value.
         *
         * @param picker The NumberPicker associated with this listener.
         * @param oldVal The previous value.
         * @param newVal The new value.
         */
        fun onValueChange(picker: CommonInput?, oldVal: String, newVal: String)
    }


    fun getInputText() = inputText

    fun getInputType() = mText.inputType

    fun setInputType(inputType: Int) {
        mText.inputType = inputType
        mText.setSelection(getInputText().length)
    }

    private fun setMaxLength(length: Int) {
        if (this.maxLength != length) {
            this.maxLength = length
            updateMaxLength()
        }
    }

    private fun updateMaxLength() {
        val lengthFilter = if (maxLength >= 0) {
            arrayOf<InputFilter>(LengthFilter(maxLength))
        } else {
            arrayOf()
        }
        mText.filters = lengthFilter
        mLengthLimit.text = "0/$maxLength"
        mLengthLimit.isVisible = displayMode > 3
    }

    fun setInputHint(hint: CharSequence?) {
        if (this.inputHint != hint) {
            this.inputHint = hint ?: ""
            updateInputHint()
        }
    }

    private fun updateInputHint() {
        mText.hint = inputHint
    }

    fun setInputEnabled(enable: Boolean) {
        if (this.inputEnabled != enable) {
            this.inputEnabled = enable
            updateInputTextEnabled()
        }
    }

    private fun updateInputTextEnabled() {
        mText.isEnabled = this.inputEnabled
    }

    fun isInputEnabled() = this.inputEnabled

    fun setInputText(title: String?) {
        if (this.inputText != title) {
//            mOnValueChangeListener?.onValueChange(this, inputText.toString(), title.toString())
            this.inputText = title ?: ""
            updateInputText()
        }
    }

    private fun updateInputText() {
        mText.setText(inputText)
        mText.setSelection(inputText.length)
    }

    fun setInputStyle(@StyleRes inputStyleRes: Int) {
        if (this.inputStyleRes != inputStyleRes) {
            this.inputStyleRes = inputStyleRes
            updateInputStyle()
        }
    }

    private fun updateInputStyle() {
        Paris.style(mText).apply(inputStyleRes)
    }

    fun setDrawableTintColor(color: Int) {
        if (this.drawableTintColor != color) {
            this.drawableTintColor = color
            updateDrawableTintColor()
        }
    }

    private fun updateDrawableTintColor() {
        TextViewCompat.setCompoundDrawableTintList(mText, ColorStateList.valueOf(drawableTintColor))
    }

    fun setDrawableStateListColor(colorStateList: ColorStateList?) {
        if (this.drawableStateListColor != colorStateList) {
            this.drawableStateListColor = colorStateList
            updateDrawableStateListColor()
        }
    }

    private fun updateDrawableStateListColor() {
        TextViewCompat.setCompoundDrawableTintList(mText, this.drawableStateListColor)
    }

    fun setActionIcon(@DrawableRes actionIconRes: Int) {
        if (this.actionIconRes != actionIconRes) {
            this.actionIconRes = actionIconRes
            updateActionIcon()
        }
    }

    private fun updateActionIcon() {
        val showIcon = listOf(1, 3).contains(displayMode)
        mActionIcon.isVisible = showIcon
        if (showIcon) {
            setImageViewDrawable(
                mActionIcon,
                if (actionIconRes != 0) AppCompatResources.getDrawable(
                    context,
                    actionIconRes
                ) else null
            )
            mActionIcon.isVisible = showActionIcon && this.actionIconRes != 0
        }
    }

    fun setActionIconTintColor(color: Int) {
        if (this.actionIconTintColor != color) {
            this.actionIconTintColor = color
            updateActionIconTintColor()
        }
    }

    private fun updateActionIconTintColor() {
        if (actionIconRes != 0) {
            ImageViewCompat.setImageTintList(
                mActionIcon,
                ColorStateList.valueOf(actionIconTintColor)
            )
        }
    }

    fun getInput() = mText

    fun setActionIconClick(click: OnClickListener?) {
        mActionIcon.setOnClickListener(click)
    }

    fun setActionIconVisible(isVisible: Boolean) {
        mActionIcon.isVisible = isVisible
    }

    private fun setImageViewDrawable(imageView: ImageView, drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }

    fun setDisplayMode(displayMode: Int) {
        if (this.displayMode != displayMode) {
            this.displayMode = displayMode
            updateDisplayMode()
        }
    }

    private fun updateDisplayMode() {
        when (displayMode) {
            ONLY_TEXT -> mText.updatePadding(
                left = context.dpToPx(8f).toInt(),
                right = context.dpToPx(8f).toInt()
            )
            INCLUDE_ACTION_ICON -> mText.updatePadding(
                left = context.dpToPx(8f).toInt(),
                right = context.dpToPx(40f).toInt()
            )
            ONLY_TEXT_WITH_LIMIT -> mText.updatePadding(
                left = context.dpToPx(8f).toInt(),
                right = context.dpToPx(64f).toInt()
            )
            INCLUDE_ACTION_ICON_WITH_LIMIT -> mText.updatePadding(
                left = context.dpToPx(8f).toInt(),
                right = context.dpToPx(96f).toInt()
            )
        }

    }

    fun removeTextChangedListener(watcher: TextWatcher) {
        mText.removeTextChangedListener(watcher)
    }

    fun addTextChangedListener(watcher: TextWatcher) {
        mText.addTextChangedListener(watcher)
    }

}