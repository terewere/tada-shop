package com.kasa

import android.app.Activity
import android.util.Log
import androidx.multidex.MultiDexApplication
import androidx.paging.ExperimentalPagingApi
import com.kasa.dagger.AppInjector
import com.kasa.utils.Constants.TAG
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.plugins.RxJavaPlugins
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