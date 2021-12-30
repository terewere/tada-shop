package com.kasa.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kasa.R
import com.kasa.databinding.FragmentRegistrationWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentRegistrationWelcomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationWelcomeBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.welcomeContinueButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcome_to_register)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

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
