package com.nf98.moviecatalogue.app.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.app.detail.DetailPagerAdapter
import com.nf98.moviecatalogue.app.main.MainPagerAdapter
import com.nf98.moviecatalogue.app.main.MainViewModel
import com.nf98.moviecatalogue.db.MovieHelper
import com.nf98.moviecatalogue.db.TVShowHelper
import com.nf98.moviecatalogue.helper.MappingHelper
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private var movieHelper: MovieHelper? = null
    private var tvShowHelper: TVShowHelper? = null

    private lateinit var mainViewModel: MainViewModel
    private var index = 0

    private lateinit var favAdapter: FavoriteAdapter

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
            10 -> updateFavorite(MainPagerAdapter.TYPE_MOVIE)
            11 -> updateFavorite(MainPagerAdapter.TYPE_TV)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putParcelableArrayList("extra_state", favAdapter.movieList)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper?.close()
        tvShowHelper?.close()
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

    private fun updateFavorite(type: Int){
        tableDisc.visibility = View.GONE
        when(type){
            MainPagerAdapter.TYPE_MOVIE -> {
                movieHelper = activity?.applicationContext?.let { MovieHelper.getInstance(it) }
                movieHelper?.open()
                val adapter = FavoriteAdapter(type)
                rvList.adapter = adapter

                GlobalScope.launch(Dispatchers.Main) {
                    listProg.visibility = View.VISIBLE
                    val deferredMovies = async(Dispatchers.IO) {
                        val cursor = movieHelper?.queryAll()
                        MappingHelper.mapMovieCursorToArrayList(cursor)
                    }
                    listProg.visibility = View.INVISIBLE
                    val movies = deferredMovies.await()
                    if (movies.size > 0) adapter.movieList = movies
                    else adapter.movieList = ArrayList()
                }

                adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) {
                        toDetail(MainPagerAdapter.TYPE_FAVORITE, data)
                    }
                })

                adapter.setOnDeleteClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) {
                        movieHelper?.deleteById((data as Movie).id)
                        adapter.removeItem(data, 0)
                    }
                })
            }
            MainPagerAdapter.TYPE_TV -> {
                tvShowHelper = activity?.applicationContext?.let { TVShowHelper.getInstance(it) }
                tvShowHelper?.open()
                val adapter = FavoriteAdapter(type)
                rvList.adapter = adapter

                GlobalScope.launch(Dispatchers.Main) {
                    listProg.visibility = View.VISIBLE
                    val deferredTVShows = async(Dispatchers.IO) {
                        val cursor = tvShowHelper?.queryAll()
                        MappingHelper.mapTVShowCursorToArrayList(cursor)
                    }
                    listProg.visibility = View.INVISIBLE
                    val tvShows = deferredTVShows.await()
                    if (tvShows.size > 0) adapter.tvList = tvShows
                    else adapter.tvList = ArrayList()
                }

                adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) {
                        toDetail(MainPagerAdapter.TYPE_FAVORITE, data)
                    }
                })

                adapter.setOnDeleteClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) {
                        tvShowHelper?.deleteById((data as TVShow).id)
                        adapter.removeItem(data, 1)
                    }
                })
            }
        }
    }

    private fun toDetail(type: Int, data: Any) {
        when(type) {
            MainPagerAdapter.TYPE_MOVIE -> {
                val direction = MovieFragmentDirections.actionNavMovieToDetailActivity(null, null)
                direction.id = (data as Movie).id
                direction.type = DetailPagerAdapter.TYPE_MOVIE
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_TV -> {
                val direction = TVShowFragmentDirections.actionNavTvShowToDetailActivity(null, null)
                direction.id = (data as TVShow).id
                direction.type = DetailPagerAdapter.TYPE_TV
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_DISCOVER -> {
                val direction = DiscoverFragmentDirections.actionNavDiscoverToDetailActivity(null, null)
                direction.id = if(index == 8) (data as Movie).id else (data as TVShow).id
                direction.type = if(index == 8) DetailPagerAdapter.TYPE_MOVIE else DetailPagerAdapter.TYPE_TV
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_FAVORITE -> {
                var direction : FavoriteFragmentDirections.ActionNavFavoriteToDetailActivity? = null
                when(index){
                    10 -> {
                        direction = FavoriteFragmentDirections.actionNavFavoriteToDetailActivity(data as Movie, null)
                        direction.id = data.id
                        direction.type = DetailPagerAdapter.TYPE_MOVIE
                    }
                    11 -> {
                        direction = FavoriteFragmentDirections.actionNavFavoriteToDetailActivity(null, data as TVShow)
                        direction.id = data.id
                        direction.type = DetailPagerAdapter.TYPE_TV
                    }
                }
                direction?.let { view?.findNavController()?.navigate(it) }
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
