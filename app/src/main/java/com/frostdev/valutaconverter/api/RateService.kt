package com.frostdev.valutaconverter.api

import com.frostdev.valutaconverter.model.RatesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RateService {
    @GET("/api/android/latest")
    fun getRates(@Query("base") currency: String) : Observable<RatesResponse>
}