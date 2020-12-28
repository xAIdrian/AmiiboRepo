package com.amohnacs.amiiborepo.model

import com.google.gson.annotations.SerializedName

data class AmiiboResponse (
    @SerializedName("amiibo") val amiibos: List<Amiibo>? = null
)
