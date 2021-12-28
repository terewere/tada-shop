package com.kasa.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import androidx.navigation.fragment.findNavController
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentRegistrationCountryPickerBinding
import java.util.*
import javax.inject.Inject

class CountryPickerFragment : ListFragment(), Injectable,
    LoaderManager.LoaderCallbacks<ArrayList<Map<String, String?>>> {

    private lateinit var viewModel: VerifyPhoneViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentRegistrationCountryPickerBinding? = null

    private val binding get() = _binding!!

    private var resultKey: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationCountryPickerBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val arguments = arguments
//            resultKey = arguments.getResultKey()
        }
        if (resultKey == null) {
            viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(VerifyPhoneViewModel::class.java)
        }
        binding.countrySearch.addTextChangedListener(FilterWatcher())
        LoaderManager.getInstance(this).initLoader(0, null, this)
            .forceLoad()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onListItemClick(listView: ListView, view: View, position: Int, id: Long) {
        val item = listAdapter!!.getItem(position) as Map<String, String>
        val countryCode = item["country_code"]!!.replace("+", "").toInt()
        val countryName = item["country_name"]
        if (resultKey == null) {
            viewModel.onCountrySelected(countryName, countryCode)
        } else {
            val result = Bundle()
            result.putString(KEY_COUNTRY, countryName)
            result.putInt(KEY_COUNTRY_CODE, countryCode)
            parentFragmentManager.setFragmentResult(resultKey!!, result)
        }
        findNavController().navigateUp()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Map<String, String?>>> {
        return CountryListLoader(activity)
    }

    override fun onLoadFinished(
        loader: Loader<ArrayList<Map<String, String?>>>,
        results: ArrayList<Map<String, String?>>
    ) {
        (listView.emptyView as TextView).setText(
            R.string.country_selection_fragment__no_matching_countries
        )
        val from = arrayOf("country_name", "country_code")
        val to = intArrayOf(R.id.country_name, R.id.country_code)
        listAdapter =
            SimpleAdapter(activity, results, R.layout.country_list_item, from, to)
        applyFilter(binding.countrySearch.text)
    }

    private fun applyFilter(text: CharSequence) {
        val listAdapter = listAdapter as SimpleAdapter?
        listAdapter?.filter?.filter(text)
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Map<String, String?>>>) {
        listAdapter = null
    }

    private inner class FilterWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            applyFilter(s)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    companion object {
        const val KEY_COUNTRY = "country"
        const val KEY_COUNTRY_CODE = "country_code"
    }
}