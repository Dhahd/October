package kanielOutis.october.api

import kanielOutis.october.data.AppResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface api {

    @GET
    fun getMessages(@Header("Authorization") token: String = "", @Url url: String): Call<AppResponse>

    @POST
    @Multipart
    fun sendFile(
        @Url url: String, @Header("Authorization") token: String,
        @Part("message") msg: RequestBody? = null,
        @Part file: MultipartBody.Part
    ): Call<AppResponse>

    @POST
    @FormUrlEncoded
    fun sendMsg(
        @Url url: String, @Header("Authorization") token: String,
        @Field("message") msg: String
    ): Call<AppResponse>

    @POST("auth/login")
    @FormUrlEncoded
    fun login(@Field("name") name: String, @Field("password") password: String): Call<AppResponse>

    @GET("users/index")
    fun getUsers(@Header("Authorization") token: String): Call<AppResponse>
}