package com.kasa.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


object Util {


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