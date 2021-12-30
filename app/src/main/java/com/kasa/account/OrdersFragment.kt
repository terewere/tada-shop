package com.kasa.account

import android.os.Bundle
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentOrdersBinding
import com.kasa.utils.Constants.TAG
import com.kasa.utils.Util.trackOrderInfo
import javax.inject.Inject

class OrdersFragment : Fragment(), Injectable {


    private var _binding: FragmentOrdersBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OrdersViewModel

    private val listAdapter by lazy {
        OrdersAdapter(
            object: OrdersAdapter.OnOrderClickListener {
                override fun onOrderClickListener(orderId: String) {
                    trackOrderInfo(requireContext(), orderId)
                }
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        initViewModel()
        viewModel.getOrders()
        
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrdersViewModel::class.java)
        getOrders()
    }


    private fun initRecycler() {
        binding.recycler.apply {
            setHasFixedSize(true)
            adapter = listAdapter
        }


    }


    private fun getOrders() {
        viewModel.orders.observe(viewLifecycleOwner, Observer {
                it ?: return@Observer
               if( it.success != null) {
                   
                   i(TAG, it.success.toString())
                   
                        listAdapter.submitData(it.success)
                        binding.progressBar.hide()
                        binding.loading.circularLoader.isVisible = false

               } else {
                   Toast.makeText(context,"Something went wrong", Toast.LENGTH_LONG).show()
               }

                }
            )

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}