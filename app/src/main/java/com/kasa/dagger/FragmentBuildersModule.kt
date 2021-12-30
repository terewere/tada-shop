package com.kasa.dagger

import androidx.paging.ExperimentalPagingApi
import com.kasa.SplashFragment
import com.kasa.account.OrdersFragment
import com.kasa.auth.*
import com.kasa.category.CategoryFragment
import com.kasa.home.HomeFragment
import com.kasa.products.ProductDetailFragment
import com.kasa.products.ProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {


    @ExperimentalPagingApi
    @ContributesAndroidInjector
    abstract fun contributeWebAppFragment(): ProductFragment

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @ExperimentalPagingApi
    @ContributesAndroidInjector
    abstract fun contributeProductDetailFragment(): ProductDetailFragment


    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryFragment

    @ExperimentalPagingApi
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeAccountFragment(): OrdersFragment


}
