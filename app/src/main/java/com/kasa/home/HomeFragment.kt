package com.kasa.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentHomeBinding
import com.kasa.models.ProductWithImages
import com.kasa.products.*
import com.kasa.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.lang.Math.abs


@ExperimentalPagingApi
class HomeFragment : Fragment(), Injectable, ProductListAdapter.OnItemClickListener {


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


    private val listAdapter by lazy {
        ProductListAdapter(
            this
        )
    }


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
        initRecycler()
        getProducts()

    }


    private fun getProducts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllProducts().distinctUntilChanged().collectLatest {
                 i("testing", it.toString())
                listAdapter.submitData(it)

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            listAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
//                binding.retryButton.isVisible = loadStates.refresh !is LoadState.Loading
//                binding.errorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
    }


    private fun initRecycler() {
        binding.recyclerviewProducts.apply {
            setHasFixedSize(true)

            adapter = listAdapter.withLoadStateFooter(
                footer = ProductLoadingStateAdapter(listAdapter)
            )

            layoutManager = GridLayoutManager(context, 2)
        }

    }


    private fun getOrders() {
        viewModel.slides.observe(viewLifecycleOwner, Observer {
            it ?: return@Observer
            if (it.success != null) {

                initImageSlider(it.success.toMutableList())
            } else {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
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
            page.scaleY = 0.75f + r * 0.15f

        }

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 5000)
            }
        })

        productItemListAdapter = CarouselAdapter(binding.pager)
        binding.pager.adapter = productItemListAdapter
        binding.pageIndicator.setViewPager(binding.pager, imgList.size)


        productItemListAdapter?.submitList(imgList)
        binding.pager.currentItem = 1

    }


    override fun onItemClicked(productWithImages: ProductWithImages) {
        val bundle = bundleOf(
            Constants.ARG_PRODUCT to productWithImages
        )

        findNavController().navigate(R.id.nav_product_detail, bundle)
    }

}