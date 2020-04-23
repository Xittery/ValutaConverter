package com.frostdev.valutaconverter.injection.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.frostdev.valutaconverter.database.Database
import com.frostdev.valutaconverter.database.RatesResponseDao

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    internal fun provideActivityContext(): Context = activity

    @Provides
    fun provideRatesresponseDatabase(): Database = Room.databaseBuilder(activity,
        Database::class.java, "ratesresponse_db").allowMainThreadQueries().build()

    @Provides
    fun provideRatesResponseDao(database: Database): RatesResponseDao = database.ratesResponseDao()
}
