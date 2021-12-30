package com.tada

import android.app.Activity
import androidx.multidex.MultiDexApplication
import androidx.paging.ExperimentalPagingApi
import com.tada.dagger.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

@ExperimentalPagingApi
class App : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>


    override fun onCreate() {
        super.onCreate()


        AppInjector.init(this)


    }


    override fun activityInjector() = dispatchingAndroidInjector
}