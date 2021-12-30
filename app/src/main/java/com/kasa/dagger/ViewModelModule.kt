package com.kasa.dagger


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.kasa.ViewModelFactory
import com.kasa.account.OrdersViewModel
import com.kasa.auth.AuthViewModel
import com.kasa.cart.CartViewModel
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
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindRegisterViewModel(authViewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel



    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    abstract fun bindCartViewModel(cartViewModel: CartViewModel): ViewModel



    @ExperimentalPagingApi
    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel::class)
    abstract fun bindShopViewModel(viewModel: ProductViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModel::class)
    abstract fun bindOrdersViewModel(viewModel: OrdersViewModel): ViewModel



    @Binds
    @Singleton
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}
