package com.amohnacs.amiiborepo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Amiibo (
    @PrimaryKey val tail: String,
    val amiiboSeries: String? = null,
    val character: String? = null,
    val gameSeries: String? = null,
    val head: String? = null,
    val image: String? = null,
    val name: String? = null,
    val type: String? = null,
    val isPurchased: Boolean? = false
)

