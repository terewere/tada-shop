package com.kasa.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.VerifyTokenFragmentBinding
import com.kasa.models.ApiError
import com.kasa.network.TokenManager
import com.kasa.utils.Constants.PHONE_NUMBER
import com.kasa.utils.Constants.TOKEN
import com.kasa.utils.extentions.hideKeyboard
import javax.inject.Inject

class VerifyTokenFragment : Fragment(), Injectable {


    private var broadcastReceiver: BroadcastReceiver? = null

    private var mPhone: String? = null
    private var mToken: String? = null
    private lateinit var viewModel: VerifyTokenViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: VerifyTokenFragmentBinding? = null

    private val binding get() = _binding!!


    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mPhone = it.getString(PHONE_NUMBER)
            mToken = it.getString(TOKEN)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VerifyTokenFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this, viewModelFactory).get(VerifyTokenViewModel::class.java)


        binding.txtvNumber.text = mPhone

        mToken?.let {
            viewModel.verifyToken(mPhone!!, it)

        }


        binding.txtPinEntry.setOnPinEnteredListener {
                str ->
            mPhone?.let { viewModel.verifyToken(it, str.toString()) }
        }

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val otpToken = intent.getStringExtra("otp")
                binding.txtPinEntry.setText(otpToken)
            }
        }

        viewModel.apiResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            hideLoading()

            if (loginResult.error != null) {
                handleErrors(loginResult.error)
            }

            if (loginResult.success != null) {

                nextStep()
            }

            if (loginResult.success == null && loginResult.error == null) {
                binding.txtPinEntry.error = "Incorrect token"
                binding.txtPinEntry.setText("")
                hideLoading()
            }

        })


        binding.requestNewCode.setOnClickListener {
            showLoading()
            Toast.makeText(requireContext(), "requesting new token", Toast.LENGTH_LONG).show()

            binding.txtPinEntry.setText("")

        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



    private fun handleErrors(apiError: ApiError?) {

        val entries = apiError?.errors?.entries

        if (entries != null) {
            for ((key, value) in entries) {
                if (key ==  TOKEN) {
                    binding.txtPinEntry.error = value[0]
                }
            }
        }
    }


    private fun nextStep() {


        findNavController().navigate(R.id.nav_register)
        binding.txtPinEntry.setText("")

    }

    fun showLoading(){

        Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_LONG).show()
    }

    fun hideLoading(){

    }



    override fun onPause() {
        super.onPause()
        hideKeyboard(requireActivity())

        requireContext().unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
        requireContext().registerReceiver(broadcastReceiver, IntentFilter("OTP_FILTER"))
    }

}
