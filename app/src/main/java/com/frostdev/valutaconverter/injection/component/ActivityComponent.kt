package com.frostdev.valutaconverter.injection.component

import android.content.Context
import com.frostdev.valutaconverter.database.Database
import com.frostdev.valutaconverter.database.RatesResponseDao
import com.frostdev.valutaconverter.main.MainActivity
import com.frostdev.valutaconverter.injection.NetworkModule
import com.frostdev.valutaconverter.injection.module.ActivityModule
import com.frostdev.valutaconverter.api.RateService
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class, NetworkModule::class))
interface ActivityComponent : ActivityComponentProvides {
    fun inject(activity: MainActivity)
}

interface ActivityComponentProvides {

    fun activityContext(): Context

    fun rateService(): RateService

    fun dao(): RatesResponseDao

}