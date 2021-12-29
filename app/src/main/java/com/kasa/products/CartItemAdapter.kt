package com.kasa.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kasa.R
import com.kasa.databinding.CartListItemBinding
import com.kasa.models.CartItemWithProduct
import com.squareup.picasso.Picasso

class CartItemAdapter(private val listener: OnClickListener )
	:RecyclerView.Adapter<CartItemAdapter.ViewHolder>() {


	var data: List<CartItemWithProduct> = mutableListOf()

	inner class ViewHolder(private val binding: CartListItemBinding) :
		RecyclerView.ViewHolder(binding.root) {
		fun bind(itemData: CartItemWithProduct) {
			val product = itemData.product

			binding.cartProductTitleTv.text = product.label
			binding.cartProductPriceTv.text =
				binding.root.context.getString(R.string.price, product.price.toString())
				Picasso.get().load(product.imgUrl).into(binding.productImageView)
				//binding.productImageView.clipToOutline = true

			binding.cartProductQuantityTextView.text = itemData.cartItem.quantity.toString()


			binding.cartProductPlusBtn.setOnClickListener {
				listener.onPlusClick(product.id)
			}
			binding.cartProductMinusBtn.setOnClickListener {
				listener.onMinusClick(product.id, itemData.cartItem.quantity)
			}

		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(
			CartListItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(data[position])
	}

	override fun getItemCount() = data.size

	fun submitData(items: List<CartItemWithProduct>){
		data = items
		notifyDataSetChanged()
	}



	interface OnClickListener {
		fun onPlusClick(itemId: Long)
		fun onMinusClick(itemId: Long, currQuantity: Long)
	}
}