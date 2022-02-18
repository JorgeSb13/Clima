package com.examen.clima.models

import com.google.gson.annotations.SerializedName

class SearchResponse(
    @SerializedName("id")
    internal var id: Int,
    @SerializedName("name")
    internal var name: String,
    @SerializedName("region")
    internal var region: String,
    @SerializedName("country")
    internal var country: String
)