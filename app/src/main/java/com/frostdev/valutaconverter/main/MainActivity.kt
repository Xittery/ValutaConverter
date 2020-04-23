package com.frostdev.valutaconverter.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.frostdev.valutaconverter.R
import com.frostdev.valutaconverter.databinding.ActivityMainBinding
import com.frostdev.valutaconverter.injection.NetworkModule
import com.frostdev.valutaconverter.injection.component.ActivityComponent
import com.frostdev.valutaconverter.injection.component.DaggerActivityComponent
import com.frostdev.valutaconverter.injection.module.ActivityModule


class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var activityComponent: ActivityComponent
            private set

        lateinit var mActivity: MainActivity
            private set
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(mActivity))
            .networkModule(NetworkModule())
            .build()
        val viewModel = MainActivityViewModel()
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_main)
        binding.viewModel = viewModel
    }

}
