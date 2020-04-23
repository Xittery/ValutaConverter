package com.frostdev.valutaconverter.main

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.frostdev.valutaconverter.R
import com.frostdev.valutaconverter.model.SingleRate

class RateViewModel(context: Context): ViewModel() {

    private val mContext = context

    private val abbreviation = MutableLiveData<String>()
    private val currencyName = MutableLiveData<String>()
    private val currencySymbol = MutableLiveData<String>()
    private val rate = MutableLiveData<String>()
    private val image = MutableLiveData<Drawable>()

    fun bind(singleRate: SingleRate){
        abbreviation.value = singleRate.abbreviation
        currencyName.value = singleRate.currencyName
        currencySymbol.value = singleRate.currencySymbol
        rate.value = singleRate.rate.toString()
        val imageIdentifier = mContext.resources.getIdentifier(
            getAbbrivation().value.toString().toLowerCase(), "drawable", mContext.packageName)
        image.value = mContext.getDrawable(imageIdentifier)!!
    }

    fun getSymbol(): MutableLiveData<String> {
        return currencySymbol
    }

    fun getImage(): MutableLiveData<Drawable> {
        return image
    }

    fun getAbbrivation():MutableLiveData<String>{
        return abbreviation
    }

    fun getCurrencyName():MutableLiveData<String>{
        return currencyName
    }

    fun getRate():MutableLiveData<String>{

        val modifiedRate = getFormattedRate(rate.value.toString())
        rate.value = modifiedRate;

        return rate
    }

    fun getTextColor(): Int{
        if(rate.value!!.toFloat() > 0f){
            return mContext.resources.getColor(R.color.colorAccent)
        }else{
            return mContext.resources.getColor(R.color.colorLightGrey)
        }
    }

    fun getFormattedRate(fullRate: String): String {
        var rateBeforeDecimal = fullRate.subSequence(0, fullRate.indexOf(".")).toString()
        var rateAfterDecimal = fullRate.subSequence(fullRate.indexOf(".")+1, fullRate.length).toString()
        if(rateAfterDecimal.length > 2)
            rateAfterDecimal = rateAfterDecimal.subSequence(0, 2).toString()
        else if(rateAfterDecimal.length < 2){
            while(rateAfterDecimal.length < 2){
                rateAfterDecimal += "0"
            }
        }
        return rateBeforeDecimal + "." + rateAfterDecimal
    }
}