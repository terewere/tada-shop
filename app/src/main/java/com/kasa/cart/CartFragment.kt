package com.kasa.cart

import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentCartBinding
import com.kasa.utils.Constants.TAG
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragment : Fragment(), Injectable {

	private lateinit var binding: FragmentCartBinding

	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory


	private lateinit var viewModel: CartViewModel

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

						//TODO: app crashinng when value is 0
						if (viewModel.getItemsCount() <=1) findNavController().popBackStack()
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

		viewModel = ViewModelProvider(this, viewModelFactory).get(CartViewModel::class.java)

		setObservers()
		getCartItems()

		return binding.root
	}


	private fun setViews() {
		binding.cartEmptyTextView.visibility = View.GONE
		binding.cartCheckOutBtn.setOnClickListener {
			navigateToSelectAddress()
		}
			concatAdapter = ConcatAdapter(itemsAdapter, priceCardAdapter)
			binding.cartProductsRecyclerView.adapter = concatAdapter

	}

	private fun setObservers() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getItemsCountFlow().distinctUntilChanged().collectLatest {
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

				if ( it.isEmpty()) {
					binding.cartEmptyTextView.visibility = View.VISIBLE
				}  else {
					itemsAdapter.submitData(it)
				}



			}
		}
	}



	private fun navigateToSelectAddress() {
		            Toast.makeText(context, "Not implement yet... ", Toast.LENGTH_SHORT).show()

	}

}