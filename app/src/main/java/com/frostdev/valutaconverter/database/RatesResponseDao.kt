package com.frostdev.valutaconverter.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.frostdev.valutaconverter.model.RatesResponse

@Dao
interface RatesResponseDao {

    @Query("SELECT * FROM rates")
    fun loadRates(): LiveData<RatesResponse>

    @Query("DELETE FROM rates")
    fun deleteRates()

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    fun insertRatesResponse(ratesResponse: RatesResponse)
}
