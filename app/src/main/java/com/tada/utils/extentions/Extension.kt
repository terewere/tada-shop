package com.tada.utils.extentions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tada.R


fun Fragment?.setTitle(title: String) {
    (this?.activity as? AppCompatActivity)?.supportActionBar?.apply {
        this.title = title
    }
}


fun setProgressDialog(context: Context): AlertDialog {
    val llPadding = 30
    val ll = LinearLayout(context)
    ll.orientation = LinearLayout.HORIZONTAL
    ll.setPadding(llPadding, llPadding, llPadding, llPadding)
    ll.gravity = Gravity.CENTER
    var llParam = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT)
    llParam.gravity = Gravity.CENTER
    ll.layoutParams = llParam

    val progressBar = ProgressBar(context)
    progressBar.isIndeterminate = true
    progressBar.setPadding(0, 0, llPadding, 0)
    progressBar.layoutParams = llParam

    llParam = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT)
    llParam.gravity = Gravity.LEFT
    val tvText = TextView(context)
    tvText.text = context.getString(R.string.loading_short)
    tvText.setTextColor(Color.parseColor("#000000"))
    tvText.textSize = 14.toFloat()
    tvText.layoutParams = llParam

    ll.addView(progressBar)
    ll.addView(tvText)

    val builder = AlertDialog.Builder(context)
    builder.setCancelable(true)
    builder.setView(ll)

    val dialog = builder.create()
    val window = dialog.window
    if (window != null) {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }
    return dialog
}



     fun capitalize(s: String?): String {
         if (s == null || s.isEmpty()) {
             return ""
         }
         val first = s[0]
         return if (Character.isUpperCase(first)) {
             s
         } else {
             Character.toUpperCase(first).toString() + s.substring(1)
         }
     }



fun hideKeyboard(context: Context, view: View) {

    val mview = view.findViewById<View>(android.R.id.content)

    if (mview != null) {
        val imm =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )

    }

}

     fun hideKeyboard(context: Activity) {

         val view = context.findViewById<View>(android.R.id.content)

         if (view != null) {
             val imm =
                 context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
             imm!!.hideSoftInputFromWindow(
                 view.windowToken,
                 InputMethodManager.RESULT_UNCHANGED_SHOWN
             )

         }

     }
