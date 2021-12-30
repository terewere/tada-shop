package com.kasa.products

import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kasa.R
import com.kasa.databinding.FragmentCartBinding
import com.kasa.models.CartItem
import com.kasa.models.CartItemWithProduct
import com.kasa.utils.Constants.TAG
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class CartFragment : Fragment() {

	private lateinit var binding: FragmentCartBinding

	private val viewModel: ProductViewModel by navGraphViewModels(R.id.mobile_navigation)

	private lateinit var concatAdapter: ConcatAdapter

	private val itemsAdapter by lazy {
		CartItemAdapter(
			object : CartItemAdapter.OnClickListener {

				override fun onPlusClick(itemId: Long) {
					Log.d(TAG, "onPlus: Increasing quantity")
					viewLifecycleOwner.lifecycleScope.launch {
						viewModel.incrementItemInCart(itemId)
					}
				}
				override fun onMinusClick(itemId: Long, currQuantity: Long) {
					viewLifecycleOwner.lifecycleScope.launch {

						viewModel.decrementItemInCart(itemId)
					}
					Log.d(TAG, "onMinus: decreasing quantity")
				}
			}
		)
	}

    private  val priceCardAdapter by lazy {
        PriceCardAdapter()
    }

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FragmentCartBinding.inflate(layoutInflater)

		setViews()
		setObservers()
		getCartItems()

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//viewModel.getCartItems()
	}

	private fun setViews() {
		//binding.loaderLayout.circularLoader.showAnimationBehavior
		//binding.cartAppBar.topAppBar.title = getString(R.string.cart_fragment_label)
		binding.cartEmptyTextView.visibility = View.GONE
		binding.cartCheckOutBtn.setOnClickListener {
			//navigateToSelectAddress()
		}
			//setItemsAdapter(viewModel.cartItems.value)
			concatAdapter = ConcatAdapter(itemsAdapter, priceCardAdapter)
			binding.cartProductsRecyclerView.adapter = concatAdapter

	}

	private fun setObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getItemsCountFlow().distinctUntilChanged().collectLatest {
               // i("testing", it.toString())
                priceCardAdapter.setItemsCountFlow(it)
            }

        }


		viewLifecycleOwner.lifecycleScope.launch {
			viewModel.getTotalAmount().distinctUntilChanged().collectLatest {
				priceCardAdapter.setTotalAmount(it)
			}
		}


	}


	fun getCartItems(){
		viewLifecycleOwner.lifecycleScope.launch {
			viewModel.getCartItems().distinctUntilChanged().collectLatest {
				 i("testing", it.toString())

				if (it == null || it.isEmpty()) {
					binding.cartEmptyTextView.visibility = View.VISIBLE
				}  else {
					i("testing", "DDONNT CALLLLLLL ME")

					itemsAdapter.submitData(it)
				}



			}
		}
	}



//	private fun navigateToSelectAddress() {
//		findNavController().navigate(R.id.action_cartFragment_to_selectAddressFragment)
//	}

}