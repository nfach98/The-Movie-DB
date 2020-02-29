package com.nf98.moviecatalogue.app.detail

import android.content.ContentValues
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.db.DatabaseContract
import com.nf98.moviecatalogue.db.MovieHelper
import com.nf98.moviecatalogue.db.TVShowHelper
import com.nf98.moviecatalogue.helper.ImageDownloader
import com.nf98.moviecatalogue.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var movieHelper: MovieHelper
    private lateinit var tvShowHelper: TVShowHelper

    private val arguments: DetailActivityArgs by navArgs()
    lateinit var viewModel: DetailViewModel

    var movie = Movie()
    var tvShow = TVShow()

    var id = 0
    var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        showLoading(true)

        prepSQLite()
        btnFav.setOnClickListener(this)

        id = arguments.id
        type = arguments.type

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        when(type){
            DetailPagerAdapter.TYPE_MOVIE -> {
                viewModel.getMovie(id).observe(this, Observer {
                    if (it != null) {
                        movie = it
                        setAdapter(arguments.type)
                        bind()
                    }
                })
            }
            DetailPagerAdapter.TYPE_TV -> {
                viewModel.getTVShow(id).observe(this, Observer {
                    if (it != null) {
                        tvShow = it
                        setAdapter(arguments.type)
                        bind()
                    }
                })
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieHelper.close()
        tvShowHelper.close()
    }

    private fun bind(){
        val options = RequestOptions()
            .placeholder(R.drawable.img_poster_na)
            .error(R.drawable.img_poster_na)

        if(type == DetailPagerAdapter.TYPE_MOVIE){
            setTitle(movie.originalLanguage, movie.originalTitle, movie.title)
            setDate(movie.releaseDate)
            setScore(movie.score)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185${movie.posterPath}")
                .apply(options)
                .into(poster)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
                .apply(options)
                .into(backPoster)
        }
        if(type == DetailPagerAdapter.TYPE_TV){
            setTitle(tvShow.originalLanguage, tvShow.originalName, tvShow.name)
            setDate(tvShow.firstAirDate)
            setScore(tvShow.score)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185${tvShow.posterPath}")
                .apply(options)
                .into(poster)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original${tvShow.backdropPath}")
                .apply(options)
                .into(backPoster)
        }
        showLoading(false)
    }

    private fun prepSQLite(){
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        tvShowHelper = TVShowHelper.getInstance(applicationContext)
        tvShowHelper.open()
    }

    private fun setAdapter(type: Int){
        val pagerAdapter = DetailPagerAdapter(this, supportFragmentManager, type)
        pagerDetail.adapter = pagerAdapter
        tabDetail.setupWithViewPager(pagerDetail)
    }

    private fun setTitle(lang: String?, oriTitle: String?, intTitle: String?){
        if(Locale.getDefault().language == lang){ name.text = oriTitle }
        else { name.text = intTitle }
    }

    private fun setScore(input: Float){
        val vote = input.times(10f).toInt()
        score.progress = vote.toFloat()

        if (vote > 0) score.text = vote.toString()
        else score.text = "NR"

        when (vote) {
            in Int.MIN_VALUE..39 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutRed)
            in 40..59 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutOrange)
            in 60..69 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutYellow)
            in 70..79 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutLime)
            in 80..Int.MAX_VALUE -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutGreen)
        }
    }

    private fun setDate(input: String?) {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        date.text = format.format(parser.parse(input))
    }

    private fun showLoading(state: Boolean) { prog.visibility = if(state) View.VISIBLE else View.GONE }

    private fun addToFavorite() : Long? {
        var status: Long? = null

        if(!isFavorite()) {
            when(type){
                DetailPagerAdapter.TYPE_MOVIE -> {
                    val poster = ImageDownloader(applicationContext, "movie", "${movie.id}_poster_mov")
                    poster.execute("https://image.tmdb.org/t/p/w185${movie.posterPath}")

                    val backdrop = ImageDownloader(applicationContext, "movie", "${movie.id}_backdrop_mov")
                    backdrop.execute("https://image.tmdb.org/t/p/original${movie.backdropPath}")

                    val values = ContentValues()
                    values.put(DatabaseContract.MovieColumns.TITLE, movie.title)
                    values.put(DatabaseContract.MovieColumns.RELEASE_DATE, movie.releaseDate)
                    values.put(DatabaseContract.MovieColumns.SCORE, movie.score)
                    values.put(DatabaseContract.MovieColumns.OVERVIEW, movie.overview)
                    values.put(DatabaseContract.MovieColumns.POSTER_PATH, poster.path)
                    values.put(DatabaseContract.MovieColumns.BACKDROP_PATH, backdrop.path)
                    values.put(DatabaseContract.MovieColumns.ID, movie.id)
                    status = movieHelper.insert(values)
                }
                DetailPagerAdapter.TYPE_TV -> {
                    val poster = ImageDownloader(applicationContext, "tv", "${tvShow.id}_poster_tv")
                    poster.execute("https://image.tmdb.org/t/p/w185${tvShow.posterPath}")

                    val backdrop = ImageDownloader(applicationContext, "tv", "${tvShow.id}_backdrop_tv")
                    backdrop.execute("https://image.tmdb.org/t/p/original${tvShow.backdropPath}")

                    val values = ContentValues()
                    values.put(DatabaseContract.TVShowColumns.NAME, tvShow.name)
                    values.put(DatabaseContract.TVShowColumns.FIRST_AIR_DATE, tvShow.firstAirDate)
                    values.put(DatabaseContract.TVShowColumns.SCORE, tvShow.score)
                    values.put(DatabaseContract.TVShowColumns.OVERVIEW, tvShow.overview)
                    values.put(DatabaseContract.TVShowColumns.POSTER_PATH, poster.path)
                    values.put(DatabaseContract.TVShowColumns.BACKDROP_PATH, backdrop.path)
                    values.put(DatabaseContract.TVShowColumns.ID, tvShow.id)
                    status = tvShowHelper.insert(values)
                }
            }
        }
        return status
    }

    private fun isFavorite() : Boolean {
        var result = false

        GlobalScope.launch(Dispatchers.Main) {
            when(type){
                DetailPagerAdapter.TYPE_MOVIE -> {
                    val deferredMovies = async(Dispatchers.IO) {
                        val cursor = movieHelper.queryAll()
                        MappingHelper.mapMovieCursorToArrayList(cursor)
                    }
                    val list = deferredMovies.await()
                    if (list.size > 0) {
                        for(item in list){
                            if(item.id == movie.id){
                                result = true
                                break
                            }
                        }
                    }
                }
                DetailPagerAdapter.TYPE_TV -> {
                    val deferredTVShows = async(Dispatchers.IO) {
                        val cursor = tvShowHelper.queryAll()
                        MappingHelper.mapTVShowCursorToArrayList(cursor)
                    }
                    val list = deferredTVShows.await()
                    if (list.size > 0) {
                        for(item in list){
                            if(item.id == tvShow.id){
                                result = true
                                break
                            }
                        }
                    }
                }
            }
        }

        return result
    }

    override fun onClick(v: View) {
        if(v.id == R.id.btnFav){
            val result = addToFavorite()
            if (result != null && result > 0)
                Toast.makeText(this@DetailActivity, resources.getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show()
        }
    }

}
