package com.kasa.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.widget.ViewPager2
import com.kasa.R
import com.kasa.account.OrdersViewModel
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentHomeBinding
import com.kasa.databinding.FragmentProductDetailBinding
import com.kasa.models.ProductWithImages
import com.kasa.products.CarouselAdapter
import com.kasa.products.PriceShareBottomSheet
import com.kasa.products.ProductViewModel
import com.kasa.utils.Constants
import com.kasa.utils.extentions.setTitle
import kotlinx.coroutines.launch
import java.lang.Math.abs


@ExperimentalPagingApi
class HomeFragment : Fragment(), Injectable {


    private lateinit var binding: FragmentHomeBinding


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

        binding = FragmentHomeBinding.inflate(layoutInflater)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getOrders()
        viewModel.getHomeSlides()

    }






    private fun getOrders() {
        viewModel.slides.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            if( it.success != null) {

                Log.i(Constants.TAG, it.success.toString())

//                binding.progressBar.hide()
//                binding.loading.circularLoader.isVisible = false

                    initImageSlider(it.success.toMutableList())

            } else {
                Toast.makeText(context,"Something went wrong", Toast.LENGTH_LONG).show()
            }

        }
        )

    }


    private fun initImageSlider(imgList: MutableList<String?>) {

		binding.pager.clipToPadding = false
		binding.pager.clipChildren = false
		binding.pager.offscreenPageLimit = 3


		binding.pager.setPageTransformer { page, position ->

			val r = 1 - abs(position)
			page.scaleY = 0.75f +r * 0.15f

		}

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