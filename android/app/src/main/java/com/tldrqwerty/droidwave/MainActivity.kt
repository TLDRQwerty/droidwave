package com.tldrqwerty.droidwave

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.lynx.tasm.LynxBooleanOption
import com.lynx.tasm.LynxView
import com.lynx.tasm.LynxViewBuilder
import com.lynx.tasm.TimingHandler
import com.tldrqwerty.droidwave.providers.DroidWaveGenericResourceFetcher
import com.tldrqwerty.droidwave.providers.DroidWaveResourceFetcher

class MainActivity : Activity() {

    val extraTimingInfo = TimingHandler.ExtraTimingInfo()

    lateinit var mLynxView: LynxView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        val builder = LynxViewBuilder()

        builder.setEnableGenericResourceFetcher(LynxBooleanOption.TRUE)
        builder.setGenericResourceFetcher(DroidWaveGenericResourceFetcher())
        builder.setTemplateResourceFetcher(DroidWaveResourceFetcher())

        val lynxView = builder.build(this)

        val uri = "http://192.168.0.200:3000/main.lynx.bundle"
        extraTimingInfo.mOpenTime = System.currentTimeMillis()
        extraTimingInfo.mContainerInitStart = System.currentTimeMillis()
        extraTimingInfo.mPrepareTemplateEnd = System.currentTimeMillis()

        setContentView(lynxView)
        lynxView.setExtraTiming(extraTimingInfo)
        lynxView.renderTemplateUrl(uri, "")

        mLynxView = lynxView

    }

    override fun onDestroy() {
        super.onDestroy()
        mLynxView.destroy()
    }

    companion object {
        val TAG = "MainActivity"
    }
}