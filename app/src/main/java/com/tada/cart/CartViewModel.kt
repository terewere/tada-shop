package com.tada.cart

import androidx.lifecycle.*
import javax.inject.Inject


class CartViewModel
@Inject
constructor(
    private val cart: ShoppingCart
) : ViewModel() {


    fun getCartItems() = cart.itemsInCart

    suspend fun getItemsCountFlow() = cart.getItemsCountFlow
    suspend fun getItemsCount() = cart.getItemsCount()
    suspend fun getTotalAmount() = cart.getTotalAmount

    suspend fun incrementItemInCart(productId: Long) = cart.incrementItemInCart(productId)
    suspend fun decrementItemInCart(productId: Long) = cart.decrementItemInCart(productId)
    suspend fun itemsInCart() = cart.itemsInCart()
    suspend fun empty() = cart.empty()


}

