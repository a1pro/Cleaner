package com.applications.cleaner.Models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class AddOnsList {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("data")
    @Expose
    var data: ArrayList<DataAddOns>? = null


}



