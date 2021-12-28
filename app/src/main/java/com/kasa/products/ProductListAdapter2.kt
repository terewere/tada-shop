//package com.kasa.products
//
//import android.util.Log.i
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//import com.kasa.R
//import com.kasa.databinding.ItemProductBinding
//import com.kasa.models.ProductWithImages
//
//class ProductListAdapter2(val listener: OnItemClickListener) :
//    PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(
//        movieDiffCallback
//    ) {
//
//
//    private var binding: ItemProductBinding? = null
//
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return if (viewType == R.layout.item_web_app) {
//
//            binding = ItemProductBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent, false
//        )
//            ProductViewHolder(binding!!)
//        } else {
//            SeparatorViewHolder.create(parent)
//        }
//    }
//
//    override fun getItemViewType(position: Int): Int {
//
//        i("testing", position.toString())
//        i("testing", getItem(position).toString())
//        return when (getItem(position)) {
//            is UiModel.ItemModel -> R.layout.item_web_app
//            is UiModel.SeparatorItem -> R.layout.separator_view_item
//            null -> throw UnsupportedOperationException("Unknown view")
//        }
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        val uiModel = getItem(position)
//        uiModel.let {
//            when (uiModel) {
//                is UiModel.ItemModel -> (holder as ProductViewHolder).render(uiModel.model, listener)
//                is UiModel.SeparatorItem -> (holder as SeparatorViewHolder).bind(uiModel.description)
//            }
//        }
//    }
//
//
//
//
//    companion object {
//        private val movieDiffCallback = object : DiffUtil.ItemCallback<UiModel>() {
//            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
//
//                val isSameRepoItem = oldItem is UiModel.ItemModel
//                        && newItem is UiModel.ItemModel
//                        && oldItem.model.product.id == newItem.model.product.id
//
//                val isSameSeparatorItem = oldItem is UiModel.SeparatorItem
//                        && newItem is UiModel.SeparatorItem
//                        && oldItem.description == newItem.description
//
//                return isSameRepoItem || isSameSeparatorItem
//
//            }
//
//            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//
//    interface OnItemClickListener {
//        fun onItemClicked(productWithImages: ProductWithImages)
//    }
//
//
//
//}
//
//
//class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//    private val description: TextView = view.findViewById(R.id.separator_description)
//
//    fun bind(separatorText: String) {
//        description.text = separatorText
//    }
//
//    companion object {
//        fun create(parent: ViewGroup): SeparatorViewHolder {
//            val view = LayoutInflater.from(parent.context)
//                .inflate(R.layout.separator_view_item, parent, false)
//            return SeparatorViewHolder(view)
//        }
//    }
//}