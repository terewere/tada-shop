package com.tada.products


import android.os.Bundle
import android.util.Log.i
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.tada.R
import com.tada.dagger.Injectable
import com.tada.databinding.FragmentProductBinding
import com.tada.models.Category
import com.tada.models.ProductWithImages
import com.tada.utils.Constants
import com.tada.utils.Constants.ARG_CATEGORY
import com.tada.utils.Constants.ARG_PRODUCT
import com.tada.utils.extentions.setTitle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class ProductFragment : Fragment(),
    Injectable, ProductListAdapter.OnItemClickListener {


    private val viewModel: ProductViewModel by navGraphViewModels(R.id.mobile_navigation)


    private var _binding: FragmentProductBinding? = null
    private var category: Category? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        ProductListAdapter(
            this
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getParcelable<Category>(ARG_CATEGORY) as Category

            i(Constants.TAG, category.toString())

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductBinding.inflate(inflater, container, false)
        val view = binding.root


        initRecycler()
        getProducts()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initRecycler() {
        binding.recycler.apply {
            setHasFixedSize(true)

            adapter = listAdapter.withLoadStateFooter(
                footer = ProductLoadingStateAdapter(listAdapter)
            )

            layoutManager = GridLayoutManager(context, 2)
        }

    }


    override fun onResume() {
        super.onResume()
        setTitle(category!!.label)
    }

    private fun getProducts() {
        if (category == null) return
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getProducts(category!!.id).distinctUntilChanged().collectLatest {
                // i("testing", it.toString())
                listAdapter.submitData(it)


            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            listAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.loading.circularLoader.isVisible = loadStates.refresh is LoadState.Loading
//                binding.retryButton.isVisible = loadStates.refresh !is LoadState.Loading
//                binding.errorMsg.isVisible = loadStates.refresh is LoadState.Error
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!isAdded) return
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!isAdded) return false
        return false

    }



    override fun onItemClicked(productWithImages: ProductWithImages) {
        val bundle = bundleOf(
            ARG_PRODUCT to productWithImages
        )

        i(Constants.TAG, productWithImages.product.label)

        findNavController().navigate(R.id.nav_product_detail, bundle)
    }
}