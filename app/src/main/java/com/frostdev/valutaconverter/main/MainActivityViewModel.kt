package com.frostdev.valutaconverter.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.frostdev.valutaconverter.model.RatesResponse
import com.frostdev.valutaconverter.model.SingleRate
import com.frostdev.valutaconverter.util.readJsonAsset
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class MainActivityViewModel(): ViewModel() {
    var converterList: MutableList<SingleRate>? = null
    var converterAdapter: ConverterAdapter =
        ConverterAdapter()

    private lateinit var compositeDisposable: CompositeDisposable
    var baseCurrency = "EUR"
    var hasChanged = false

    init{
        converterAdapter.setHasStableIds(true)
        loadRates()
    }

    private fun loadRates() {
        compositeDisposable = CompositeDisposable()
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS).subscribe{
            MainActivity.activityComponent.rateService().getRates(baseCurrency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ rates -> run {
                    MainActivity.mActivity.runOnUiThread {
                        MainActivity.activityComponent.dao().insertRatesResponse(rates)
                        MainActivity.activityComponent.dao().loadRates()
                            .observe(MainActivity.mActivity, Observer {
                                display(it)
                                MainActivity.activityComponent.dao().deleteRates()
                            })
                    }
                }}})
    }

    private fun display(rateRespons: RatesResponse?) {
        //Get remaining values for SingleRates
        if(rateRespons != null) {
            converterList = mutableListOf<SingleRate>()
            val currencyInfo =
                JSONObject(MainActivity.activityComponent.activityContext().readJsonAsset("currency.json"))
            var id: Long = 2
            var singleRate: SingleRate?
            baseCurrency = converterAdapter.getFocusValuta()
            rateRespons.rates.entries.forEach {
                val currencyInfoObject = currencyInfo.getJSONObject(it.key)
                val amount = it.value * converterAdapter.getInputAmount()
                println(converterAdapter.getInputAmount())
                converterList!!.add(
                    SingleRate(
                        id, it.key, amount, currencyInfoObject.getString("name"),
                        currencyInfoObject.getString("symbol_native")
                    )
                )
                id++

            }
            val currencyInfoObject = currencyInfo.getJSONObject(baseCurrency)
            singleRate = SingleRate(
                1,
                baseCurrency,
                1f,
                currencyInfoObject.getString("name"),
                currencyInfoObject.getString("symbol_native")
            )
            converterList!!.add(0, singleRate)
            converterAdapter.updateConverterList(converterList!!)
        }
    }
}