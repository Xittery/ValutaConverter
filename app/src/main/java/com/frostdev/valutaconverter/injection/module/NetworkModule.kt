package com.frostdev.valutaconverter.injection

import com.frostdev.valutaconverter.api.RateService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule() {

    @Provides
    internal fun provideRatesApi(retrofit: Retrofit): RateService {
        return retrofit.create(RateService::class.java)
    }

    @Provides
    internal fun provideRetrofitInterface(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hiring.revolut.codes")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
}