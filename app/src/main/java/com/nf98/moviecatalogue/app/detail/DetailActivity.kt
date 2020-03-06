package com.nf98.moviecatalogue.app.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.app.ViewModelFactory
import com.nf98.moviecatalogue.helper.ImageDownloader
import com.nf98.moviecatalogue.helper.Inject
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(){

    private val arguments: DetailActivityArgs by navArgs()
    lateinit var viewModel: DetailViewModel
    private lateinit var viewModelFactory: ViewModelFactory

    var movie = Movie()
    var tvShow = TVShow()

    var isFavorite = false
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

        id = arguments.id
        type = arguments.type
        isFavorite = arguments.favorite

        viewModelFactory = Inject.provideViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        Log.d("MovieDB", "${arguments.id}")

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menu[0].icon =  if(isFavorite) ContextCompat.getDrawable(this, R.drawable.ic_favorite_true_black_24dp)
                        else ContextCompat.getDrawable(this, R.drawable.ic_favorite_false_black_24dp)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
            R.id.menu_fav -> {
                when {
                    isFavorite -> {
                        deleteFromFavorite()
                        Toast.makeText(this@DetailActivity, resources.getString(R.string.del_from_fav), Toast.LENGTH_SHORT).show()
                        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_false_black_24dp)
                    }
                    else -> {
                        addToFavorite()
                        Toast.makeText(this@DetailActivity, resources.getString(R.string.added_to_fav), Toast.LENGTH_SHORT).show()
                        item.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_true_black_24dp)
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        Inject.closeDatabase()
    }

    private fun bind(){
        val options = RequestOptions()
            .placeholder(R.drawable.img_poster_na)
            .error(R.drawable.img_poster_na)

        when (type) {
            DetailPagerAdapter.TYPE_MOVIE -> {
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
            DetailPagerAdapter.TYPE_TV -> {
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
        }
        showLoading(false)
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

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun setDate(input: String?) {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        date.text = format.format(parser.parse(input))
    }

    private fun showLoading(state: Boolean) { prog.visibility = if(state) View.VISIBLE else View.GONE }

    private fun addToFavorite() {
        when(type){
            DetailPagerAdapter.TYPE_MOVIE -> {
                viewModel.insertMovie(movie)
                isFavorite = true

                val poster = ImageDownloader(applicationContext, "movie", "poster_${movie.id}")
                poster.downloadImage("https://image.tmdb.org/t/p/w185${movie.posterPath}")

                val backdrop = ImageDownloader(applicationContext, "movie", "back_${movie.id}")
                backdrop.downloadImage("https://image.tmdb.org/t/p/original${movie.backdropPath}")

                Log.d("MovieDB", "$isFavorite")
            }
            DetailPagerAdapter.TYPE_TV -> {
                viewModel.insertTV(tvShow)
                isFavorite = true

                val poster = ImageDownloader(applicationContext, "tv_show", "poster_${tvShow.id}")
                poster.downloadImage("https://image.tmdb.org/t/p/w185${tvShow.posterPath}")

                val backdrop = ImageDownloader(applicationContext, "tv_show", "back_${tvShow.id}")
                backdrop.downloadImage("https://image.tmdb.org/t/p/original${tvShow.backdropPath}")

                Log.d("MovieDB", "$isFavorite")
            }
        }
    }

    private fun deleteFromFavorite() {
        when(type){
            DetailPagerAdapter.TYPE_MOVIE -> {
                viewModel.deleteMovie(movie)
                isFavorite = false
                Log.d("MovieDB", "$isFavorite")
            }
            DetailPagerAdapter.TYPE_TV -> {
                viewModel.deleteTV(tvShow)
                isFavorite = false
                Log.d("MovieDB", "$isFavorite")
            }
        }
    }
}
