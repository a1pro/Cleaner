package com.applications.cleaner.Models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Orders_models : Serializable {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Order_Data>? = null

    companion object {
        private const val serialVersionUID = -6434400233452567018L
    }
}