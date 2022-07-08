package com.applications.cleaner.Retrofit

import com.applications.cleaner.Models.AddOnsList
import com.applications.cleaner.Models.Login
import com.applications.cleaner.Models.Orders_models
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
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/ordercnfstatus")
    fun confirmOrder(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_models>

    @GET("api/productslist")
    fun orderListOrAddOns(): Call<AddOnsList>

    @FormUrlEncoded
    @POST("api/orderhaveprblm")
    fun haveProblemWithOrder(
        @Field("cleaner_Id") cleaner_id: String,
        @Field("order_Id") order_Id: String,
        @Field("notes_issue") notes_issue: String
    ): Call<Orders_models>


    @FormUrlEncoded
    @POST("api/location_update")
    fun updateLocation(
        @Field("cleaner_id") cleaner_id: String,
        @Field("live_lat") live_lat: String,
        @Field("live_long") live_long: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/arrived_at_location")
    fun arriverAtLocation(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/Start_Clean")
    fun startClean(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_models>

    @Multipart
    @POST("api/Photo_upload_before_clean")
    fun uploadFilesBeforeCleaning(
        @Part("cleaner_id") cleaner_id: RequestBody?,
        @Part("booking_id") booking_id: RequestBody?,
        @Part("notes") notes: RequestBody?,
        @Part("product_id") product_id: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?
    ): Call<Orders_models?>?

    @Multipart
    @POST("api/Complete_clean")
    fun completeCleaning(
        @Part("cleaner_id") cleaner_id: RequestBody?,
        @Part("booking_id") booking_id: RequestBody?,
        @Part("product_id") product_id: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part file2: MultipartBody.Part?
    ): Call<Orders_models?>?


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
    ): Call<Orders_models?>?


    @FormUrlEncoded
    @POST("api/sendotp")
    fun generateOTP(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/verify")
    fun verifyOtp(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String,
        @Field("otp") otp: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/endOrder")
    fun completeOrder(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/cash_payment")
    fun cashPayment(
        @Field("amount") amount: String,
        @Field("cleaner_id") cleaner_id: String,
        @Field("order_id") booking_id: String,
        @Field("product_id") product_id: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/terminal")
    fun terminalPayment(
        @Field("amount") amount: String,
        @Field("cleaner_id") cleaner_id: String,
        @Field("order_id") booking_id: String,
        @Field("slip_id") slip_id: String
    ): Call<Orders_models>

    @FormUrlEncoded
    @POST("api/addonUpsellby")
    fun addonUpsellby(
        @Field("cleaner_id") cleaner_id: String,
        @Field("booking_id") booking_id: String,
        @Field("addonUpsell_by_clenaer") addonUpsell_by_clenaer: String,
        @Field("addonPrice_by_clenaer") addonPrice_by_clenaer: String,
        @Field("wages_by_clenaer") wages_by_clenaer: String
    ): Call<Orders_models>

}