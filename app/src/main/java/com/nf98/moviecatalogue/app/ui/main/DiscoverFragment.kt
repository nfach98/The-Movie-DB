package com.nf98.moviecatalogue.app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.app.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.fragment_main_discover.*

class DiscoverFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = MainPagerAdapter(activity, childFragmentManager, MainPagerAdapter.TYPE_DISCOVER)
        pagerDiscover.adapter = pagerAdapter
        tabDiscover.setupWithViewPager(pagerDiscover)
    }

}
