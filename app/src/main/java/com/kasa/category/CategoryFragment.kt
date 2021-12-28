package com.kasa.category


import android.os.Bundle
import android.util.Log.i
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.ActivityMainBinding
import com.kasa.databinding.FragmentCategoryBinding
import com.kasa.models.Category
import com.kasa.network.Resource
import com.kasa.products.*
import com.kasa.utils.Constants.ARG_CATEGORY
import com.kasa.utils.Constants.TAG
import javax.inject.Inject

class CategoryFragment : Fragment(),
    Injectable, CategoryListAdapter.OnCategoryClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


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
                    }

                    else -> {}
                }
            })

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!isAdded) return
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!isAdded) return false
        return false

    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }


    override fun onCategoryClicked(category: Category) {

                val bundle = bundleOf(
            ARG_CATEGORY to category
        )

        i(TAG, category.label)
        i(TAG, category.id.toString())

        findNavController().navigate(R.id.nav_products, bundle)

    }
}