package com.amohnacs.amiiborepo.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Amiibo (
        @PrimaryKey var tail: String,
        var amiiboSeries: String?,
        var character: String?,
        var gameSeries: String?,
        var head: String?,
        var image: String?,
        var name: String?,
        var type: String?,
        var isPurchased: Boolean?,
        var localImagePath: String?,
        var userCreated: Boolean?,
        @Ignore var localImage: Bitmap?
) {
        constructor(tail: String) : this(
                tail,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                null,
                false,
                null
        )
        constructor() : this(
                "",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                null,
                false,
                null
        )
}

