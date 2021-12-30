package com.kasa.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import com.kasa.utils.Constants.TAG
import java.io.File
import java.io.FileOutputStream
import androidx.core.content.ContextCompat.startActivity





object Util {

    @RequiresPermission(anyOf = [Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS])
    @SuppressLint("MissingPermission")
    fun getDeviceNumber(context: Context): PhoneNumber? {
        return try {
            val localNumber =
                (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number
            val countryIso: String? = getSimCountryIso(context)
            if (TextUtils.isEmpty(localNumber)) return null
            if (countryIso ==null) null
            else  PhoneNumberUtil.getInstance().parse(localNumber, countryIso)

        } catch (e: NumberParseException) {
            Log.i(TAG, e.localizedMessage)
           null
        }


    }

    fun getSimCountryIso(context: Context): String? {
        val simCountryIso =
            (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simCountryIso
        return simCountryIso?.uppercase()
    }


    fun shareProduct(context: Context,message:String, file: File){
        val uri = FileProvider.getUriForFile(context, "com.tada.provider", file)

        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "*/*"
        sharingIntent.putExtra( Intent.EXTRA_TEXT,message);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
        context.startActivity(sharingIntent)
//        context.startActivity(Intent.createChooser( sharingIntent, "Share using"))
    }

    fun getLocalBitmapUri(context: Context, bmp: Bitmap, title: String) : File {

            val file =  File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                title + System.currentTimeMillis() + ".png"
            );
            val out = FileOutputStream(file);

            bmp.compress(
                Bitmap.CompressFormat.PNG,
                50,
                out
            );

            out.close();

            return file


    }


    fun trackOrderInfo(context: Context, orderId: String){
        val phoneNumberWithCountryCode = "+233243270000"

        val message = "I would like to check the status of my order ${orderId}"

        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                        phoneNumberWithCountryCode,
                        message
                    )
                )
            )
        )
    }
}