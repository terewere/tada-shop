package com.kasa.products

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import com.kasa.auth.ApiResult
import com.kasa.models.Order
import com.kasa.models.ProductWithImages
import com.kasa.utils.Constants
import kotlinx.coroutines.launch
import java.text.ParseException
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


    fun getCartItems() = cart.itemsInCart


    suspend fun getItemsCountFlow() = cart.getItemsCount
    suspend fun getTotalAmount() = cart.getTotalAmount

    suspend fun incrementItemInCart(productId: Long) = cart.incrementItemInCart(productId)
    suspend fun decrementItemInCart(productId: Long) = cart.decrementItemInCart(productId)
    suspend fun itemsInCart() = cart.itemsInCart()
    suspend fun empty() = cart.empty()



    fun getHomeSlides() {

        viewModelScope.launch {
            try {

                val slides = repo.getHomeSlides()

                Log.i(Constants.TAG, slides.toString())

                _slides.postValue(ApiResult(success = slides ))

            } catch (e: Throwable) {
                _slides.postValue(ApiResult())
               // Log.i(Constants.TAG, e.localizedMessage)

            }
        }
    }

}

