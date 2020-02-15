package com.nf98.moviecatalogue.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.adapter.DetailPagerAdapter
import com.nf98.moviecatalogue.model.Movie
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private val arguments: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val id = arguments.id

//        style()
        val pagerAdapter = DetailPagerAdapter(this, supportFragmentManager, DetailPagerAdapter.TYPE_TV)
        pagerDetail.adapter = pagerAdapter
        tabDetail.setupWithViewPager(pagerDetail)

//        data()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home)
            finish()

        return super.onOptionsItemSelected(item)
    }

    private fun style(){
        /*when (movie.score){
            in Int.MIN_VALUE..39 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutRed)
            in 40..59 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutOrange)
            in 60..69 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutYellow)
            in 70..79 -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutLime)
            in 80..Int.MAX_VALUE -> score.finishedStrokeColor = ContextCompat.getColor(this, R.color.donutGreen)
        }*/
    }
}
