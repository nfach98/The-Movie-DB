package com.nf98.moviecatalogue.app.ui.detail

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
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.app.adapter.DetailPagerAdapter
import com.nf98.moviecatalogue.app.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity() {

    private val arguments: DetailActivityArgs by navArgs()
    private lateinit var viewModel: DetailViewModel

    private lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = arguments.id
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        style()
        viewModel.getMovie(id).observe(this, Observer {
            if (it != null) {
                movie = it
                bindData()
            }
        })

        val pagerAdapter = DetailPagerAdapter(this, supportFragmentManager, DetailPagerAdapter.TYPE_TV)
        pagerDetail.adapter = pagerAdapter
        tabDetail.setupWithViewPager(pagerDetail)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    private fun style(){
        when (movie.score){
            in Int.MIN_VALUE..39 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutRed)
            in 40..59 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutOrange)
            in 60..69 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutYellow)
            in 70..79 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutLime)
            in 80..Int.MAX_VALUE -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutGreen)
        }
    }

    private fun bindData(){
        name.text = movie.title
        setDate(movie.releaseDate)
        score.progress = movie.score*10f
        score.text = movie.score.times(10).toInt().toString()

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w154${movie.posterPath}")
            .into(poster)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w154${movie.backdropPath}")
            .into(backPoster)
    }

    private fun setDate(input: String?) {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        date.text = format.format(parser.parse(input))
    }
}
