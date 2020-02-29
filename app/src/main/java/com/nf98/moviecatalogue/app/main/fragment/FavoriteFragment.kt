package com.nf98.moviecatalogue.app.main.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.app.main.MainPagerAdapter
import kotlinx.android.synthetic.main.fragment_main_favorite.*

class FavoriteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = MainPagerAdapter(
            activity,
            childFragmentManager,
            MainPagerAdapter.TYPE_FAVORITE
        )
        pagerFavorite.adapter = pagerAdapter
        tabFavorite.setupWithViewPager(pagerFavorite)
    }
}
