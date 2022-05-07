package com.applications.cleaner.Models

import com.google.gson.annotations.SerializedName

data class Order_Data(
    @SerializedName("longitude") var longitude: String? = "",
    @SerializedName("latitude") var latitude: String? = "",
    @SerializedName("user_id") var userId: String? = null,
    @SerializedName("booking_id") var bookingId: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("cleaner_id") var cleanerId: String? = null,
    @SerializedName("postcode_booking") var postcodeBooking: String? = null,
    @SerializedName("preferred_time") var preferredTime: String? = null,
    @SerializedName("available_date") var availableDate: String? = null,
    @SerializedName("extra_product") var extraProduct: String? = null,
    @SerializedName("total_amout") var totalAmout: String? = null,
    @SerializedName("single_oven") var singleOven: String? = null,
    @SerializedName("other_notes") var otherNotes: String? = null,
    @SerializedName("house_no") var houseNo: String? = null,
    @SerializedName("street_name") var streetName: String? = null,
    @SerializedName("town_city") var townCity: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("gender") var gender: String? = null,
    @SerializedName("fname") var fname: String? = null,
    @SerializedName("lname") var lname: String? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null
)
