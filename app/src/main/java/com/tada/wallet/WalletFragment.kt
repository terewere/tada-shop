package com.tada.wallet


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.tada.R

class WalletFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }


}