package com.nf98.moviecatalogue.app.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.app.ui.detail.CreditFragment
import com.nf98.moviecatalogue.app.ui.detail.FactFragment
import com.nf98.moviecatalogue.app.ui.detail.SummaryFragment
import viewpagerwc.ui.dom.wrapping.WrappingFragmentPagerAdapter


class DetailPagerAdapter(private val context: Context, fm: FragmentManager, private val type: Int)
    : WrappingFragmentPagerAdapter(fm) {

    companion object{
        const val TYPE_MOVIE = 0
        const val TYPE_TV = 1
    }

    @StringRes
    private var TITLES = intArrayOf()

    init {
        when(type) {
            TYPE_MOVIE -> TITLES = intArrayOf(R.string.summary, R.string.cast, R.string.crew, R.string.fact)
            TYPE_TV -> TITLES = intArrayOf(R.string.summary, R.string.season, R.string.cast, R.string.crew, R.string.fact)
        }
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if(type == TYPE_MOVIE) {
            when (position) {
                0 -> fragment = SummaryFragment()
                in 1..2 -> fragment = CreditFragment.newInstance(position)
                3 -> fragment = FactFragment()
            }
        }
        if(type == TYPE_TV) {
            when (position) {
                0 -> fragment = SummaryFragment()
                in 1..3 -> fragment = CreditFragment.newInstance(position - 1)
                4 -> fragment = FactFragment()
            }
        }

        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TITLES[position])
    }

    override fun getCount(): Int {
        return TITLES.size
    }

}