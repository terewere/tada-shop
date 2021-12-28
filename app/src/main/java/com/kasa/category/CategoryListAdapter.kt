package com.kasa.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kasa.databinding.ItemCategoryBinding
import com.kasa.models.Category

class CategoryListAdapter(val listener: (OnCategoryClickListener)) :
    RecyclerView.Adapter<CategoryViewHolder>() {


    private var items: List<Category> = emptyList()

    private var binding: ItemCategoryBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {

        binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return CategoryViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.render(items[position], listener)
    }


    interface OnCategoryClickListener {
        fun onCategoryClicked(category: Category)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(list: List<Category>) {
        notifyDataSetChanged()
        items = list
    }

}

