package com.nf98.moviecatalogue.app.main.fragment

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.app.ViewModelFactory
import com.nf98.moviecatalogue.app.detail.DetailPagerAdapter
import com.nf98.moviecatalogue.app.main.MainPagerAdapter
import com.nf98.moviecatalogue.app.main.MainViewModel
import com.nf98.moviecatalogue.helper.ImageManager
import com.nf98.moviecatalogue.helper.Inject
import com.nf98.moviecatalogue.provider.MovieProvider
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.layout_main_bottom_sheet.view.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ListFragment : Fragment() {

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel
    private var index = 0

    private var dbList = ArrayList<Any>()
    private var db: Cursor? = null

    private val loaderCallback = object : LoaderManager.LoaderCallbacks<Cursor>{
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return when(id){
                MovieProvider.MOVIE -> activity?.applicationContext?.let {
                    CursorLoader(it, MovieProvider.URI_MOVIE, arrayOf(Movie.VOTE_COUNT, Movie.TITLE, Movie.STATUS, Movie.SCORE, Movie.REVENUE,
                        Movie.RELEASE_DATE, Movie.OVERVIEW, Movie.ORI_TITLE, Movie.ORI_LANGUAGE, Movie.GENRES,
                        Movie.DURATION, Movie.BUDGET, Movie.POPULARITY, Movie.ID),
                        null, null, null)
                }!!
                MovieProvider.TV -> activity?.applicationContext?.let {
                    CursorLoader(it, MovieProvider.URI_TV, arrayOf(TVShow.FIRST_AIR_DATE, TVShow.GENRES, TVShow.ID, TVShow.NAME, TVShow.NUM_SEASONS,
                        TVShow.ORI_LANGUAGE,TVShow.ORI_NAME, TVShow.OVERVIEW, TVShow.POPULARITY, TVShow.RUNTIMES,
                        TVShow.SCORE, TVShow.STATUS, TVShow.VOTE_COUNT),
                        null, null, null)
                }!!
                else -> throw IllegalArgumentException("Invalid type")
            }
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            db = data
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            db = null
        }

    }

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

        viewModelFactory = activity?.let { Inject.provideViewModelFactory(it) }!!
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        showLoading(true)
        rvList.layoutManager = LinearLayoutManager(activity)
        setSpinner()
    }


    override fun onStart() {
        super.onStart()
        when (index) {
            in 0..3 -> {
                updateMovie(index)
                getDb(MainPagerAdapter.TYPE_MOVIE)
            }
            in 4..7 -> {
                updateTV(index)
                getDb(MainPagerAdapter.TYPE_TV)
            }
            8 -> {
                updateDiscover(MainPagerAdapter.TYPE_MOVIE)
                getDb(MainPagerAdapter.TYPE_MOVIE)
            }
            9 -> {
                updateDiscover(MainPagerAdapter.TYPE_TV)
                getDb(MainPagerAdapter.TYPE_TV)
            }
            10 -> {
                updateFavorite(MainPagerAdapter.TYPE_MOVIE)
                getDb(MainPagerAdapter.TYPE_MOVIE)
            }
            11 -> {
                updateFavorite(MainPagerAdapter.TYPE_TV)
                getDb(MainPagerAdapter.TYPE_TV)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Inject.closeDatabase()
    }

    private fun getDb(type: Int){
        when(type){
            MainPagerAdapter.TYPE_MOVIE -> {
                viewModel.getMovieList().observe(this, Observer {
                    if(it != null) { dbList.addAll(it) }
                })
            }
            MainPagerAdapter.TYPE_TV -> {
                viewModel.getTVList().observe(this, Observer {
                    if(it != null) { dbList.addAll(it) }
                })
            }
        }
    }

    private fun isFavorite(type: Int, data: Any): Boolean{
        var fav = false

        loop@ for(item in dbList){
            when(type){
                MainPagerAdapter.TYPE_MOVIE -> {
                    if((item as Movie).id == (data as Movie).id) {
                        fav = true
                        break@loop
                    }
                }
                MainPagerAdapter.TYPE_TV -> {
                    if((item as TVShow).id == (data as TVShow).id) {
                        fav = true
                        break@loop
                    }
                }
            }
        }

        return fav
    }

    private fun updateMovie(index: Int){
        tableDisc.visibility = View.GONE
        viewModel.getMovieList(index).observe(this, Observer {
            if (it != null) refreshList(it)
        })
    }

    private fun updateTV(index: Int){
        tableDisc.visibility = View.GONE
        viewModel.getTVList(index).observe(this, Observer {
            if (it != null) refreshList(it)
        })
    }

    private fun refreshList(it: ArrayList<*>) {
        showLoading(true)
        val adapter = ListAdapter()
        adapter.notifyDataSetChanged()
        rvList.adapter = adapter
        adapter.setData(it)
        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback{
            override fun onItemClicked(data: Any) {
                when(index) {
                    in 0..3 -> toDetail(MainPagerAdapter.TYPE_MOVIE, data, isFavorite(MainPagerAdapter.TYPE_MOVIE, data))
                    in 4..7 -> toDetail(MainPagerAdapter.TYPE_TV, data, isFavorite(MainPagerAdapter.TYPE_TV, data))
                    8 -> toDetail(MainPagerAdapter.TYPE_DISCOVER, data, isFavorite(MainPagerAdapter.TYPE_MOVIE, data))
                    9 -> toDetail(MainPagerAdapter.TYPE_DISCOVER, data, isFavorite(MainPagerAdapter.TYPE_TV, data))
                }
            }
        })

        adapter.setShowSheetCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Any) {
                when(index) {
                    in 0..3, 8 -> showBottomSheet(MainPagerAdapter.TYPE_MOVIE, data)
                    in 4..7, 9 -> showBottomSheet(MainPagerAdapter.TYPE_TV, data)
                }
            }
        })
        showLoading(false)
    }

    private fun updateDiscover(type: Int) {
        tableDisc.visibility = View.VISIBLE
        when(type) {
            MainPagerAdapter.TYPE_MOVIE -> {
                viewModel.getMovieList(index, spinnerYear.selectedItem.toString().toInt(), spinnerSort.selectedItemPosition)
                    .observe(this, Observer {
                        if (it != null) { refreshList(it) }
                    })
            }
            MainPagerAdapter.TYPE_TV -> {
                viewModel.getTVList(index, spinnerYear.selectedItem.toString().toInt(), spinnerSort.selectedItemPosition)
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
                LoaderManager.getInstance(this).initLoader(MovieProvider.MOVIE, null, loaderCallback)
                val adapter = activity?.applicationContext?.let { FavoriteAdapter(it) }
                adapter?.notifyDataSetChanged()
                rvList.adapter = adapter
                adapter?.setData(dbList)

                adapter?.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) {
                        toDetail(MainPagerAdapter.TYPE_FAVORITE, data, isFavorite(MainPagerAdapter.TYPE_MOVIE, data))
                    }
                })

                adapter?.setShowSheetCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) { showBottomSheet(MainPagerAdapter.TYPE_MOVIE, data) }
                })
            }
            MainPagerAdapter.TYPE_TV -> {
                LoaderManager.getInstance(this).initLoader(MovieProvider.TV, null, loaderCallback)
                val adapter = activity?.applicationContext?.let { FavoriteAdapter(it) }
                adapter?.notifyDataSetChanged()
                rvList.adapter = adapter
                adapter?.setData(dbList)

                adapter?.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) {
                        toDetail(MainPagerAdapter.TYPE_FAVORITE, data, isFavorite(MainPagerAdapter.TYPE_TV, data))
                    }
                })

                adapter?.setShowSheetCallback(object : FavoriteAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Any) { showBottomSheet(MainPagerAdapter.TYPE_MOVIE, data) }
                })
            }
        }
        showLoading(false)
    }

    private fun toDetail(type: Int, data: Any, favorite: Boolean = false) {
        when(type) {
            MainPagerAdapter.TYPE_MOVIE -> {
                val direction = MovieFragmentDirections.actionNavMovieToDetailActivity(null, null)
                direction.id = (data as Movie).id
                direction.type = DetailPagerAdapter.TYPE_MOVIE
                direction.favorite = favorite
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_TV -> {
                val direction = TVShowFragmentDirections.actionNavTvShowToDetailActivity(null, null)
                direction.id = (data as TVShow).id
                direction.type = DetailPagerAdapter.TYPE_TV
                direction.favorite = favorite
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_DISCOVER -> {
                val direction = DiscoverFragmentDirections.actionNavDiscoverToDetailActivity(null, null)
                direction.id = if(index == 8) (data as Movie).id else (data as TVShow).id
                direction.type = if(index == 8) DetailPagerAdapter.TYPE_MOVIE else DetailPagerAdapter.TYPE_TV
                direction.favorite = favorite
                view?.findNavController()?.navigate(direction)
            }
            MainPagerAdapter.TYPE_FAVORITE -> {
                var direction : FavoriteFragmentDirections.ActionNavFavoriteToDetailActivity? = null
                when(index){
                    10 -> {
                        direction = FavoriteFragmentDirections.actionNavFavoriteToDetailActivity(data as Movie, null)
                        direction.id = data.id
                        direction.type = DetailPagerAdapter.TYPE_MOVIE
                        direction.favorite = favorite
                    }
                    11 -> {
                        direction = FavoriteFragmentDirections.actionNavFavoriteToDetailActivity(null, data as TVShow)
                        direction.id = data.id
                        direction.type = DetailPagerAdapter.TYPE_TV
                        direction.favorite = favorite
                    }
                }
                direction?.let { view?.findNavController()?.navigate(it) }
            }
        }
    }

    private fun showLoading(state: Boolean) { listProg.visibility = if(state) View.VISIBLE else View.GONE }

    private fun showBottomSheet(type: Int, data: Any){
        val view = layoutInflater.inflate(R.layout.layout_main_bottom_sheet, null)
        val dialog = activity?.let { it -> BottomSheetDialog(it) }
        val options = RequestOptions()
            .placeholder(R.drawable.img_poster_na)
            .error(R.drawable.img_poster_na)
        val wrapper = ContextWrapper(context)

        dialog?.setContentView(view)
        dialog?.window?.decorView?.findViewById<View>(R.id.design_bottom_sheet)?.setBackgroundResource(android.R.color.transparent)
        when (type) {
            MainPagerAdapter.TYPE_MOVIE -> {
                if(index == 10){
                    var poster = wrapper.getDir("movie", Context.MODE_PRIVATE)
                    poster = File(poster, "poster_${(data as Movie).id}.jpg")

                    Glide.with(this)
                        .load(Uri.fromFile(poster))
                        .apply(options)
                        .into(view.sheet_poster)
                }
                else {
                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w154${(data as Movie).posterPath}")
                        .apply(options)
                        .into(view.sheet_poster)
                }

                view.sheet_title.text = getTitle(data.originalLanguage, data.originalTitle, data.title)
                view.sheet_year.text = data.releaseDate?.subSequence(0..3)

                if (isFavorite(type, data)) view.btn_fav.text = resources.getString(R.string.del_from_fav)
                else view.btn_fav.text = resources.getString(R.string.add_to_fav)

                view.btn_fav.setOnClickListener {
                    dialog?.dismiss()
                    if (isFavorite(type, data)) {
                        viewModel.deleteMovie(data)
                        activity?.applicationContext?.let { it1 ->
                            ImageManager(it1, "movie", "poster_${data.id}").deleteImage()
                            ImageManager(it1, "movie", "back_${data.id}").deleteImage()
                        }
                    } else {
                        viewModel.insertMovie(data)
                        activity?.applicationContext?.let { it1 ->
                            ImageManager(it1, "movie", "poster_${data.id}")
                                .downloadImage("https://image.tmdb.org/t/p/w185${data.posterPath}")

                            ImageManager(it1, "movie", "back_${data.id}")
                                .downloadImage("https://image.tmdb.org/t/p/original${data.backdropPath}")
                        }
                    }
                    updateFavorite(MainPagerAdapter.TYPE_MOVIE)
                    getDb(MainPagerAdapter.TYPE_MOVIE)
                }

                view.btn_share.setOnClickListener {
                    dialog?.dismiss()
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/${data.id}")
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }
            }
            MainPagerAdapter.TYPE_TV -> {
                if(index == 11){
                    var poster = wrapper.getDir("tv_show", Context.MODE_PRIVATE)
                    poster = File(poster, "poster_${(data as TVShow).id}.jpg")

                    Glide.with(this)
                        .load(Uri.fromFile(poster))
                        .apply(options)
                        .into(view.sheet_poster)
                }
                else {
                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w154${(data as TVShow).posterPath}")
                        .apply(options)
                        .into(view.sheet_poster)
                }

                view.sheet_title.text = getTitle(data.originalLanguage, data.originalName, data.name)
                view.sheet_year.text = data.firstAirDate?.subSequence(0..3)
                if (isFavorite(type, data)) view.btn_fav.text = resources.getString(R.string.del_from_fav)
                else view.btn_fav.text = resources.getString(R.string.add_to_fav)

                view.btn_fav.setOnClickListener {
                    dialog?.dismiss()
                    if (isFavorite(type, data)) {
                        viewModel.deleteTV(data)
                        activity?.applicationContext?.let { it1 ->
                            ImageManager(it1, "tv_show", "poster_${data.id}").deleteImage()
                            ImageManager(it1, "tv_show", "back_${data.id}").deleteImage()
                        }
                    } else {
                        viewModel.insertTV(data)
                        activity?.applicationContext?.let { it1 ->
                            ImageManager(it1, "tv_show", "poster_${data.id}")
                                .downloadImage("https://image.tmdb.org/t/p/w185${data.posterPath}")

                            ImageManager(it1, "tv_show", "back_${data.id}")
                                .downloadImage("https://image.tmdb.org/t/p/original${data.backdropPath}")
                        }
                    }
                    updateFavorite(MainPagerAdapter.TYPE_TV)
                    getDb(MainPagerAdapter.TYPE_TV)
                }

                view.btn_share.setOnClickListener {
                    dialog?.dismiss()
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/tv/${data.id}")
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }
            }
        }
        dialog?.show()
    }

    private fun getTitle(lang: String?, oriTitle: String?, intTitle: String?): String?{
        return if(Locale.getDefault().language == lang) oriTitle else intTitle
    }

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
        val adapterYear = activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item, viewModel.getYearList()) }
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
