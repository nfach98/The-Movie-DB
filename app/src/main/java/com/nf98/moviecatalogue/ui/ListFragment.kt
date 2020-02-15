package com.nf98.moviecatalogue.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.adapter.ListAdapter
import com.nf98.moviecatalogue.adapter.MainPagerAdapter
import com.nf98.moviecatalogue.model.Movie
import com.nf98.moviecatalogue.model.TVShow
import com.nf98.moviecatalogue.ui.main.DiscoverFragmentDirections
import com.nf98.moviecatalogue.ui.main.MovieFragmentDirections
import com.nf98.moviecatalogue.ui.main.TVShowFragmentDirections
import com.nf98.moviecatalogue.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private var index = 0

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): ListFragment {
            val fragment = ListFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        showLoading(true)
        rvList.layoutManager = LinearLayoutManager(activity)
        setSpinner()

        when (index) {
            in 0..3 -> {
                tableDisc.visibility = View.GONE
                mainViewModel.getMovieList(index).observe(this, Observer {
                    if (it != null)
                        refreshList(it)
                })
            }
            in 4..7 -> {
                tableDisc.visibility = View.GONE
                mainViewModel.getTVList(index).observe(this, Observer {
                    if (it != null)
                        refreshList(it)
                })
            }
            8 -> updateDiscover(MainPagerAdapter.TYPE_MOVIE)
            9 -> updateDiscover(MainPagerAdapter.TYPE_TV)
        }
    }

    private fun refreshList(it: ArrayList<*>) {
        val adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        rvList.adapter = adapter
        adapter.setData(it)
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Any) {
                when(index) {
                    in 0..3 -> toDetail(MainPagerAdapter.TYPE_MOVIE, data)
                    in 4..7 -> toDetail(MainPagerAdapter.TYPE_TV, data)
                    in 8..9 -> toDetail(MainPagerAdapter.TYPE_DISCOVER, data)
                }
            }
        })
        showLoading(false)
    }

    private fun updateDiscover(type: Int) {
        tableDisc.visibility = View.VISIBLE
        when(type) {
            MainPagerAdapter.TYPE_MOVIE -> {
                mainViewModel.getMovieList(index, spinnerYear.selectedItem.toString().toInt(), spinnerSort.selectedItemPosition)
                    .observe(this, Observer {
                        if (it != null) { refreshList(it) }
                    })
            }
            MainPagerAdapter.TYPE_TV -> {
                mainViewModel.getTVList(index, spinnerYear.selectedItem.toString().toInt(), spinnerSort.selectedItemPosition)
                    .observe(this, Observer {
                        if (it != null) { refreshList(it) }
                    })
            }
        }
    }

    private fun toDetail(type: Int, data: Any) {
        when(type) {
            MainPagerAdapter.TYPE_MOVIE -> {
                val direction = MovieFragmentDirections.actionNavMovieToDetailActivity()
                direction.id = (data as Movie).id
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_TV -> {
                val direction = TVShowFragmentDirections.actionNavTvShowToDetailActivity()
                direction.id = (data as TVShow).id
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_DISCOVER -> {
                val direction = DiscoverFragmentDirections.actionNavDiscoverToDetailActivity()
                direction.id = if(index == 8) (data as Movie).id else (data as TVShow).id
                view?.findNavController()?.navigate(direction)
            }
        }
    }

    private fun showLoading(state: Boolean) { listProg.visibility = if(state) View.VISIBLE else View.GONE }

    private fun setSpinner() {
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(index == 8) updateDiscover(MainPagerAdapter.TYPE_MOVIE)
                else updateDiscover(MainPagerAdapter.TYPE_TV)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //Year Spinner
        val adapterYear = activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, mainViewModel.getYearList()) }
        adapterYear?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerYear.adapter = adapterYear
        spinnerYear.onItemSelectedListener = spinnerListener

        //Sort Spinner
        val arraySort = if(index == 8) resources.getStringArray(R.array.sort_by_movie) else resources.getStringArray(R.array.sort_by_tv)
        val adapterSort = activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, arraySort) }
        adapterYear?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSort.adapter = adapterSort
        spinnerSort.onItemSelectedListener = spinnerListener
    }
}
