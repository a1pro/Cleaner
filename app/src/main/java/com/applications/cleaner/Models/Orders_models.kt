package com.applications.cleaner.Models

import com.google.gson.annotations.SerializedName

data class Orders_ (@SerializedName("code"   ) var code   : Int?            = null,
                    @SerializedName("status" ) var status : String?         = null,
                    @SerializedName("data"   ) var data   : ArrayList<Order_Data> = arrayListOf()
)