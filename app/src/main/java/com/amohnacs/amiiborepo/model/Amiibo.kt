package com.amohnacs.amiiborepo.model

data class Amiibo (
    val amiiboSeries: String? = null,
    val character: String? = null,
    val gameSeries: String? = null,
    val head: String? = null,
    val image: String? = null,
    val name: String? = null,
    val release: Release? = null,
    val tail: String? = null,
    val type: String? = null
)

