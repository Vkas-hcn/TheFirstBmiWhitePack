package com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk.BmiApp.Companion.sharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.UUID

object BBBLLL {
    private fun getAppVersion(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
    @SuppressLint("HardwareIds")
    fun blackData(context: Context): Map<String, Any> {
        val uuidData = sharedPreferences.getString("uuidData", "")?:""
        Log.e("TAG", "blackData: $uuidData", )
        return mapOf(
            "greylag" to "com.healthhabit.bmi.calculator",
            "flatbed" to "newark",
            "suspend" to getAppVersion(context).orEmpty(),
            "aqueduct" to uuidData,
            "gerhardt" to System.currentTimeMillis()
        )
    }
    fun getBlackList(context: Context) {
        val blackData = sharedPreferences.getString("blackData", null)

        if (blackData?.isNotEmpty() == true) {
            return
        }
        getMapData(
            "https://strom.healthhabitbmi.com/cistern/bathos",
            blackData(context),
            onNext = {
                Log.e("TAG", "The blacklist request is successful：$it")
                sharedPreferences.edit().putString("blackData", it).apply()
            },
            onError = {
                GlobalScope.launch(Dispatchers.IO) {
                    delay(10000)
                    Log.e("TAG", "The blacklist request failed：$it")
                    getBlackList(context)
                }
            })
    }

    fun getMapData(
        url: String,
        map: Map<String, Any>,
        onNext: (response: String) -> Unit,
        onError: (error: String) -> Unit
    ) {

        val queryParameters = StringBuilder()
        for ((key, value) in map) {

            queryParameters.append("&")
            queryParameters.append(URLEncoder.encode(key, "UTF-8"))
            queryParameters.append("=")
            queryParameters.append(URLEncoder.encode(value.toString(), "UTF-8"))
        }

        val urlString = if (url.contains("?")) {
            "$url&$queryParameters"
        } else {
            "$url?$queryParameters"
        }
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 15000

        try {

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val reader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                inputStream.close()
                onNext(response.toString())
            } else {
                onError("HTTP error: $responseCode")
            }
        } catch (e: Exception) {
            onError("Network error: ${e.message}")
        } finally {
            connection.disconnect()
        }
    }
}