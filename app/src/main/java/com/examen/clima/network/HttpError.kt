package com.examen.clima.network

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.HttpException
import java.io.IOException

internal class HttpError(json: String) {

    @SerializedName("error")
    var error: String? = null

    companion object {

        fun parseException(exception: HttpException): HttpError? {
            return try {
                HttpError(exception.response()?.errorBody()!!.source().readUtf8())
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    init {
        val gson = Gson()
        val temp = gson.fromJson(json, HttpError::class.java)
        error = temp.error
    }

    override fun toString(): String {
        return "{error : $error}"
    }

}