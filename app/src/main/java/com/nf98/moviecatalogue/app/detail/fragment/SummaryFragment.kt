package com.nf98.moviecatalogue.app.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.*
import com.nf98.moviecatalogue.app.detail.DetailActivity
import com.nf98.moviecatalogue.app.detail.DetailPagerAdapter
import com.nf98.moviecatalogue.app.detail.DetailViewModel
import com.nf98.moviecatalogue.app.detail.DetailViewModel.OnFailure
import kotlinx.android.synthetic.main.fragment_detail_summary.*

class SummaryFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var movie: Movie
    private lateinit var tvShow: TVShow
    var type = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*tvAllSeason.setOnClickListener(this)
        tvFullCast.setOnClickListener(this)
        tvFullCrew.setOnClickListener(this)*/

        type = (activity as DetailActivity).type

        if (type == DetailPagerAdapter.TYPE_MOVIE) movie = (activity as DetailActivity).movie
        if (type == DetailPagerAdapter.TYPE_TV) tvShow = (activity as DetailActivity).tvShow

        viewModel = (activity as DetailActivity).viewModel

        rvTopCast.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        rvTopCrew.apply {
            layoutManager = GridLayoutManager(activity, 2)
            isNestedScrollingEnabled = false
            setHasFixedSize(false)
        }

        bind()
    }

    private fun bind(){
        when(type){
            DetailPagerAdapter.TYPE_MOVIE -> {
                showSeason(false)
                showCrew(true)
                tvDurGenre.text = "${getDuration(movie.duration)} | ${getGenre(movie.genres)}"
                desc.text = movie.overview
                getCasts()
                getCrews()
            }
            DetailPagerAdapter.TYPE_TV -> {
                showSeason(true)
                showCrew(false)
                tvDurGenre.text = "${getEpisodeDuration(tvShow.duration)} | ${getGenre(tvShow.genres)}"
                desc.text = tvShow.overview
                getCasts()
                getSeason()
            }
        }
    }

    private fun getSeason(){
        val list = tvShow.seasons
        var season = Season()

        if (list != null) {
            for (item in list) {
                if(item.season_number == tvShow.number_of_seasons)
                    season = item
            }

            val options = RequestOptions()
                .placeholder(R.drawable.img_poster_na)
                .error(R.drawable.img_poster_na)

            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w154${season.poster_path}")
                .apply(options)
                .into(ivCurSeason)

            tvCurSeasonTitle.text = season.name
            tvCurSeasonSub.text = "${season.air_date?.subSequence(0..3)} | ${season.episode_count} episode"
            tvCurSeasonDesc.text = season.overview
        }
        else showSeason(false)
    }

    private fun getCasts(){
        viewModel.getCasts(type, (activity as DetailActivity).id, object : OnFailure{
                override fun fail() = showCast(false)
            }).observe(this, Observer {
            if (it != null && it.size != 0) {
                val list = ArrayList<Credit>()
                if(it.size >= 5) {
                    for (index in 0..4)
                        list.add(it[index])
                }
                else{
                    for (item in it)
                        list.add(item)
                }
                showCast(true)
                refreshList(SummaryAdapter.TYPE_CAST, list)
            }
            else showCast(false)
        })
    }

    private fun getCrews(){
        viewModel.getCrews(type, (activity as DetailActivity).id, object : OnFailure{
            override fun fail() = showCrew(false)
            }).observe(this, Observer {
            if (it != null && it.size != 0){
                val list = ArrayList<Credit>()
                if(it.size >= 4) {
                    for (index in 0..3)
                        list.add(it[index])
                }
                else{
                    for (item in it)
                        list.add(item)
                }
                showCrew(true)
                refreshList(SummaryAdapter.TYPE_CREW, list)
            }
            else showCrew(false)
        })
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

    private fun refreshList(type: Int, it: ArrayList<Credit>) {
        val adapter = SummaryAdapter()
        adapter.apply {
            notifyDataSetChanged()
            setData(type, it)
        }
        when(type){
            SummaryAdapter.TYPE_CAST -> rvTopCast.adapter = adapter
            SummaryAdapter.TYPE_CREW -> rvTopCrew.adapter = adapter
        }
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

    private fun showCrew(state: Boolean){
        if(state){
            tvTopCrew.visibility = View.VISIBLE
            rvTopCrew.visibility = View.VISIBLE
        }
        else{
            tvTopCrew.visibility = View.GONE
            rvTopCrew.visibility = View.GONE
        }
    }

    private fun showCast(state: Boolean){
        if(state){
            tvTopCast.visibility = View.VISIBLE
            rvTopCast.visibility = View.VISIBLE
        }
        else{
            tvTopCast.visibility = View.GONE
            rvTopCast.visibility = View.GONE
        }
    }

    /*private fun movePager(position: Int){
        Handler().post { pagerDetail.currentItem = position }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.tvAllSeason -> if(type == DetailPagerAdapter.TYPE_TV) movePager( 1)
            R.id.tvFullCast -> {
                if(type == DetailPagerAdapter.TYPE_MOVIE) movePager(1)
                if(type == DetailPagerAdapter.TYPE_TV) movePager(2)
            }
            R.id.tvFullCrew -> if(type == DetailPagerAdapter.TYPE_MOVIE) movePager(2)
        }
    }*/
}
