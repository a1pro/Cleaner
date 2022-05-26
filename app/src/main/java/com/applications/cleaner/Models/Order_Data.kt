package com.applications.cleaner.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


public class Order_Data : Serializable {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("user_id")
    @Expose
    var userId: Int? = null

    @SerializedName("booking_id")
    @Expose
    var bookingId: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("cleaner_id")
    @Expose
    var cleanerId: Int? = null

    @SerializedName("postcode_booking")
    @Expose
    var postcodeBooking: String? = null

    @SerializedName("preferred_time")
    @Expose
    var preferredTime: String? = null

    @SerializedName("available_date")
    @Expose
    var availableDate: String? = null

    @SerializedName("complete_order_date")
    @Expose
    var completeOrderDate: Any? = null

    @SerializedName("ordertimeslot")
    @Expose
    var ordertimeslot: String? = null

    @SerializedName("extra_product")
    @Expose
    var extraProduct: String? = null

    @SerializedName("total_amout")
    @Expose
    var totalAmout: String? = "0"

    @SerializedName("paid_amount")
    @Expose
    var paidAmount: String? = "0"

    @SerializedName("single_oven")
    @Expose
    var singleOven: String? = "0"

    @SerializedName("other_notes")
    @Expose
    var otherNotes: Any? = null

    @SerializedName("house_no")
    @Expose
    var houseNo: String? = null

    @SerializedName("street_name")
    @Expose
    var streetName: String? = null

    @SerializedName("town_city")
    @Expose
    var townCity: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("fname")
    @Expose
    var fname: String? = null

    @SerializedName("lname")
    @Expose
    var lname: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("latitude")
    @Expose
    var latitude: String? = null

    @SerializedName("longitude")
    @Expose
    var longitude: String? = null

    @SerializedName("otp")
    @Expose
    var otp: Any? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    companion object {
        private const val serialVersionUID = 2520143056396083418L
    }
}