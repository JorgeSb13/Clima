package com.examen.clima.network

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.examen.clima.App
import com.examen.clima.R
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

class HelperUtil {

    fun parseError(e: Throwable, context: Context) {

        if (e is HttpException) {
            when (e.code()) {
                401, 400, 403, 404, 405, 423 -> {
                    val error: HttpError? = HttpError.parseException(e)
                    if (error != null) {
                        AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                            .setMessage(error.error)
                            .setTitle(R.string.error_title)
                            .setCancelable(false)
                            .setPositiveButton(App.shareInstance!!.resources.getString(R.string.accept), null)
                            .show()
                    }
                }
                422 -> {
                    val errors = UnprocessableEntity.parseException(e)
                    AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                        .setMessage(errors!!.errors[0])
                        .setTitle(errors.message)
                        .setCancelable(false)
                        .setPositiveButton(App.shareInstance!!.resources.getString(R.string.accept), null)
                        .show()
                }
                500 -> AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(App.shareInstance!!.resources.getString(R.string.error_unexpected))
                    .setTitle(App.shareInstance!!.resources.getString(R.string.error_title))
                    .setCancelable(false)
                    .setPositiveButton(context.resources.getString(R.string.accept), null)
                    .show()
            }
        } else if (e is IOException) {
            when (e) {
                is ConnectException -> AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(App.shareInstance!!.resources.getString(R.string.error_unreachable_network))
                    .setTitle(App.shareInstance!!.resources.getString(R.string.error_title))
                    .setCancelable(false)
                    .setPositiveButton(App.shareInstance!!.resources.getString(R.string.accept), null)
                    .setNeutralButton(App.shareInstance!!.resources.getString(R.string.network_settings)) { _, _ ->
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting")
                        context.startActivity(intent)
                    }.show()

                is SocketException -> AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(App.shareInstance!!.resources.getString(R.string.error_unreachable_network))
                    .setTitle(App.shareInstance!!.resources.getString(R.string.error_title))
                    .setCancelable(false)
                    .setPositiveButton(App.shareInstance!!.resources.getString(R.string.accept), null)
                    .setNeutralButton(App.shareInstance!!.resources.getString(R.string.network_settings)) { _, _ ->
                        val intent = Intent(Intent.ACTION_MAIN)
                        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting")
                        context.startActivity(intent)
                    }.show()

                is SocketTimeoutException -> AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(App.shareInstance!!.resources.getString(R.string.error_timeout))
                    .setTitle(App.shareInstance!!.resources.getString(R.string.error_title))
                    .setCancelable(false)
                    .setPositiveButton(App.shareInstance!!.resources.getString(R.string.accept), null)
                    .show()

                is UnknownHostException -> Toast.makeText(context, context.getString(R.string.error_unknown_host_exception), Toast.LENGTH_LONG).show()

                is SSLPeerUnverifiedException -> AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setMessage(App.shareInstance!!.resources.getString(R.string.certificate_error_message))
                    .setTitle(App.shareInstance!!.resources.getString(R.string.error_certificate))
                    .setCancelable(false)
                    .setPositiveButton(App.shareInstance!!.resources.getString(R.string.accept), null)
                    .show()

                else -> Toast.makeText(context, context.getString(R.string.error_unexpected), Toast.LENGTH_LONG).show()
            }
        }
    }

}