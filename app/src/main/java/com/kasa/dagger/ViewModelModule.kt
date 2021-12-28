package com.kasa.dagger


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.kasa.ViewModelFactory
import com.kasa.auth.UpdateProfileViewModel
import com.kasa.auth.VerifyPhoneViewModel
import com.kasa.auth.VerifyTokenViewModel
import com.kasa.category.CategoryViewModel
import com.kasa.products.ProductViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(VerifyPhoneViewModel::class)
    abstract fun bindVerifyPhoneViewModel(verifyPhoneViewModel: VerifyPhoneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerifyTokenViewModel::class)
    abstract fun bindVerifyTokenViewModel(verifyTokenViewModel: VerifyTokenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdateProfileViewModel::class)
    abstract fun bindRegisterViewModel(updateProfileViewModel: UpdateProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(updateProfileViewModel: CategoryViewModel): ViewModel



    @ExperimentalPagingApi
    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindShopViewModel(viewModel: ProductViewModel): ViewModel



    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}
