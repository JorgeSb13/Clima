package com.examen.clima.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.examen.clima.App
import com.examen.clima.R
import java.text.SimpleDateFormat
import java.util.*

fun toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(App.context, message, duration).show()

fun toast(resourceId: Int, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(App.context, resourceId, duration).show()

fun ViewGroup.inflate(layoutId: Int) = LayoutInflater.from(context).inflate(layoutId, this, false)!!

inline fun <reified T : Activity> Activity.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.init()
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.goToActivity(noinline init: Intent.() -> Unit = {}) {
    val intent = Intent(context, T::class.java)
    intent.init()
    startActivity(intent)
}

fun Activity.goToActivityResult(action: String, requestCode: Int, init: Intent.() -> Unit = {}) {
    val intent = Intent(action)
    intent.init()
    startActivityForResult(intent, requestCode)
}

fun Fragment.goToActivityResult(action: String, requestCode: Int, init: Intent.() -> Unit = {}) {
    val intent = Intent(action)
    intent.init()
    startActivityForResult(intent, requestCode)
}

fun AppCompatActivity.fragmentTransaction(fragment: Fragment) {
    supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
}

fun Activity.enableLoading(frame: View, visibility: Int, text: String? = "") {
    frame.visibility = visibility
    val tvLoading: TextView = findViewById(R.id.tvLoading)
    tvLoading.text = text
}
fun Fragment.enableLoading(frame: View, visibility: Int, text: String? = "") {
    frame.visibility = visibility
    val tvLoading: TextView = view!!.findViewById(R.id.tvLoading)
    tvLoading.text = text
}

fun alertDialog(context: Context, title: String, message: String, positiveBtn: String,
                posListener: DialogInterface.OnClickListener, negativeBtn: String,
                negListener: DialogInterface.OnClickListener? = null) {

    AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveBtn, posListener)
        .setNegativeButton(negativeBtn, negListener)
        .setCancelable(false).show()
}

fun simpleAlertDialog(context: Context, title: String, message: String, buttonText: String,
                      listener: DialogInterface.OnClickListener? = null) {

    AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(buttonText, listener)
        .setCancelable(false).show()
}

fun Activity.transitionBack() = overridePendingTransition(R.anim.animation_right_in, R.anim.animation_right_out)
fun Activity.transitionRight() = overridePendingTransition(R.anim.animation_right_in, R.anim.animation_right_out)
fun Activity.transitionLeft() = overridePendingTransition(R.anim.animation_left_in, R.anim.animation_left_out)

fun getDateTime(): String {
    val day = SimpleDateFormat("EEE, d", Locale("es", "MX")) // e.g "mar., 15"
    val monthTime = SimpleDateFormat("MMMM h:mm aa", Locale("es", "MX")) // e.g "febrero 2:33 p. m."
    val date = Date()
    return "${day.format(date)} de ${monthTime.format(date)}" // e.g "mar., 15 de febrero 2:33 p. m."
}

fun getTimeZone(): String {
    val time = Calendar.getInstance().time.toString()
    val splitTime = time.split(" ")
    return splitTime[4] // e.g "GMT-07:00"
}