package com.tldrqwerty.droidwave.providers

import android.util.Log
import com.lynx.tasm.resourceprovider.LynxResourceCallback
import com.lynx.tasm.resourceprovider.LynxResourceRequest
import com.lynx.tasm.resourceprovider.LynxResourceResponse
import com.lynx.tasm.resourceprovider.generic.LynxGenericResourceFetcher
import com.tldrqwerty.droidwave.TemplateApi
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class DroidWaveGenericResourceFetcher: LynxGenericResourceFetcher() {
    override fun fetchResource(
        request: LynxResourceRequest?,
        callback: LynxResourceCallback<ByteArray?>?
    ) {
        if (request == null) {
            callback?.onResponse(LynxResourceResponse.onFailed(Throwable("request is null")) as LynxResourceResponse<ByteArray?>?)
            return
        }

        val retrofit = Retrofit.Builder().baseUrl("https://example.com/").build();

        val templateApi = retrofit.create(TemplateApi::class.java);

        val call = templateApi.getTemplate(request.url);

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.body() != null) {
                    try {
                        callback?.onResponse(LynxResourceResponse.onSuccess(response.body()?.bytes()))
                    } catch (e: IOException) {
                        e.printStackTrace()
                        callback?.onResponse(LynxResourceResponse.onFailed(e) as LynxResourceResponse<ByteArray?>?)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, throwable: Throwable) {
                callback?.onResponse(LynxResourceResponse.onFailed(throwable) as LynxResourceResponse<ByteArray?>?);
            }
        })
    }

    override fun fetchResourcePath(
        request: LynxResourceRequest?,
        callback: LynxResourceCallback<String?>?
    ) {
        callback?.onResponse(LynxResourceResponse.onFailed(Throwable("fetchResourcePath is not supported")) as LynxResourceResponse<String?>?)
    }

    companion object {
        val TAG = "DroidWaveGenericResourceFetcher"
    }
}