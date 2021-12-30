package com.tada.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tada.R
import com.tada.databinding.ItemOrderBinding
import com.tada.models.Order
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class OrdersAdapter( val listener: (OnOrderClickListener) ) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    var mOrders = emptyList<Order>()
    inner class ViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            val context = binding.root.context

            Picasso.get().load(order.productUrl).into(binding.ivProductImg, object : Callback {
                override fun onSuccess() {
                    binding.loadingIndicator.setVisibility(View.GONE)
                }

                override fun onError(e: Exception?) {}
            })

            binding.tvProductName.text = order.productName
            binding.tvOrderId.text = order.id.toString()
            binding.tvQuantity.text = order.quantity.toString()
            binding.tvProfitFromCustomer.text = context.getString(R.string.price, order.total.toString())

            binding.btnTrackOrder.setOnClickListener {
                listener.onOrderClickListener(order.id)
            }
        }
    }

    fun submitData(list: List<Order>) {
        mOrders = list
        notifyDataSetChanged()
    }

    interface OnOrderClickListener {
        fun onOrderClickListener(orderId: String)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(mOrders[position])
    }

    override fun getItemCount() = mOrders.size;

}