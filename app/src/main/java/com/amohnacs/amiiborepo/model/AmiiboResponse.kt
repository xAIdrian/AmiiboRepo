package com.amohnacs.amiiborepo.model

import com.google.gson.annotations.SerializedName

data class AmiiboResponse (
    val page: Int,
    @SerializedName("amiibo") val amiibos: List<Amiibo>? = null
)
