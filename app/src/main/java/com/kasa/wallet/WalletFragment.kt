package com.kasa.wallet


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.kasa.R
import com.kasa.products.*

class WalletFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }


}