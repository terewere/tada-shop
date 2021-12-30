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
import com.kasa.databinding.FragmentRegisterBinding
import com.kasa.network.TokenManager
import com.kasa.utils.extentions.hideKeyboard
import com.kasa.utils.extentions.setProgressDialog
import javax.inject.Inject

class RegisterFragment : Fragment(), Injectable {

    private lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var tokenManager: TokenManager

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!
    private val progress  by lazy { setProgressDialog(requireContext() ) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.TEFirstName.setText( tokenManager.user.firstName)
        binding.TELastName.setText( tokenManager.user.lastName)
        binding.TEEmail.setText( tokenManager.user.lastName)
        binding.TEPhoneNumber.setText( tokenManager.user.number)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AuthViewModel::class.java)

        viewModel.apiResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer
            progress.hide()

            if (loginResult.success != null) {
                nextStep()
            }

            if (loginResult.success == null && loginResult.error == null) {
                progress.hide()
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
            }

        })

        binding.TVHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.nav_login)
        }

        binding.signupBtn.setOnClickListener {
            handleRegister(requireContext())

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun handleRegister(context: Context){
        if (TextUtils.isEmpty(binding.TEFirstName.text) ) {

            val res =  resources.getString(R.string.required, "First name")
            binding.TEFirstName.error = res
            Toast.makeText(context,res,Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(binding.TELastName.text) ) {

            val res = resources.getString(R.string.required, "Last name")
            binding.TELastName.error = res
            Toast.makeText(context,res,Toast.LENGTH_LONG).show()
            return
        }

        if (TextUtils.isEmpty(binding.TEEmail.text) ) {

            val res = resources.getString(R.string.required, "Email")
            binding.TEEmail.error = res
            Toast.makeText(context,res,Toast.LENGTH_LONG).show()

            return
        }

        if (TextUtils.isEmpty(binding.TEPhoneNumber.text) ) {

            val res = resources.getString(R.string.required, "Phone Number")
            binding.TEPhoneNumber.error = res
            Toast.makeText(context,res,Toast.LENGTH_LONG).show()
            return
        }

        progress.show()
        viewModel.register(
            binding.TEFirstName.text.toString(),
            binding.TELastName.text.toString(),
            binding.TEEmail.text.toString(),
            binding.TEPhoneNumber.text.toString(),
        )
    }




    private fun nextStep() {
       findNavController().navigate(R.id.action_nav_register_to_nav_home)

    }


    override fun onPause() {
        super.onPause()
        hideKeyboard(requireActivity())

    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}
