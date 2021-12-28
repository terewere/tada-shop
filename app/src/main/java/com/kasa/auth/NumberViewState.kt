package com.kasa.auth

import android.os.Parcel
import android.os.Parcelable
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.kasa.utils.PhoneNumberFormatter.formatE164
import com.kasa.utils.PhoneNumberFormatter.getRegionDisplayName
import com.kasa.utils.PhoneNumberFormatter.isValidNumber

data class NumberViewState constructor(val builder: Builder) :
    Parcelable {
    private val selectedCountryName: String? = builder.countryDisplayName
    val countryCode: Int
    val nationalNumber: String?

    fun toBuilder(): Builder {
        return Builder().countryCode(countryCode)
            .selectedCountryDisplayName(selectedCountryName)
            .nationalNumber(nationalNumber)
    }

    val countryDisplayName: String?
        get() {
            if (selectedCountryName != null) {
                return selectedCountryName
            }
            val util: PhoneNumberUtil = PhoneNumberUtil.getInstance()
            if (isValid) {
                val actualCountry: String? = getActualCountry(
                    util,
                    e164Number
                )
                if (actualCountry != null) {
                    return actualCountry
                }
            }
            val regionCode: String = util.getRegionCodeForCountryCode(countryCode)
            return getRegionDisplayName(regionCode)
        }
    val isValid: Boolean
        get() = isValidNumber(
            e164Number, countryCode.toString()
        )



    val e164Number: String
        get() {
            return getConfiguredE164Number(countryCode, nationalNumber)
        }
    val fullFormattedNumber: String
        get() {
            return formatNumber(
                PhoneNumberUtil.getInstance(),
                e164Number
            )
        }

    class Builder() {
        var countryDisplayName: String? = null
        var countryCode: Int = 0
        var nationalNumber: String? = null
        fun countryCode(countryCode: Int): Builder {
            this.countryCode = countryCode
            return this
        }

        fun selectedCountryDisplayName(countryDisplayName: String?): Builder {
            this.countryDisplayName = countryDisplayName
            return this
        }

        fun nationalNumber(nationalNumber: String?): Builder {
            this.nationalNumber = nationalNumber
            return this
        }

        fun build(): NumberViewState {
            return NumberViewState(this)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(selectedCountryName)
        parcel.writeInt(countryCode)
        parcel.writeString(nationalNumber)
    }

    companion object {
        val INITIAL: NumberViewState = Builder().build()

        /**
         * Finds actual name of region from a valid number. So for example +1 might map to US or Canada or other territories.
         */
        private fun getActualCountry(util: PhoneNumberUtil, e164Number: String): String? {
            try {
                val phoneNumber: PhoneNumber = getPhoneNumber(util, e164Number)
                val regionCode: String? = util.getRegionCodeForNumber(phoneNumber)
                if (regionCode != null) {
                    return getRegionDisplayName(regionCode)
                }
            } catch (e: NumberParseException) {
                return null
            }
            return null
        }

        private fun formatNumber(util: PhoneNumberUtil, e164Number: String): String {
            return try {
                val number: PhoneNumber = getPhoneNumber(util, e164Number)
                util.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            } catch (e: NumberParseException) {
                e164Number
            }
        }

        private fun getConfiguredE164Number(countryCode: Int, number: String?): String {
            return formatE164(countryCode.toString(), number)
        }

        @Throws(NumberParseException::class)
        private fun getPhoneNumber(util: PhoneNumberUtil, e164Number: String): PhoneNumber {
            return util.parse(e164Number, null)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<NumberViewState?> =
            object : Parcelable.Creator<NumberViewState?> {
                override fun createFromParcel(`in`: Parcel): NumberViewState {
                    return Builder().selectedCountryDisplayName(`in`.readString())
                        .countryCode(`in`.readInt())
                        .nationalNumber(`in`.readString())
                        .build()
                }

                override fun newArray(size: Int): Array<NumberViewState?> {
                    return arrayOfNulls(size)
                }
            }
    }

    init {
        countryCode = builder.countryCode
        nationalNumber = builder.nationalNumber
    }

}