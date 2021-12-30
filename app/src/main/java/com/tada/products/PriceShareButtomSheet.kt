package com.tada.products

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.LifecycleOwner
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.slider.Slider
import com.tada.R
import com.tada.databinding.FragmentProductDetailBinding
import com.tada.models.Product
import com.tada.utils.Util.getLocalBitmapUri
import com.tada.utils.Util.shareProduct
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.lang.Exception

@ExperimentalPagingApi
class PriceShareBottomSheet constructor(
    val binding: FragmentProductDetailBinding,
    private val viewModel: ProductViewModel,
    private val product: Product,
    private val viewLifecycleOwner: LifecycleOwner,

    ) {

    private var mBottomSheetLayout: CoordinatorLayout? = null
    private var sheetBehavior: BottomSheetBehavior<*>? = null


    private var mShareProductTitleTV: TextView? = null
    private var mShareProductPriceTV: TextView? = null

    private var mProfitTV: TextView? = null
    private var mPriceShareTV: TextView? = null
    private var mTotalPriceTV: TextView? = null
    private var mAmountProfitTV: TextView? = null
    private var mSlider: Slider? = null
    private var mItemImage: ImageView? = null
    private var mShareBtn: ExtendedFloatingActionButton? = null
    private var mBitmap: Bitmap? = null

    
    init{

        val view = binding.root
        val context = view.context

        mBottomSheetLayout = view.findViewById(R.id.bottom_sheet_layout);

        mShareProductTitleTV = view.findViewById(R.id.tv_title_bottom);
        mShareProductPriceTV = view.findViewById(R.id.tv_price);

        mShareProductTitleTV!!.text = product.label
        mShareProductPriceTV!!.text = context.getString(R.string.price, product.price.toString())


        mProfitTV = view.findViewById(R.id.tv_my_profit);
        mPriceShareTV = view.findViewById(R.id.tv_price_share);
        mTotalPriceTV = view.findViewById(R.id.tv_total_price);
        mAmountProfitTV = view.findViewById(R.id.tv_amount_profit);


        mSlider = view.findViewById(R.id.slider);
        mItemImage = view.findViewById(R.id.img_image);
        mShareBtn = view.findViewById(R.id.product_share_btn);

        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout as CoordinatorLayout);


        binding.shareProductBtn.setOnClickListener {

            val price = product.price!!

            mSlider!!.valueTo = price.toFloat()

            mSlider!!.addOnChangeListener(Slider.OnChangeListener { slider, value, fromUser ->

                viewModel.addProfit(value)

                Log.i("testing", value.toString())
            })

            mPriceShareTV!!.text = context.getString(R.string.price, price.toString())
            viewModel.getProfitLiveData().observe(viewLifecycleOwner,{
                mProfitTV!!.text = context.getString(R.string.price, it.toString())
                mAmountProfitTV!!.text = context.getString(R.string.profit, it.toString())
                mTotalPriceTV!!.text =context.getString(R.string.price, it.plus(price).toString())
            })

            if(sheetBehavior?.state != BottomSheetBehavior.STATE_EXPANDED){
                sheetBehavior?.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior?.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }


        }

        Picasso.get().load(product.imgUrl).into(object : Target {
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                mItemImage!!.setImageBitmap(bitmap)
                mBitmap = bitmap
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }
        })
        
        mShareBtn!!.setOnClickListener {
            mBitmap?.let {

                val title = "${product.label} ${mTotalPriceTV!!.text}"
                val imgUri = getLocalBitmapUri(context, it, title)

                    shareProduct(context, title, imgUri, )
            }

        }
    }



}