package com.kasa.auth

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentRegistrationWelcomeBinding
import com.kasa.utils.Constants.TAG
import com.kasa.utils.Util
import com.kasa.utils.permissions.Permissions
import javax.inject.Inject

class WelcomeFragment : Fragment(), Injectable {

    private val PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_CONTACTS
    )

    @RequiresApi(26)
    private val PERMISSIONS_API_26 = arrayOf(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.READ_CONTACTS,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    @StringRes
    private val RATIONALE: Int =
        R.string.RegistrationActivity_needs_access_to_your_contacts_in_order_to_connect_with_friends

    private val HEADERS =
        intArrayOf(R.drawable.ic_contacts)

    private lateinit var viewModel: VerifyPhoneViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: FragmentRegistrationWelcomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(VerifyPhoneViewModel::class.java)

        binding.welcomeContinueButton.setOnClickListener {
            continueClicked()
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


    private fun continueClicked() {

        Permissions.with(this)
            .request(*getContinuePermissions())
            .ifNecessary(true)
            .withRationaleDialog(
                getString(getContinueRationale()),
                *getContinueHeaders()
            )
            .withPermanentDenialDialog(getString(RATIONALE))
            .onSomeGranted {gatherInformationAndContinue()}
            .execute()

    }

    private fun gatherInformationAndContinue() {

        initializeNumber()
        findNavController().navigate(R.id.action_nav_welcome_to_nav_verify_phone)

    }

    @SuppressLint("MissingPermission")
    private fun initializeNumber() {

            Log.i(TAG, "No number detected")
            val simCountryIso: String? = Util.getSimCountryIso(requireContext())
            if (simCountryIso != null && !TextUtils.isEmpty(simCountryIso)) {
                viewModel.onNumberDetected(
                    PhoneNumberUtil.getInstance().getCountryCodeForRegion(simCountryIso), ""
                )
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        Permissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @SuppressLint("NewApi")
    private fun getContinuePermissions(): Array<String> {
        return  if (Build.VERSION.SDK_INT >= 26) {
            PERMISSIONS_API_26
        } else {
            PERMISSIONS
        }
    }

    @StringRes
    private fun getContinueRationale(): Int {
        return RATIONALE
    }

    private fun getContinueHeaders(): IntArray {
        return  HEADERS
    }


    private val mCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
                (activity as AppCompatActivity?)?.finish()
        }
    }


    override fun onPause() {
        super.onPause()
        mCallback.remove()
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        requireActivity().onBackPressedDispatcher.addCallback(mCallback)

    }


}
