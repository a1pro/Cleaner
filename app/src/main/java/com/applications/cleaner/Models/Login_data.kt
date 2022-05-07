package com.applications.cleaner.Models

import com.google.gson.annotations.SerializedName

data class Login_data(
    @SerializedName("id"                ) var id              : Int?    = null,
    @SerializedName("role_id"           ) var roleId          : String? = null,
    @SerializedName("name"              ) var name            : String? = null,
    @SerializedName("email"             ) var email           : String? = null,
    @SerializedName("phone"             ) var phone           : String? = null,
    @SerializedName("address"           ) var address         : String? = null,
    @SerializedName("avatar"            ) var avatar          : String? = null,
    @SerializedName("email_verified_at" ) var emailVerifiedAt : String? = null,
    @SerializedName("created_at"        ) var createdAt       : String? = null,
    @SerializedName("updated_at"        ) var updatedAt       : String? = null,
    @SerializedName("original_pass"     ) var originalPass    : String? = null
)
