package com.applications.cleaner.Retrofit

import com.applications.cleaner.Models.AddOnsList
import com.applications.cleaner.Models.Login
import com.applications.cleaner.Models.Orders_
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("phone") username: String,
        @Field("password") password: String
    ): Call<Login>

    @FormUrlEncoded
    @POST("api/getClenarOrder")
    fun getorderlist(
        @Field("cleaner_id") cleaner_id: String
    ): Call<Orders_>

    @FormUrlEncoded
    @POST("api/ordercnfstatus")
    fun confirmOrder(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_>

    @GET("api/productslist")
    fun orderListOrAddOns(): Call<AddOnsList>

    @FormUrlEncoded
    @POST("api/orderhaveprblm")
    fun haveProblemWithOrder(
        @Field("cleaner_Id") cleaner_id: String,
        @Field("order_Id") order_Id: String,
        @Field("notes_issue") notes_issue: String
    ): Call<Orders_>


    @FormUrlEncoded
    @POST("api/location_update")
    fun updateLocation(
        @Field("cleaner_id") cleaner_id: String,
        @Field("live_lat") live_lat: String,
        @Field("live_long") live_long: String
    ): Call<Orders_>

    @FormUrlEncoded
    @POST("api/arrived_at_location")
    fun arriverAtLocation(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_>

    @FormUrlEncoded
    @POST("api/Start_Clean")
    fun startClean(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_>

    @Multipart
    @POST("api/Photo_upload_before_clean")
    fun uploadFilesBeforeCleaning(
        @Part("cleaner_id") cleaner_id: RequestBody?,
        @Part("booking_id") booking_id: RequestBody?,
        @Part("notes") notes: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?
    ): Call<Orders_?>?

    @Multipart
    @POST("api/Complete_clean")
    fun completeCleaning(
        @Part("cleaner_id") cleaner_id: RequestBody?,
        @Part("booking_id") booking_id: RequestBody?,
        @Part("notes") notes: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?
    ): Call<Orders_?>?


    @Multipart
    @POST("api/feedback")
    fun submitFeedback(
        @Part("experience") experience: RequestBody,
        @Part("expectations") expectations: RequestBody,
        @Part("rate") rate: RequestBody,
        @Part("cleaningarea") cleaningarea: RequestBody,
        @Part("grease") grease: RequestBody,
        @Part("carbon") carbon: RequestBody,
        @Part signature: MultipartBody.Part?,
        @Part("cleaner_id") cleaner_id: RequestBody,
        @Part("booking_id") booking_id: RequestBody
    ): Call<Orders_?>?
}