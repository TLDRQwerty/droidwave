package com.tldrqwerty.droidwave.providers

import android.content.Context
import android.util.Log
import com.lynx.tasm.provider.AbsTemplateProvider
import com.tldrqwerty.droidwave.TemplateApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class DroidWaveTemplateProvider() : AbsTemplateProvider() {

    override fun loadTemplate(uri: String, callback: Callback) {
        val retrofit = Retrofit.Builder().baseUrl("https://example.com/").build()

        val templateApi = retrofit.create(TemplateApi::class.java)

        Log.i("DemoTemplateProvider", uri)

        val call: Call<ResponseBody> = templateApi.getTemplate(uri)
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    Log.i("DemoTemplateProvider", response.isSuccessful.toString())
                    if (response.body() != null) {
                        Log.i("DemoTemplateProvider", response.body()!!.toString())
                        callback.onSuccess(response.body()!!.bytes())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    callback.onFailed(e.toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                Log.i("DemoTemplateProvider", throwable.message.toString())
                callback.onFailed(throwable.message ?: "Unknown error")
            }
        })
    }
}