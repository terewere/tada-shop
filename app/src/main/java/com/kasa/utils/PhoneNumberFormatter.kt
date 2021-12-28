package com.kasa.utils

import android.text.TextUtils
import android.util.Log
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import java.lang.NumberFormatException
import java.util.*
import java.util.regex.Pattern

/**
 * Copyright (C) 2014-2016 Open Whisper Systems
 *
 * Licensed according to the LICENSE file in this repository.
 */
//
//import org.whispersystems.libsignal.logging.Log;
//import org.whispersystems.libsignal.util.guava.Optional;
//import org.whispersystems.signalservice.internal.util.Util;
/**
 * Phone number formats are a pain.
 *
 * @author Moxie Marlinspike
 */

object PhoneNumberFormatter {
    private val TAG = PhoneNumberFormatter::class.java.simpleName
    private const val COUNTRY_CODE_BR = "55"
    private const val COUNTRY_CODE_US = "1"

    @JvmStatic
    fun isValidNumber(e164Number: String, countryCode: String): Boolean {
        if (!PhoneNumberUtil.getInstance().isPossibleNumber(e164Number, countryCode)) {
            Log.i(TAG, "Failed isPossibleNumber()")
            return false
        }
        if (COUNTRY_CODE_US == countryCode && !Pattern.matches(
                "^\\+1[0-9]{10}$",
                e164Number
            )
        ) {
            Log.i(TAG, "Failed US number format check")
            return false
        }
        if (COUNTRY_CODE_BR == countryCode && !Pattern.matches(
                "^\\+55[0-9]{2}9?[0-9]{8}$",
                e164Number
            )
        ) {
            Log.i(TAG, "Failed Brazil number format check")
            return false
        }
        return e164Number.matches("^\\+[1-9][0-9]{6,14}$".toRegex())
    }

    @Throws(Throwable::class)
    private fun impreciseFormatNumber(number: String, localNumber: String): String {
        var number = number
        var localNumber = localNumber
        number = number.replace("[^0-9+]".toRegex(), "")
        if (number[0] == '+') return number
        if (localNumber[0] == '+') localNumber = localNumber.substring(1)
        if (localNumber.length == number.length || number.length > localNumber.length) return "+$number"
        val difference = localNumber.length - number.length
        return "+" + localNumber.substring(0, difference) + number
    }

    fun formatNumberInternational(number: String): String {
        return try {
            val util = PhoneNumberUtil.getInstance()
            val parsedNumber = util.parse(number, null)
            util.format(parsedNumber, PhoneNumberFormat.INTERNATIONAL)
        } catch (e: NumberParseException) {
            Log.i(TAG, e.localizedMessage)
            number
        }
    }

    @Throws(Throwable::class)
    fun formatNumber(number: String, localNumber: String): String {
        var number = number

        if (number.contains("@")) {
            throw Throwable("Possible attempt to use email address.")
        }
        number = number.replace("[^0-9+]".toRegex(), "")
        if (number.isEmpty()) {
            throw Throwable("No valid characters found.")
        }

//    if (number.charAt(0) == '+')
//      return number;
        return try {
            val util = PhoneNumberUtil.getInstance()
            val localNumberObject = util.parse(localNumber, null)
            val localCountryCode = util.getRegionCodeForNumber(localNumberObject)
            Log.i(
                TAG,
                "Got local CC: $localCountryCode"
            )
            val numberObject = util.parse(number, localCountryCode)
            util.format(numberObject, PhoneNumberFormat.E164)
        } catch (e: NumberParseException) {
            Log.i(TAG, e.localizedMessage)
            impreciseFormatNumber(number, localNumber)
        }
    }


    @JvmStatic
    fun getRegionDisplayName(regionCode: String?): String? {
        if (regionCode != null && regionCode != "ZZ" && regionCode != PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY) {
            val displayCountry = Locale("", regionCode).getDisplayCountry(Locale.getDefault())
            if (!TextUtils.isEmpty(displayCountry)) {
                return displayCountry
            }
        }
        return null
    }

    @JvmStatic
    fun formatE164(countryCode: String, number: String?): String {
        try {
            val util = PhoneNumberUtil.getInstance()
            val parsedCountryCode = countryCode.toInt()
            val parsedNumber = util.parse(
                number,
                util.getRegionCodeForCountryCode(parsedCountryCode)
            )
            return util.format(parsedNumber, PhoneNumberFormat.E164)
        } catch (npe: NumberParseException) {
            Log.i(TAG, npe.localizedMessage)
        } catch (npe: NumberFormatException) {
            Log.i(TAG, npe.localizedMessage)
        }
        return "+" +
                countryCode.replace("[^0-9]".toRegex(), "").replace("^0*".toRegex(), "") +
                (number?.replace("[^0-9]".toRegex(), "") ?: "")
    }

    fun getInternationalFormatFromE164(e164number: String): String {
        return try {
            val util = PhoneNumberUtil.getInstance()
            val parsedNumber = util.parse(e164number, null)
            util.format(parsedNumber, PhoneNumberFormat.INTERNATIONAL)
        } catch (e: NumberParseException) {
            Log.w(TAG, e.localizedMessage)
            e164number
        }
    }


}