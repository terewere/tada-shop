//package com.kasa.utils
//
//import android.content.ActivityNotFoundException
//import android.content.Context
//import android.content.Intent
//import android.graphics.PorterDuff
//import android.net.Uri
//import android.os.Build
//import android.util.Log
//import android.widget.ImageView
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.core.content.ContextCompat
//import com.kasa.R
//import com.kasa.utils.Constants.PLAYSTORE_URL
//
//
//object ShareUtil {
//
//
//    fun share(artistName: CharSequence? = "GIR" , trackName:CharSequence? = "LIVE", context: Context) {
//
//       val program = String.format(
//            "%s - %s on GH Islamic Radio",
//            artistName,
//            trackName
//        )
//
//
//        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "text/plain"
//        intent.putExtra(
//            Intent.EXTRA_TEXT,
//            "I am listening NOW ==> \n $program.\n Download the App on Google PlayStore.\n$PLAYSTORE_URL"
//        )
//        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_via)))
//    }
//
//
//    fun openInBrowser(context: Context, url: String?) {
//        try {
//            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            context.startActivity(myIntent)
//        } catch (e: ActivityNotFoundException) {
//            Toast.makeText(context, R.string.pref_no_browser_found, Toast.LENGTH_LONG).show()
//            Log.e(
//                "testing",
//                Log.getStackTraceString(e)
//            )
//        }
//    }
//
//
//
//
//
//
//
//    enum class REPEAT_MODE(value: Int) {
//        NONE(0), ONE(1), ALL(2);
//
//        var value = 0
//
//        init {
//            this.value = value
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    fun updateRepeatDrawable(
//        rmode: REPEAT_MODE?,
//        repeat: ImageView,
//        ctx: Context
//    ) {
//
//        when (rmode) {
//            REPEAT_MODE.ONE -> {
//                repeat.setImageDrawable(ctx.getDrawable(R.drawable.ic_gir_logo))
//                repeat.setColorFilter(
//                    ContextCompat.getColor(ctx, R.color.active),
//                    PorterDuff.Mode.MULTIPLY
//                )
//            }
//            REPEAT_MODE.ALL -> {
//                repeat.setImageDrawable(ctx.getDrawable(R.drawable.ic_gir_logo))
//                repeat.setColorFilter(ContextCompat.getColor(ctx, R.color.active))
//            }
//            REPEAT_MODE.NONE -> {
//                repeat.setImageDrawable(ctx.getDrawable(R.drawable.ic_gir_logo))
//                repeat.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent))
//            }
//            else -> {
//                repeat.setImageDrawable(ctx.getDrawable(R.drawable.ic_gir_logo))
//                repeat.setColorFilter(ContextCompat.getColor(ctx, R.color.colorAccent))
//            }
//        }
//
//    }
//
//    fun updateShuffleDrawable(
//        enabled: Boolean,
//        shuffle: ImageView,
//        ctx: Context?
//    ) {
//        if (enabled) {
//            shuffle.setColorFilter(ContextCompat.getColor(ctx!!, R.color.active))
//        } else {
//            shuffle.setColorFilter(ContextCompat.getColor(ctx!!, R.color.colorAccent))
//        }
//    }
//
//}
