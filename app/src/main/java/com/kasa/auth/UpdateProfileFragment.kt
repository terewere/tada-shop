package com.kasa.auth

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kasa.R
import com.kasa.dagger.Injectable
import com.kasa.databinding.FragmentUpdateProfileBinding
import com.kasa.models.ApiError
import com.kasa.network.TokenManager
import com.kasa.utils.Constants.PASSWORD
import com.kasa.utils.extentions.hideKeyboard
import com.kasa.utils.extentions.setProgressDialog
import javax.inject.Inject

class UpdateProfileFragment : Fragment(), Injectable {

    private lateinit var viewModel: UpdateProfileViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tokenManager: TokenManager

    private var _binding: FragmentUpdateProfileBinding? = null

    private val binding get() = _binding!!
    private val progress  by lazy { setProgressDialog(requireContext() ) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtUserName.setText( tokenManager.user.firstName)
        binding.txtUserLastName.setText( tokenManager.user.lastName)

        viewModel = ViewModelProvider(this, viewModelFactory).get(UpdateProfileViewModel::class.java)

        viewModel.apiResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            progress.hide()

            if (loginResult.error != null) {
                handleErrors(loginResult.error)
            }

            if (loginResult.success != null) {
                nextStep()
            }

            if (loginResult.success == null && loginResult.error == null) {
                progress.hide()
            }

        })


        binding.btnNext.setOnClickListener {
            handleProfileUpdate(requireContext())

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun handleProfileUpdate(context: Context){
        if (TextUtils.isEmpty(binding.txtUserName.text) ) {

            val res = R.string.invalid_name
            binding.txtUserName.error = resources.getString(res)

            Toast.makeText(
                context,
                getString(res),
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (binding.txtUserName.text.toString().length >= 40) {

            val res = R.string.invalid_name_length
            binding.txtUserName.error = resources.getString(res)
            Toast.makeText(
                context,
                getString(res),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        progress.show()
        viewModel.updateUser(
            binding.txtUserName.text.toString(),
            binding.txtUserLastName.text.toString()
        )
    }


    private fun handleErrors(apiError: ApiError?) {

        val entries = apiError?.errors?.entries

        if (entries != null) {
            for ((key, value) in entries) {
                if (key == PASSWORD) {
                    binding.txtUserLastName.error = value[0]
                    binding.txtUserLastNameLayout.error = value[0]
                }
            }
        }
    }


    private fun nextStep() {
        //getMainActivity()?.setNavHeader()
       findNavController().popBackStack(R.id.nav_welcome, true)

    }


    private val mCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
           findNavController().navigate(R.id.nav_home)
        }
    }

    override fun onPause() {
        super.onPause()
        hideKeyboard(requireActivity())
        mCallback.remove()

    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(mCallback)

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}
