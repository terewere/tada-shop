package com.tada.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tada.R
import com.tada.databinding.LayoutPriceCardBinding

class PriceCardAdapter() : RecyclerView.Adapter<PriceCardAdapter.ViewHolder>() {

    var mCount = 0
    var mTotalAmount = 0.0
    inner class ViewHolder(private val priceCardBinding: LayoutPriceCardBinding) :
        RecyclerView.ViewHolder(priceCardBinding.root) {

        fun bind() {
            val context = priceCardBinding.root.context
            priceCardBinding.priceItemsLabelTv.text =  context.getString(R.string.price_card_items_string,mCount.toString())
            priceCardBinding.priceItemsAmountTv.text = context.getString(R.string.price, mTotalAmount.toString())
            priceCardBinding.priceTotalAmountTv.text = context.getString(R.string.price, mTotalAmount.toString())
        }
    }

    fun setItemsCountFlow(count: Int) {
        mCount = count
    }

    fun setTotalAmount(amount: Double) {
        mTotalAmount = amount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutPriceCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 1

}