package com.the.first.bmi.white.pack.thefirstbmiwhitepack.rrrrkk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.the.first.bmi.white.pack.thefirstbmiwhitepack.wwwwwlll.BBBLLL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class BmiApp : Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var sharedPreferences: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        sharedPreferences = this.getSharedPreferences("BmiRecords", Context.MODE_PRIVATE)
        val uuidData = sharedPreferences.getString("uuidData", null)
        if (uuidData == null) {
            sharedPreferences.edit().putString("uuidData", UUID.randomUUID().toString()).apply()
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }
}
