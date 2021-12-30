package com.kasa

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AppCompatActivity
import com.kasa.dagger.Injectable
import com.kasa.network.TokenManager
import javax.inject.Inject


class SplashFragment : Fragment(), Injectable {


    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            context?.let {

                if (tokenManager.accessToken == null) {
                    findNavController().navigate(R.id.nav_welcome)
                }else{
                    findNavController().navigate(R.id.action_nav_splash_to_nav_home)
                }
            }
        }, 200)
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