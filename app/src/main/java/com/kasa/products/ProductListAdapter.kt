package com.kasa.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kasa.databinding.ItemProductBinding
import com.kasa.models.ProductWithImages

class ProductListAdapter(val listener: OnItemClickListener) :
    PagingDataAdapter<ProductWithImages, ProductViewHolder>(
        movieDiffCallback
    ) {


    private var binding: ItemProductBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ProductViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val cat = getItem(position)
        if (cat != null) {
            holder.render(cat, listener)
        } else holder.clear()
    }


    companion object {
        private val movieDiffCallback = object : DiffUtil.ItemCallback<ProductWithImages>() {
            override fun areItemsTheSame(oldProduct: ProductWithImages, newProduct: ProductWithImages): Boolean {
                return oldProduct.product.id == newProduct.product.id

            }

            override fun areContentsTheSame(oldProduct: ProductWithImages, newProduct: ProductWithImages): Boolean {
                return oldProduct == newProduct
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(productWithImages: ProductWithImages)
    }

}

