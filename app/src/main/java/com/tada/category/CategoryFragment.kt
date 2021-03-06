package com.tada.category


import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.tada.R
import com.tada.dagger.Injectable
import com.tada.databinding.FragmentCategoryBinding
import com.tada.models.Category
import com.tada.network.Resource
import com.tada.network.TokenManager
import com.tada.products.*
import com.tada.utils.Constants.ARG_CATEGORY
import javax.inject.Inject

class CategoryFragment : Fragment(),
    Injectable, CategoryListAdapter.OnCategoryClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var viewModel: CategoryViewModel

    private var _binding: FragmentCategoryBinding? = null

    private val binding get() = _binding!!

    private val listAdapter by lazy {
        CategoryListAdapter(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        val view = binding.root

        initRecycler()
        initViewModel()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)
        getCategories()
    }


    private fun initRecycler() {
        binding.recycler.apply {
            setHasFixedSize(true)
            adapter = listAdapter
            layoutManager = GridLayoutManager(context, 2)
        }


    }

    private fun getCategories() {
        viewModel.getCategories()
            .observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
                it.data ?: return@Observer


                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        it.data.forEach {
                            //i(TAG, it.label)
                        }
                        listAdapter.submitList(it.data)
                        binding.progressBar.hide()
                        binding.loading.circularLoader.isVisible = false

                    }

                    else -> {}
                }
            })

    }


    override fun onCategoryClicked(category: Category) {

                val bundle = bundleOf(
            ARG_CATEGORY to category
        )

        findNavController().navigate(R.id.nav_products, bundle)

    }
}