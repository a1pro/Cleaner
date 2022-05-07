package com.applications.cleaner.Models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DataAddOns {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("price")
    @Expose
    var price: String? = null

    @SerializedName("name_product")
    @Expose
    var nameProduct: String? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
}