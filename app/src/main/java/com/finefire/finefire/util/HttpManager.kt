package com.finefire.finefire.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.appcompat.app.AlertDialog
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*


class HttpManager(var context: Context) {
    private lateinit var ce: (obj:JSONObject) -> Unit
    var rootUrl = "http://ff.adoo.kr:8081/"

    fun login(id:String, pw:String, ce: (obj:JSONObject) -> Unit){
        val loginTask  = LoginTask()
        loginTask.id = id
        loginTask.pw = pw
        this.ce = ce
        loginTask.execute()
    }

    fun get(subUrl:String, ce: (obj:JSONObject) -> Unit){
        val getTask  = GetTask()
        getTask.subUrl = subUrl
        this.ce = ce
        getTask.execute()
    }

    inner class LoginTask : AsyncTask<Void?, Void?, String>() {
        var id = ""
        var pw = ""

        @SuppressLint("NewApi")
        override fun doInBackground(vararg params: Void?): String {
            var response:String = ""
            try {
                val pushToken = StorageManager().getToken(context)
                val client: OkHttpClient = OkHttpClient().newBuilder().addInterceptor(AuthenticationInterceptor("finefire", "Api**FineFire**0825")).build()
                val mediaType: MediaType = "multipart/form-data; boundary=--------------------------679199047935296003279868".toMediaTypeOrNull()!!
                val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("grant_type", "password")
                    .addFormDataPart("username", id)
                    .addFormDataPart("password", pw)
                    .addFormDataPart("push_token", pushToken!!)
                    .addFormDataPart("device_os", "android", "".toRequestBody(mediaType))
                    .build()
                val request: Request = Request.Builder()
                    .url("${rootUrl}oauth/token")
                    .method("POST", body)
                    .addHeader("Authorization", "Basic ZmluZWZpcmU6QXBpKipGaW5lRmlyZSoqMDgyNQ==")
                    .addHeader(
                        "Content-Type",
                        "multipart/form-data; boundary=--------------------------679199047935296003279868"
                    )
                    .build()
                var res: Response = client.newCall(request).execute()
                var strs = res.body?.string()
                Log.d("",strs)

                response = strs.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val body = JSONObject(result)
                if(body.has("access_token")){
                    val access_token = body.getString("access_token")
                    StorageManager().setLoginToken(context, access_token)
                }

                ce(body)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        inner class AuthenticationInterceptor(user: String, password: String) : Interceptor {

            private val credentials: String = Credentials.basic(user, password)

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build()
                return chain.proceed(authenticatedRequest)
            }

        }
    }


    inner class GetTask : AsyncTask<Void?, Void?, String>() {
        var subUrl = ""
        @SuppressLint("NewApi")
        override fun doInBackground(vararg params: Void?): String {
            var response:String = ""
            try {

                val loginToken = StorageManager().getLoginToken(context)
                val client = OkHttpClient().newBuilder()
                    .build()
                val request: Request = Request.Builder()
                    .url("${rootUrl}${subUrl}")
                    .method("GET", null)
                    .addHeader("Authorization", "Bearer ${loginToken}")
                    .build()
                var res = client.newCall(request).execute()
                var str = res.body?.string()

                response = str.toString()

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return response
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                val body = JSONObject(result)
                if (!body.has("code")){
                    val oDialog = AlertDialog.Builder(context)
                    oDialog.setMessage("서버 연결에 실패 하였습니다")
                        .setTitle("네트워크를 확인해주세요")
                        .setPositiveButton("확인") { dialog, which -> }
                        .setCancelable(false).show()
                }else{
                    ce(body)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}