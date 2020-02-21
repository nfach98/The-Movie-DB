package com.nf98.moviecatalogue.app.detail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Credit
import com.nf98.moviecatalogue.api.model.Genre
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import com.nf98.moviecatalogue.app.detail.DetailActivity
import com.nf98.moviecatalogue.app.detail.DetailPagerAdapter
import kotlinx.android.synthetic.main.fragment_detail_summary.*

class SummaryFragment : Fragment() {

    private lateinit var movie: Movie
    private lateinit var tvShow: TVShow
    var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_summary, container, false)
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
                showSeason(false)

                tvRateDurGenre.text = "${getDuration(movie.duration)} | ${getGenre(movie.genres)}"
                desc.text = movie.overview
            }
            DetailPagerAdapter.TYPE_TV -> {
                showSeason(true)

                tvRateDurGenre.text = "${getEpisodeDuration(tvShow.duration)} | ${getGenre(tvShow.genres)}"
                desc.text = tvShow.overview
            }
        }
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

    private fun showSeason(state: Boolean){
        if(state){
            tvSeason.visibility = View.VISIBLE
            curSeason.visibility = View.VISIBLE
            tvCurSeasonTitle.visibility = View.VISIBLE
            tvCurSeasonSub.visibility = View.VISIBLE
            tvCurSeasonDesc.visibility = View.VISIBLE
        }
        else{
            tvSeason.visibility = View.GONE
            curSeason.visibility = View.GONE
            tvCurSeasonTitle.visibility = View.GONE
            tvCurSeasonSub.visibility = View.GONE
            tvCurSeasonDesc.visibility = View.GONE
        }
    }
}
