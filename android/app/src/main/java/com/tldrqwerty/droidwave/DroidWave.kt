package com.tldrqwerty.droidwave

import android.app.Application
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.memory.PoolConfig
import com.facebook.imagepipeline.memory.PoolFactory
import com.lynx.service.devtool.LynxDevToolService
import com.lynx.service.http.LynxHttpService
import com.lynx.service.image.LynxImageService
import com.lynx.service.log.LynxLogService
import com.lynx.tasm.service.LynxServiceCenter
import com.lynx.tasm.LynxEnv
import com.tldrqwerty.droidwave.modules.NativeLocalStorageModule

class DroidWave : Application() {

    override fun onCreate() {
        super.onCreate()

        initFresco()

        initLynxService()
        initLynxModules()
        initLynxEnv()
    }

    private fun initFresco() {
        // init Fresco which is needed by LynxImageService
        val factory = PoolFactory(PoolConfig.newBuilder().build())
        val builder = ImagePipelineConfig.newBuilder(applicationContext).setPoolFactory(factory)
        Fresco.initialize(applicationContext, builder.build())
    }

    private fun initLynxService() {

        LynxServiceCenter.inst().registerService(LynxDevToolService)

        LynxServiceCenter.inst().registerService(LynxImageService.getInstance())
        LynxServiceCenter.inst().registerService(LynxLogService)
        LynxServiceCenter.inst().registerService(LynxHttpService)
    }

    private fun initLynxModules () {
        LynxEnv.inst().registerModule("NativeLocalStorageModule", NativeLocalStorageModule::class.java);
    }

    private fun initLynxEnv() {
        LynxEnv.inst().init(
            this,
            null,
            null,
            null
        )
        // Enable Lynx Debug
        LynxEnv.inst().enableLynxDebug(true);
        // Enable Lynx DevTool
        LynxEnv.inst().enableDevtool(true);
        // Enable Lynx LogBox
        LynxEnv.inst().enableLogBox(true);
    }

    companion object {
        val TAG = "DroidWaveApplication"
    }
}