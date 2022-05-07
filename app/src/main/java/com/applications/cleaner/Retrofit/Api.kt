package com.applications.cleaner.Retrofit

import com.applications.cleaner.Models.AddOnsList
import com.applications.cleaner.Models.Login
import com.applications.cleaner.Models.Orders_
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {

    @FormUrlEncoded
    @POST("api/login")
    fun login(
        @Field("phone") username:String,
        @Field("password") password:String
    ):Call<Login>

    @FormUrlEncoded
    @POST("api/getClenarOrder")
    fun getorderlist(
        @Field("cleaner_id") cleaner_id:String
    ):Call<Orders_>

    @FormUrlEncoded
    @POST("api/ordercnfstatus")
    fun confirmOrder(
        @Field("cleaner_id") cleaner_id:String,
        @Field("booking_id") booking_id:String
    ):Call<Orders_>

    @GET("api/productslist")
    fun orderListOrAddOns():Call<AddOnsList>

    @FormUrlEncoded
    @POST("api/orderhaveprblm")
    fun haveProblemWithOrder(
        @Field("cleaner_Id") cleaner_id:String,
        @Field("order_Id") order_Id:String,
        @Field("notes_issue") notes_issue:String
    ):Call<Orders_>


    @FormUrlEncoded
    @POST("api/location_update")
    fun updateLocation(
        @Field("cleaner_id") cleaner_id:String,
        @Field("live_lat") live_lat:String,
        @Field("live_long") live_long:String
    ):Call<Orders_>

    @FormUrlEncoded
    @POST("api/arrived_at_location")
    fun arriverAtLocation(
        @Field("cleaner_id") cleaner_id:String,
        @Field("booking_id") booking_id:String
    ):Call<Orders_>

}