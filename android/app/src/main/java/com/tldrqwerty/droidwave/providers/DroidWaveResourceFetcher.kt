package com.tldrqwerty.droidwave.providers

import android.content.Context
import android.util.Log
import com.lynx.tasm.core.LynxThreadPool
import com.lynx.tasm.resourceprovider.LynxResourceCallback
import com.lynx.tasm.resourceprovider.LynxResourceRequest
import com.lynx.tasm.resourceprovider.LynxResourceResponse;
import com.lynx.tasm.resourceprovider.template.LynxTemplateResourceFetcher
import com.lynx.tasm.resourceprovider.template.TemplateProviderResult
import com.tldrqwerty.droidwave.TemplateApi
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit


class DroidWaveResourceFetcher(): LynxTemplateResourceFetcher() {

    fun requestResource(request: LynxResourceRequest, callback: retrofit2.Callback<ResponseBody>) {
        val retrofit = Retrofit.Builder().baseUrl("https://example.com/").callbackExecutor(LynxThreadPool.getBriefIOExecutor()).build();

        val templateApi = retrofit.create(TemplateApi::class.java);

        val call = templateApi.getTemplate(request.url);
        call.enqueue(callback)
    }

    override fun fetchTemplate(
        request: LynxResourceRequest?,
        callback: LynxResourceCallback<TemplateProviderResult?>?
    ) {
        if (request == null) {
            callback?.onResponse(LynxResourceResponse.onFailed(Throwable("request is null")) as LynxResourceResponse<TemplateProviderResult?>?)
            return
        }

        Log.i(TAG, "fetchTemplate ${request.url}")

        requestResource(request, object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.body() != null) {
                    try {
                        val result = TemplateProviderResult.fromBinary(response.body()?.bytes())
                        callback?.onResponse(LynxResourceResponse.onSuccess(result))
                    } catch (e: IOException) {
                        e.printStackTrace()
                        callback?.onResponse(LynxResourceResponse.onFailed(e) as LynxResourceResponse<TemplateProviderResult?>?)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, throwable: Throwable) {
                callback?.onResponse(LynxResourceResponse.onFailed(throwable) as LynxResourceResponse<TemplateProviderResult?>?);
            }
        })
    }

    override fun fetchSSRData(
        request: LynxResourceRequest?,
        callback: LynxResourceCallback<ByteArray?>?
    ) {
        if (request == null) {
            callback?.onResponse(LynxResourceResponse.onFailed(Throwable("request is null")) as LynxResourceResponse<ByteArray?>?);
            return
        }

        Log.i(TAG, "fetchSSRData ${request.url}")

        requestResource(request, object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                try {
                    if (response.body() != null) {
                        callback?.onResponse(
                            LynxResourceResponse.onSuccess<ByteArray>(
                                response.body()?.bytes()
                            )
                        )
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    callback?.onResponse(LynxResourceResponse.onFailed(e) as LynxResourceResponse<ByteArray?>?)
                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                throwable: Throwable
            ) {
                callback?.onResponse(LynxResourceResponse.onFailed(throwable) as LynxResourceResponse<ByteArray?>?)
            }

        })
    }

    companion object {
        const val TAG = "DroidWaveResourceFetcher"
    }
}