package com.kasa.products

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kasa.R
import com.kasa.databinding.ItemProductBinding
import com.kasa.models.ProductWithImages
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception


class ProductViewHolder(itemView: ItemProductBinding) :

    RecyclerView.ViewHolder(itemView.root) {
    private val binding: ItemProductBinding = itemView

    fun render(
        item: ProductWithImages,
        listener: ProductListAdapter.OnItemClickListener
    )  {


        Picasso.get().load(item.product.imgUrl).into(object : Target {
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                binding.loadingIndicator.setVisibility(View.GONE)
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                binding.productImageView.setImageBitmap(bitmap)
                binding.loadingIndicator.setVisibility(View.GONE)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                binding.loadingIndicator.setVisibility(View.VISIBLE)
            }

        })

        binding.productNameTv.text= item.product.label
        binding.productPriceTv.text = itemView.context.getString(R.string.price, item.product.price.toString())

        itemView.setOnClickListener {
            listener.onItemClicked(item)
        }

    }




    fun clear() = itemView.run {
       // binding.name.invalidate()

    }


}
