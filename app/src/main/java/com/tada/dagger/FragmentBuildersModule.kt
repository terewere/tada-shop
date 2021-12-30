package com.tada.dagger

import androidx.paging.ExperimentalPagingApi
import com.tada.SplashFragment
import com.tada.account.OrdersFragment
import com.tada.auth.*
import com.tada.cart.CartFragment
import com.tada.category.CategoryFragment
import com.tada.home.HomeFragment
import com.tada.products.ProductDetailFragment
import com.tada.products.ProductFragment
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
    abstract fun contributeCartFragment(): CartFragment


    @ContributesAndroidInjector
    abstract fun contributeAccountFragment(): OrdersFragment


}
