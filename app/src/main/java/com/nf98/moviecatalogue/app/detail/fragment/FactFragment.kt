package com.nf98.moviecatalogue.app.detail.fragment

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Genre
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.app.detail.DetailActivity
import com.nf98.moviecatalogue.app.detail.DetailPagerAdapter
import kotlinx.android.synthetic.main.fragment_detail_fact.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FactFragment : Fragment() {

    private lateinit var movie: Movie
    private lateinit var tvShow: TVShow
    var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_fact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = (activity as DetailActivity).type

        if (type == DetailPagerAdapter.TYPE_MOVIE)
            movie = (activity as DetailActivity).movie
        if (type == DetailPagerAdapter.TYPE_TV)
            tvShow = (activity as DetailActivity).tvShow

        bind()
    }

    private fun bind(){
        when(type){
            DetailPagerAdapter.TYPE_MOVIE -> {
                budget.visibility = View.VISIBLE
                revenue.visibility = View.VISIBLE

                genre.text = getGenre(movie.genres)
                status.text = movie.status
                setDate(movie.releaseDate)
                lang.text = movie.originalLanguage
                runtime.text = getDuration(movie.duration)
                budget.text = if(movie.budget > 0) movie.budget.toString() else "-"
                revenue.text = if(movie.budget > 0) movie.revenue.toString() else "-"
            }
            DetailPagerAdapter.TYPE_TV -> {
                genre.text = getGenre(tvShow.genres)
                status.text = tvShow.status
                setDate(tvShow.firstAirDate)
                lang.text = tvShow.originalLanguage
                runtime.text = getEpisodeDuration(tvShow.duration)
                tvBudget.visibility = View.GONE
                budget.visibility = View.GONE
                tvRevenue.visibility = View.GONE
                revenue.visibility = View.GONE
            }
        }
    }

    private fun setDate(input: String?) {
        val parser = SimpleDateFormat("yyyy-MM-dd")
        val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
        date.text = format.format(parser.parse(input))
    }

    private fun getGenre(genres: List<Genre>?): String {
        var res = ""
        if (genres != null) {
            for(item in genres)
                res = res.plus(if (item == genres[genres.lastIndex]) item.name else "${item.name}, ")
        }

        return res
    }

    private fun getEpisodeDuration(times: List<Int>?): String {
        var res = ""
        if (times != null) {
            for(item in times)
                res = res.plus(if (item == times[times.lastIndex]) getDuration(item) else "${getDuration(item)}, ")
        }

        return res
    }

    private fun getDuration(duration: Int): String {
        var res = "-"
        val hour = duration/60
        val min = duration - hour*60

        if(hour > 0 && min > 0)
            res = "$hour ${resources.getString(R.string.hour)} $min ${resources.getString(R.string.minute)}"
        else if(hour > 0 && min == 0)
            res = "$hour ${resources.getString(R.string.hour)}"
        else if(hour == 0 && min > 0)
            res = "$min ${resources.getString(R.string.minute)}"

        return res
    }
}
