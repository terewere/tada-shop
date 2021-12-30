package com.kasa.feed


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.kasa.R
import com.kasa.products.*

class FeedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_feed, container, false)
    }


}