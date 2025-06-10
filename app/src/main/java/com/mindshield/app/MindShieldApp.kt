package com.mindshield.app

import android.app.Application
import android.content.Context
import com.mindshield.app.utils.PreferenceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MindShieldApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        
        // Initialize preference manager
        PreferenceManager.getInstance(this)
    }

    companion object {
        @Volatile
        private var instance: MindShieldApp? = null

        fun getAppContext(): Context {
            return instance?.applicationContext ?: throw IllegalStateException(
                "Application instance not initialized"
            )
        }
    }
}
