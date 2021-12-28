package com.kasa.category

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kasa.R
import com.kasa.databinding.ItemCategoryBinding
import com.kasa.models.Category
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception


class CategoryViewHolder(itemView: ItemCategoryBinding) :

    RecyclerView.ViewHolder(itemView.root) {
    private val binding: ItemCategoryBinding = itemView

    fun render(
        category: Category,
        listener: CategoryListAdapter.OnCategoryClickListener
    )  {

//        Picasso.get().load(category.imgUrl)
//            .placeholder(R.drawable.progress_animation)
//            .into(binding.categoryImageView)

        Picasso.get().load(category.imgUrl).into(binding.categoryImageView, object : Callback {
            override fun onSuccess() {
                binding.loadingIndicator.setVisibility(View.GONE)
            }

            override fun onError(e: Exception?) {}
        })

        binding.categoryNameTv.text= category.label

        itemView.setOnClickListener {
            listener.onCategoryClicked(category)
        }

    }


}
