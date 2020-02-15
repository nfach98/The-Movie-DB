package com.nf98.moviecatalogue.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.ui.ListFragment
import com.nf98.moviecatalogue.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail_credit.*

class CreditFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var index = 1

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        const val TYPE_SEASON = 0
        const val TYPE_CAST = 1
        const val TYPE_CREW = 2

        fun newInstance(index: Int): CreditFragment {
            val fragment = CreditFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_credit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)
        rvCredit.layoutManager = LinearLayoutManager(activity)

        if (arguments != null)
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        when(index) {
            TYPE_SEASON -> {

            }
            TYPE_CAST -> {

            }
            TYPE_CREW -> {

            }
        }
    }

    private fun showLoading(state: Boolean) {
        if(state)
            creditProg.visibility = View.VISIBLE
        else
            creditProg.visibility = View.GONE
    }
}
