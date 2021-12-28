package com.kasa.dagger

import androidx.paging.ExperimentalPagingApi
import com.kasa.auth.*
import com.kasa.category.CategoryFragment
import com.kasa.products.ProductDetailFragment
import com.kasa.products.ProductFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {


    @ExperimentalPagingApi
    @ContributesAndroidInjector
    abstract fun contributeWebAppFragment(): ProductFragment


    @ExperimentalPagingApi
    @ContributesAndroidInjector
    abstract fun contributeProductDetailFragment(): ProductDetailFragment


    @ContributesAndroidInjector
    abstract fun contributeVerifyPhoneFragment(): VerifyPhoneFragment

    @ContributesAndroidInjector
    abstract fun contributeVerifyTokenFragment(): VerifyTokenFragment

    @ContributesAndroidInjector
    abstract fun contributeRegisterFragment(): UpdateProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeWelcomeFragment(): WelcomeFragment

    @ContributesAndroidInjector
    abstract fun contributeCategoryFragment(): CategoryFragment

    @ContributesAndroidInjector
    abstract fun contributeCountryPickerFragment(): CountryPickerFragment

}
