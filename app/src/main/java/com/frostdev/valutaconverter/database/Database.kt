package com.frostdev.valutaconverter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frostdev.valutaconverter.model.RatesResponse

@Database(entities = arrayOf(RatesResponse::class), version = 1)
@TypeConverters(MapConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun ratesResponseDao(): RatesResponseDao
}