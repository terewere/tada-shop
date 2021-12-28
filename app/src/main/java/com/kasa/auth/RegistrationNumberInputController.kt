package com.kasa.auth

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.google.i18n.phonenumbers.AsYouTypeFormatter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.kasa.R

import java.lang.StringBuilder


/**
 * Handle the logic and formatting of phone number input for registration/change number flows.
 */
class RegistrationNumberInputController(
    private val context: Context,
    private val countryCode: EditText,
    private val number: EditText,
    countrySpinner: Spinner,
    private val lastInput: Boolean,
    var callbacks: Callbacks
) {


    private var countrySpinnerAdapter: ArrayAdapter<String>? = null
    private var countryFormatter: AsYouTypeFormatter? = null
    private var isUpdating = true

    init {
        initializeSpinner(countrySpinner)
        setUpNumberInput()

        countryCode.addTextChangedListener(CountryCodeChangedListener())
        countryCode.imeOptions = EditorInfo.IME_ACTION_NEXT
    }

    private fun setUpNumberInput() {
        val numberInput: EditText = number
        numberInput.addTextChangedListener(NumberChangedListener())
        number.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                callbacks.onNumberFocused()
            }
        }
        numberInput.imeOptions =
            if (lastInput) EditorInfo.IME_ACTION_DONE else EditorInfo.IME_ACTION_NEXT
        numberInput.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                callbacks.onNumberInputNext(v)
                return@setOnEditorActionListener true
            } else if (actionId == EditorInfo.IME_ACTION_DONE) {
                callbacks.onNumberInputDone(v)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initializeSpinner(countrySpinner: Spinner) {
        countrySpinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item)
        countrySpinnerAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        setCountryDisplay(context.getString(R.string.RegistrationActivity_select_your_country))
        countrySpinner.adapter = countrySpinnerAdapter
        countrySpinner.setOnTouchListener { view: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_UP) {
                callbacks.onPickCountry(view)
            }
            true
        }
        countrySpinner.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.action == KeyEvent.ACTION_UP) {
                callbacks.onPickCountry(view)
                return@setOnKeyListener true
            }
            false
        }
    }

    fun updateNumber(numberViewState: NumberViewState) {
        val countryCode = numberViewState.countryCode
        val countryCodeString = countryCode.toString()
        val number = numberViewState.nationalNumber
        val regionDisplayName = numberViewState.countryDisplayName
        isUpdating = true
        setCountryDisplay(regionDisplayName)
        if (this.countryCode.text == null || this.countryCode.text.toString() != countryCodeString
        ) {
            this.countryCode.setText(countryCodeString)
            val regionCode = PhoneNumberUtil.getInstance().getRegionCodeForCountryCode(countryCode)
            setCountryFormatter(regionCode)
        }
        if (justDigits(this.number.text) != number && !TextUtils.isEmpty(number)) {
            this.number.setText(number)
        }
        isUpdating = false
    }

    private fun justDigits(text: Editable?): String {
        if (text == null) {
            return ""
        }
        val justDigits = StringBuilder()
        for (element in text) {
            if (Character.isDigit(element)) {
                justDigits.append(element)
            }
        }
        return justDigits.toString()
    }

    private fun setCountryDisplay(regionDisplayName: String?) {
        countrySpinnerAdapter!!.clear()
        if (regionDisplayName == null) {
            countrySpinnerAdapter!!.add(context.getString(R.string.RegistrationActivity_select_your_country))
        } else {
            countrySpinnerAdapter!!.add(regionDisplayName)
        }
    }

    private fun setCountryFormatter(regionCode: String?) {
        val util = PhoneNumberUtil.getInstance()
        countryFormatter = if (regionCode != null) util.getAsYouTypeFormatter(regionCode) else null
        reformatText(number.text)
    }

    private fun reformatText(s: Editable): String? {
        if (countryFormatter == null) {
            return null
        }
        if (TextUtils.isEmpty(s)) {
            return null
        }
        countryFormatter!!.clear()
        var formattedNumber: String? = null
        val justDigits = StringBuilder()
        for (element in s) {
            if (Character.isDigit(element)) {
                formattedNumber = countryFormatter!!.inputDigit(element)
                justDigits.append(element)
            }
        }
        if (formattedNumber != null && s.toString() != formattedNumber) {
            s.replace(0, s.length, formattedNumber)
        }
        return if (justDigits.isEmpty()) {
            null
        } else justDigits.toString()
    }

    private inner class NumberChangedListener : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            val number = reformatText(s) ?: return
            if (!isUpdating) {
                callbacks.setNationalNumber(number)
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private inner class CountryCodeChangedListener : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (TextUtils.isEmpty(s) || !TextUtils.isDigitsOnly(s)) {
                setCountryDisplay(null)
                countryFormatter = null
                return
            }
            val countryCode = s.toString().toInt()
            val regionCode = PhoneNumberUtil.getInstance().getRegionCodeForCountryCode(countryCode)
            setCountryFormatter(regionCode)
            if (!TextUtils.isEmpty(regionCode) && regionCode != "ZZ") {
                if (!isUpdating) {
                    number.requestFocus()
                }
                val numberLength: Int = number.text.length
                number.setSelection(numberLength, numberLength)
            }
            if (!isUpdating) {
                callbacks.setCountry(countryCode)
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    interface Callbacks {
        fun onNumberFocused()
        fun onNumberInputNext(view: View)
        fun onNumberInputDone(view: View)
        fun onPickCountry(view: View)
        fun setNationalNumber(number: String)
        fun setCountry(countryCode: Int)
    }

}
