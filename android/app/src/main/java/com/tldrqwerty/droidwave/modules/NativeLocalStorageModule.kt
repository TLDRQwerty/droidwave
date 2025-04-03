package com.tldrqwerty.droidwave.modules

import android.content.Context
import com.lynx.jsbridge.LynxMethod
import com.lynx.jsbridge.LynxModule
import com.lynx.tasm.behavior.LynxContext
import androidx.core.content.edit

class NativeLocalStorageModule(context: Context) : LynxModule(context) {
    private val PREF_NAME = "MyLocalStorage"

    private fun getContext(): Context {
        val lynxContext = mContext as LynxContext
        return lynxContext.context
    }

    @LynxMethod
    fun setStorageItem(key: String, value: String) {
        val sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit() {
            putString(key, value)
        }
    }

    @LynxMethod
    fun getStorageItem(key: String): String? {
        val sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, null)
    }

    @LynxMethod
    fun clearStorage() {
        val sharedPreferences = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit() {
            clear()
        }
    }
}