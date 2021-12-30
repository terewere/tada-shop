package com.kasa.products

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.kasa.auth.ApiResult
import com.kasa.cart.ShoppingCart
import com.kasa.utils.Constants
import kotlinx.coroutines.launch
import javax.inject.Inject


@ExperimentalPagingApi
class ProductViewModel
@Inject
constructor(
    private val repo: ProductRepository,
    private val cart: ShoppingCart
) : ViewModel() {


    private var savedState: SavedStateHandle = SavedStateHandle()


    private val _slides = MutableLiveData<ApiResult<List<String>>>()
    var slides: LiveData<ApiResult<List<String>>> = _slides

    fun getProducts(categoryId: Int) = repo.getProducts(categoryId).cachedIn(viewModelScope)
    fun getAllProducts() = repo.getAllProducts().cachedIn(viewModelScope)


    fun addProfit( profit: Float) {
//        i(TAG, "SET STATUS CHANNEL ${toChannel}")
            savedState["profit"] = profit

    }

    fun getProfitLiveData(): LiveData<Float> {
        return savedState.getLiveData<Float>("profit")
    }


    fun getProfit(): String {
        return savedState.get("profit")?:""
    }



    suspend fun incrementItemInCart(productId: Long) = cart.incrementItemInCart(productId)


    fun getHomeSlides() {

        viewModelScope.launch {
            try {

                val slides = repo.getHomeSlides()

               // Log.i(Constants.TAG, slides.toString())

                _slides.postValue(ApiResult(success = slides ))

            } catch (e: Throwable) {
                _slides.postValue(ApiResult())
               // Log.i(Constants.TAG, e.localizedMessage)

            }
        }
    }

}

