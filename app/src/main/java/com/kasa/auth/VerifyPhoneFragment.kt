package com.kasa.auth

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.VerifyPhoneFragmentBinding
import com.kasa.models.ApiError
import com.kasa.utils.Constants.PHONE_NUMBER
import com.kasa.utils.Constants.SUCCESS
import com.kasa.utils.Constants.TAG
import com.kasa.utils.Constants.TOKEN
import com.kasa.utils.PhoneNumberFormatter
import com.kasa.utils.extentions.hideKeyboard
import com.kasa.utils.extentions.setProgressDialog
import javax.inject.Inject


class VerifyPhoneFragment : Fragment(), Injectable, RegistrationNumberInputController.Callbacks {


    private var mPhone: String? = null
    private lateinit var viewModel: VerifyPhoneViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var _binding: VerifyPhoneFragmentBinding? = null

    private val binding get() = _binding!!
    private val progress  by lazy { setProgressDialog(requireContext() ) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VerifyPhoneFragmentBinding.inflate(inflater, container, false)
          return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(VerifyPhoneViewModel::class.java)

        val controller = RegistrationNumberInputController(
            requireContext(),
            binding.countryCode,
            binding.number,
            binding.countrySpinner,
            true,
            this
        )

        viewModel.getLiveNumber().observe(viewLifecycleOwner, {
            it?:return@observe
            controller.updateNumber(it)
        })

        viewModel.apiResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            progress.hide()

            if (loginResult.error != null) {
                handleErrors(loginResult.error)
            }

            if (loginResult.success != null) {
                nextStep(loginResult.success)
            }

            if (loginResult.success == null && loginResult.error == null) {
                progress.hide()
            }

        })

        binding.btnNext.setOnClickListener {
            handleRegister(requireContext())
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    private fun handleErrors(apiError: ApiError?) {

        val entries = apiError?.errors?.entries

        if (entries != null) {
            for ((key, value) in entries) {
                if (key ==  PHONE_NUMBER) {
                    binding.txtvNumberLayout.error = value[0]
                }
            }
        } else if(apiError?.message != null) {
            Toast.makeText(requireContext(), apiError.message, Toast.LENGTH_LONG ).show()
            apiError.message = null
        }
    }


    private fun nextStep(token: String) {
                //TODO remove token after testing
        findNavController()
                    .navigate(R.id.action_nav_verify_phone_to_nav_verify_token, bundleOf(PHONE_NUMBER to mPhone, TOKEN to token ))
            }



    override fun onPause() {
        super.onPause()
        hideKeyboard(requireActivity())
    }

    private fun handleRegister(context: Context) {

        if (TextUtils.isEmpty(binding.countryCode.text)) {

            Toast.makeText(
                context,
                getString(R.string.RegistrationActivity_you_must_specify_your_country_code),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (TextUtils.isEmpty(binding.number.text)) {

            val res = R.string.RegistrationActivity_you_must_specify_your_phone_number
            binding.number.error = resources.getString(res)
            Toast.makeText(
                context,
                getString(res),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        val number = viewModel.getNumber()
        val e164number = number.e164Number
        if (!number.isValid) {

            val res = R.string.RegistrationActivity_the_number_you_specified_s_is_invalid

            binding.number.error = resources.getString(res)
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.RegistrationActivity_invalid_number))
                .setMessage(String.format(
                    getString(res),
                    e164number
                )).show()

            return
        }


        confirmNumberPrompt(context, e164number) {
            progress.show()
            mPhone = e164number
            viewModel.verifyPhone(e164number)
        }
    }
    override fun onNumberFocused() {

    }

    override fun onNumberInputNext(view: View) {

    }

    override fun onNumberInputDone(view: View) {
        hideKeyboard(requireActivity())
        handleRegister(requireContext())
    }

    override fun onPickCountry(view: View) {
        findNavController().navigate(R.id.nav_pickCountry)
    }

    override fun setNationalNumber(number: String) {
        viewModel.setNationalNumber(number)
    }

    override fun setCountry(countryCode: Int) {
        viewModel.onCountrySelected(null, countryCode)
    }


       protected fun confirmNumberPrompt(
        context: Context,
        e164number: String,
        onConfirmed: () -> Unit
    ) {

           val message: CharSequence = SpannableStringBuilder()
               .append(context.getString(R.string.RegistrationActivity_a_verification_code_will_be_sent_to))
               .append("\n\n")
               .append(PhoneNumberFormatter.getInternationalFormatFromE164(e164number))
               .append("\n\n")
               .append(context.getString(R.string.RegistrationActivity_is_your_phone_number_above_correct))

           MaterialAlertDialogBuilder(context)
               .setMessage(message )
               .setPositiveButton(android.R.string.ok) { _, _ -> onConfirmed() }
               .setNegativeButton(R.string.RegistrationActivity_edit_number) { _, _ ->  }
               .show()
    }
}
