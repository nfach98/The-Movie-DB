package com.nf98.moviecatalogue.app.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Genre
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_detail_summary.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

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

    private fun bind(){
        if(type == DetailPagerAdapter.TYPE_MOVIE){
            setTitle(movie.originalLanguage, movie.originalTitle, movie.title)
            setDate(movie.releaseDate)
            setScore(movie.score)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185${movie.posterPath}")
                .into(poster)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original${movie.backdropPath}")
                .into(backPoster)
        }
        if(type == DetailPagerAdapter.TYPE_TV){
            setTitle(tvShow.originalLanguage, tvShow.originalName, tvShow.name)
            setDate(tvShow.firstAirDate)
            setScore(tvShow.score)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w185${tvShow.posterPath}")
                .into(poster)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original${tvShow.backdropPath}")
                .into(backPoster)
        }
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
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        date.text = format.format(parser.parse(input))
    }

}
