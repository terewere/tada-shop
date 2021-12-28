package com.kasa.utils.extentions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.kasa.MainActivity
import com.kasa.R
import com.kasa.utils.Constants.PREF_LAST_FRAGMENT_TAG
import com.kasa.utils.Constants.PREF_NAME


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

 fun showCustomToast(context: Context,
                     message: String,
                     length: Int = Toast.LENGTH_SHORT,
                     y: Int = 400,
                     color: Int = android.R.color.holo_red_dark
                     ) {
    val toast = Toast.makeText(context, message, length)
    val view: View? = toast.view
    toast.setGravity(Gravity.BOTTOM, 0, y)

    //To change the Background of Toast
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         toast.view?.background?.setTintList(
             ContextCompat.getColorStateList(context, color)
         )
     } else {
         view?.setBackgroundColor(getColor(context, color))

     }

     val text = view?.findViewById(android.R.id.message) as TextView

    text.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
    text.setTextColor(Color.WHITE)
    toast.show()
}



    fun setToolbarTextViewsMarquee(toolbar: Toolbar) {
        for (child in toolbar.children) {
            if (child is TextView) {
                setMarquee(child)
            }
        }
    }

    fun setMarquee(textView: TextView) {
        textView.ellipsize = TextUtils.TruncateAt.MARQUEE
        textView.isSelected = true
        textView.marqueeRepeatLimit = -1
    }


fun setTitle(activity: Activity, title: String) {
    (activity as? AppCompatActivity)?.supportActionBar?.apply {
        this.title = title
    }
}

//fun setSubTitle(activity: MainActivity?, title: String?) {
//    (activity as? AppCompatActivity)?.supportActionBar?.apply {
//        this.subtitle = title
//    }
//}



fun saveLastNavFragment(context: Context, feedId: Long?, feedType: String?) {

    val prefs = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )
    val edit = prefs.edit()
    if (feedId != null) {
        edit.putString(PREF_LAST_FRAGMENT_TAG, "${feedType}-${feedId}")

    } else {
        edit.remove(PREF_LAST_FRAGMENT_TAG)
    }
    edit.apply()
}

fun getLastNavFragment(context: Context): String? {
    val prefs = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    return prefs.getString(
        PREF_LAST_FRAGMENT_TAG,
        null
    )
}



//fun Fragment.getMainActivity(): MainActivity? {
//    if (!isAdded) return null
//    return (requireActivity() as MainActivity)
//}


fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}


//     fun checkTls(context: Context) {
//
//         if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//             ProviderInstaller.installIfNeededAsync(context.applicationContext, object :
//                 ProviderInstaller.ProviderInstallListener {
//                 override fun onProviderInstalled() {
//                     Log.d("testing", "Provider install successfully")
//
//                 }
//
//                 override fun onProviderInstallFailed(i: Int, intent: Intent) {
//                     Log.d("testing", "Provider install failed ($i) : SSL Problems may occurs")
//                 }
//             })
//         }
//     }

     fun getDeviceName(): String {
         val manufacturer = Build.MANUFACTURER
         val model = Build.MODEL
         if (model.startsWith(manufacturer)) {
             return capitalize(model)
         } else {
             return capitalize(manufacturer) + " " + model
         }
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

     fun Activity.getColor2(color: Int): Int {
         return getColor(this, color)
     }


// fun checkFirstTime() {
//    val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//    editor = prefs.edit()
//    if (prefs.getBoolean(FIRST_LAUNCH, false)) {
//
//    }
//
//    editor?.putBoolean(FIRST_LAUNCH, true)
//    editor?.apply()
//}

//private open fun loadFragment(fragment: Fragment) {
//    val fragmentManager: FragmentManager = getSupportFragmentManager()
//    // clear back stack
//    for (i in 0 until fragmentManager.backStackEntryCount) {
//        fragmentManager.popBackStack()
//    }
//    val t = fragmentManager.beginTransaction()
//    t.replace(R.id.main_view, fragment, MainActivity.MAIN_FRAGMENT_TAG)
//    fragmentManager.popBackStack()
//    // TODO: we have to allow state loss here
//// since this function can get called from an AsyncTask which
//// could be finishing after our app has already committed state
//// and is about to get shutdown.  What we *should* do is
//// not commit anything in an AsyncTask, but that's a bigger
//// change than we want now.
//    t.commitAllowingStateLoss()
//    drawerLayout.closeDrawer(navDrawer)
//}