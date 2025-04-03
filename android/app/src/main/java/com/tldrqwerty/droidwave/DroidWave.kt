package com.tldrqwerty.droidwave

import android.app.Application
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.memory.PoolConfig
import com.facebook.imagepipeline.memory.PoolFactory
import com.lynx.service.http.LynxHttpService
import com.lynx.service.image.LynxImageService
import com.lynx.service.log.LynxLogService
import com.lynx.tasm.service.LynxServiceCenter
import com.lynx.tasm.LynxEnv

class DroidWave : Application() {

    override fun onCreate() {
        super.onCreate()
        initLynxService()
        initLynxEnv()
    }

    private fun initLynxService() {
        // init Fresco which is needed by LynxImageService
        val factory = PoolFactory(PoolConfig.newBuilder().build())
        val builder = ImagePipelineConfig.newBuilder(applicationContext).setPoolFactory(factory)
        Fresco.initialize(applicationContext, builder.build())

        LynxServiceCenter.inst().registerService(LynxImageService.getInstance())
        LynxServiceCenter.inst().registerService(LynxLogService)
        LynxServiceCenter.inst().registerService(LynxHttpService)
    }

    private fun initLynxEnv() {
        Log.i(TAG, "initLynxEnv")
        LynxEnv.inst().init(
            this,
            null,
            null,
            null
        )

//        // Turn on Lynx Debug
//        LynxEnv.inst().enableLynxDebug(true)
//        // Turn on Lynx DevTool
//        LynxEnv.inst().enableDevtool(true)
//        // Turn on Lynx LogBox
//        LynxEnv.inst().enableLogBox(true)
    }
    companion object {
        val TAG = "DroidWaveApplication"
    }
}