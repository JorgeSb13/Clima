package com.examen.clima.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
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

fun getTimeZone(): String {
    val time = Calendar.getInstance().time.toString()
    val splitTime = time.split(" ")
    return splitTime[4] // e.g "GMT-07:00"
}

fun temperatureFormat(temp: Float): String {
    return "${temp.toInt()}°C"
}

fun temperatureFormatNoMeasure(temp: Float): String {
    return "${temp.toInt()}°"
}

fun dateTimeSplitter(dateTime: String): List<String> {
    return dateTime.split(" ")
}

fun timeFormat(time: String): String {
    val timeSplit = time.split(":")
    val hour = timeSplit[0].toInt()
    val minutes = timeSplit[1]

    return if (hour > 12) {
        if ((hour - 12) == 12)
            "${hour - 12}:$minutes a.m."
        else
            "${hour - 12}:$minutes p.m."
    } else if (hour == 12)
        "${hour}:$minutes p.m."
    else if (hour == 0)
        "${12}:$minutes a.m."
    else
        "${hour}:$minutes a.m."
}

fun dateFormat(date: String): String {
    val dateSplit = date.split("-")
    val dAux = Date(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())

    val day = SimpleDateFormat("d", Locale("es", "MX")) // e.g "15"
    val month = SimpleDateFormat("MMMM", Locale("es", "MX")) // e.g "febrero"

    // Correct the number respect at the day of week
    var dayOfWeek = dAux.day - 1
    if (dayOfWeek == 0)
        dayOfWeek = 7
    else if (dayOfWeek == -1)
        dayOfWeek = 6

    return when (dayOfWeek) {
        1 -> "lun., ${day.format(dAux)} de ${month.format(dAux)}"
        2 -> "mar., ${day.format(dAux)} de ${month.format(dAux)}"
        3 -> "mié., ${day.format(dAux)} de ${month.format(dAux)}"
        4 -> "jue., ${day.format(dAux)} de ${month.format(dAux)}"
        5 -> "vie., ${day.format(dAux)} de ${month.format(dAux)}"
        6 -> "sáb., ${day.format(dAux)} de ${month.format(dAux)}"
        7 -> "dom., ${day.format(dAux)} de ${month.format(dAux)}"
        else -> "${day.format(dAux)} de ${month.format(dAux)}"
    }
}

fun dateTimeFormat(date: String, time: String): String {
    return "$date $time"
}

fun getDay(date: String): String {
    val dateSplit = date.split("-")
    val dAux = Date(dateSplit[0].toInt(), dateSplit[1].toInt(), dateSplit[2].toInt())

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("es", "MX")) // e.g "2022-02-16"
    val aDate = Date()
    val actualDate = dateFormat.format(aDate)

    // Correct the number respect at the day of week
    var dayOfWeek = dAux.day - 1
    if (dayOfWeek == 0)
        dayOfWeek = 7
    else if (dayOfWeek == -1)
        dayOfWeek = 6

    return if (date == actualDate) {
        "Hoy"
    } else {
        when (dayOfWeek) {
            1 -> "Lunes"
            2 -> "Martes"
            3 -> "Miércoles"
            4 -> "Jueves"
            5 -> "Viernes"
            6 -> "Sábado"
            7 -> "Domingo"
            else -> "X"
        }
    }
}

fun View.getImage(name: String) = resources.getIdentifier(name, "drawable", App.context!!.packageName)
fun AppCompatActivity.getImage(name: String) = resources.getIdentifier(name, "drawable", App.context!!.packageName)











