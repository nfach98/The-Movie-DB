package com.nf98.moviecatalogue.app.detail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Credit
import com.nf98.moviecatalogue.app.detail.DetailActivity
import com.nf98.moviecatalogue.app.detail.DetailViewModel
import com.nf98.moviecatalogue.app.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_detail_credit.*

class CreditFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var index = 1

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        const val TYPE_SEASON = 0
        const val TYPE_CAST = 1
        const val TYPE_CREW = 2

        fun newInstance(index: Int): CreditFragment {
            val fragment =
                CreditFragment()
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

        viewModel = (activity as DetailActivity).viewModel

        when(index) {
            TYPE_SEASON -> {

            }
            TYPE_CAST -> {
                viewModel.getCasts((activity as DetailActivity).type, (activity as DetailActivity).id).observe(this, Observer {
                    if (it != null)
                        refreshList(it)
                })
            }
            TYPE_CREW -> {
                viewModel.getCrews((activity as DetailActivity).type, (activity as DetailActivity).id).observe(this, Observer {
                    if (it != null)
                        refreshList(it)
                })
            }
        }
    }

    private fun refreshList(it: ArrayList<Credit>) {
        val adapter = CreditAdapter()
        adapter.notifyDataSetChanged()
        rvCredit.adapter = adapter
        adapter.setData(index, it)
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if(state)
            creditProg.visibility = View.VISIBLE
        else
            creditProg.visibility = View.GONE
    }
}
