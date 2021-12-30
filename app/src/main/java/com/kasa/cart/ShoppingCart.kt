package com.kasa.cart

import android.util.Log.i
import com.kasa.databases.DB
import com.kasa.models.CartItem
import com.kasa.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ShoppingCart
@Inject constructor(private val db: DB) {


    suspend fun incrementItemInCart(productId: Long) {
        val itemWithQuantity: CartItem =
            db.getCartDao().findById(productId) ?:CartItem( productId, 0)

        val updatedItemWithQuantity =
            itemWithQuantity.copy(quantity = itemWithQuantity.quantity + 1)
//        Log.i( Constants.TAG,updatedItemWithQuantity.toString())
        db.getCartDao().upsert(updatedItemWithQuantity)
    }


    /**
     * Does an Insert/Update and decrements the quantity by 1
     */
    suspend fun decrementItemInCart(productId: Long) {
        val foundItem = db.getCartDao().findById(productId)

        i( Constants.TAG,foundItem.toString())
        if (foundItem == null) {
             i( Constants.TAG,"Item didn't exist.  This must have been called by error.")
        } else {
            val newQuantity = foundItem.quantity - 1
            if (newQuantity <= 0L) {
                db.getCartDao().remove(foundItem.productId)
            } else {
                db.getCartDao().upsert(foundItem.copy(quantity = newQuantity))
            }
        }
    }



    /**
     * Exposes a current list of the Items in the Cart
     */
    suspend fun itemsInCart() = db.getCartDao().allItems().first()


    /**
     * Exposes a reactive stream via a [Flow]
     * which can be subscribed to, to get Shopping Cart updates
     */
    val itemsInCart = db.getCartDao().allItems()


    val getTotalAmount: Flow<Double> = db.getCartDao().allItems().map {
        it.map {
            it.product.price!!.times(it.cartItem.quantity)
        }.reduce { acc, d -> acc.plus(d)  }
    }



    val getItemsCountFlow: Flow<Int> = db.getCartDao().getItemsCount()
    suspend fun getItemsCount() = db.getCartDao().getItemsCount().first()


    /**
     * Empties the [ShoppingCart]
     */
    suspend fun empty() {
        db.getCartDao().empty()
    }
}