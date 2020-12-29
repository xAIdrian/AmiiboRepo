package com.amohnacs.amiiborepo.domain

import com.amohnacs.amiiborepo.model.AmiiboResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

interface AmiiboService {

    @GET("api/amiibo/")
    fun getAmiibosResponse(): Single<AmiiboResponse>
}