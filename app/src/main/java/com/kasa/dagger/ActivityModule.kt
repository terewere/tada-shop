package com.kasa.dagger

import androidx.paging.ExperimentalPagingApi
import com.kasa.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector
@ExperimentalPagingApi
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

//    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
//    abstract fun contributeMessageActivity(): MessageActivity


}
