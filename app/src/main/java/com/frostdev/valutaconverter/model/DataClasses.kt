package com.frostdev.valutaconverter.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class SingleRate(var id: Long, var abbreviation: String, var rate: Float, var currencyName: String = "", var currencySymbol: String = "")

@Entity(tableName = "rates")
data class RatesResponse(
    @PrimaryKey
    @SerializedName("baseCurrency")var baseCurrency: String,
    @SerializedName("rates")var rates: Map<String, Float>)