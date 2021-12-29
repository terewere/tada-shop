package com.kasa.products

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.widget.ViewPager2
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentProductDetailBinding
import com.kasa.models.ProductWithImages
import com.kasa.utils.Constants
import com.kasa.utils.extentions.setTitle
import kotlinx.coroutines.launch


@ExperimentalPagingApi
class ProductDetailFragment : Fragment(), Injectable {


	private lateinit var binding: FragmentProductDetailBinding

	private var productWithImages: ProductWithImages? = null


	private var productItemListAdapter: CarouselAdapter? = null


	val handler by lazy {
		Handler(Looper.getMainLooper())
	}
	val runnable by lazy {
		Runnable {
			binding.pager.setCurrentItem((binding.pager.currentItem + 1), true)

		}
	}

	private val viewModel: ProductViewModel by navGraphViewModels(R.id.mobile_navigation)


	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = FragmentProductDetailBinding.inflate(layoutInflater)

		arguments?.let {
			productWithImages = it.getParcelable<ProductWithImages>(Constants.ARG_PRODUCT) as ProductWithImages

		}


		PriceShareBottomSheet(binding, viewModel,productWithImages?.product!!, this)

		binding.detailCheckOutBtn.setOnClickListener {

			viewLifecycleOwner.lifecycleScope.launch {
				viewModel.incrementItemInCart(productWithImages?.product!!.id )
				findNavController().navigate(R.id.nav_cart)
			}

		}

		return binding.root
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val product = productWithImages?.product!!

		viewModel.addProfit(0f)

		binding.detailTitleTv.text = product.label
		binding.detailPriceTv.text = getString(R.string.price, product.price.toString())
		binding.detailDescText.text = product.description


		val imgList = productWithImages?.productImages?.map { it.imgUrl}

		imgList?.let {
			initImageSlider(it.toMutableList())
		}


	}




	override fun onResume() {
		super.onResume()
		setTitle(productWithImages?.product!!.label)

	}


	private fun initImageSlider(imgList: MutableList<String?>) {


//		binding.pager.clipToPadding = false
//		binding.pager.clipChildren = false
//		binding.pager.offscreenPageLimit = 3



//        binding.pager.setPageTransformer(MarginPageTransformer(10))
//		binding.pager.setPageTransformer { page, position ->
//
//			val r = 1 - abs(position)
//			page.scaleY = 0.75f +r * 0.15f
//
//		}

		binding.pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {

			override fun onPageSelected(position: Int) {
				super.onPageSelected(position)
				handler.removeCallbacks(runnable)
				handler.postDelayed(runnable, 5000)
			}
		})

		productItemListAdapter = CarouselAdapter( binding.pager)
		binding.pager.adapter = productItemListAdapter
		binding.pageIndicator.setViewPager(binding.pager, imgList.size)


		productItemListAdapter?.submitList(imgList)
		binding.pager.currentItem = 1

	}



}