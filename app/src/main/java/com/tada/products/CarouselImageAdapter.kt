package com.tada.products


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.tada.databinding.ItemCarouselBinding
import com.tada.utils.Constants.TAG
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception


class CarouselAdapter( val pager: ViewPager2) :
    RecyclerView.Adapter<CarouselAdapter.Holder>() {
    var imgList: MutableList<String?> = mutableListOf()


    private var binding: ItemCarouselBinding? = null


    val runnable by lazy {
       Runnable {
           val copy: MutableList<String?> = mutableListOf()
           copy.addAll(imgList)

//           adsList.clear()
           imgList.addAll(copy)
           copy.clear()
           Log.d(TAG, "size: ${imgList.size}")
           notifyDataSetChanged()
       }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {


        binding = ItemCarouselBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return Holder(binding!!)
    }

    fun submitList(list: MutableList<String?>) {
        imgList = list

        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val imgUrl = imgList[position]
        if (position == imgList.size -2) {
            pager.post(runnable)
        }



        if (imgUrl != null) {
            holder.render(imgUrl)

        } else holder.clear()

    }

    class Holder(itemView: ItemCarouselBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private val binding: ItemCarouselBinding = itemView

        fun render(imageUrl: String) = itemView.run {


            Picasso.get().load(imageUrl).into(object : Target {
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    binding.loadingIndicator.setVisibility(View.GONE)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    binding.slide.setImageBitmap(bitmap)
                    binding.loadingIndicator.setVisibility(View.GONE)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    binding.loadingIndicator.setVisibility(View.VISIBLE)
                }

            })

//            itemView.setOnClickListener {
//                openLink(activity, adsImage)
//            }

//            adsImg.setOnClickListener {
//                openLink(activity, adsImage)
//            }
        }


        fun clear() = itemView.run {
            binding.slide.invalidate()
        }

    }

    override fun getItemCount() = imgList.size

}

