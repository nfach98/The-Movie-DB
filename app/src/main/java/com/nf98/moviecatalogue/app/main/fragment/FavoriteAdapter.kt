package com.nf98.moviecatalogue.app.main.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Movie
import com.nf98.moviecatalogue.api.model.TVShow
import kotlinx.android.synthetic.main.item_fav.view.*
import kotlinx.android.synthetic.main.item_list.view.donut_score
import kotlinx.android.synthetic.main.item_list.view.iv_poster
import kotlinx.android.synthetic.main.item_list.view.tv_date
import kotlinx.android.synthetic.main.item_list.view.tv_desc
import kotlinx.android.synthetic.main.item_list.view.tv_name
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.ItemViewHolder<*>>() {

    companion object {
        const val TYPE_MOVIE = 0
        const val TYPE_TV = 1
    }

    var list = ArrayList<Any>()
    private var onItemClickCallback: OnItemClickCallback? = null
    private var onItemDeletedCallback: OnItemClickCallback? = null

    fun setData(items: ArrayList<*>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback }

    fun setOnDeleteClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemDeletedCallback = onItemClickCallback }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<*> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fav, parent, false)

        return when(viewType) {
            TYPE_MOVIE -> MovieViewHolder(view)
            TYPE_TV -> TVShowViewHolder(view)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder<*>, position: Int) {
        when (holder) {
            is MovieViewHolder -> holder.bind(list[position] as Movie)
            is TVShowViewHolder -> holder.bind(list[position] as TVShow)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Movie -> TYPE_MOVIE
            is TVShow -> TYPE_TV
            else -> throw IllegalArgumentException("Invalid type of data ${list[position].javaClass.name}")
        }
    }

    override fun getItemCount(): Int = list.size


    abstract class ItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }

    inner class MovieViewHolder(itemView: View): ItemViewHolder<Movie>(itemView) {
        @SuppressLint("SdCardPath")
        override fun bind(item: Movie) {
            with(itemView) {
                val options = RequestOptions()
                    .placeholder(R.drawable.img_poster_na)
                    .error(R.drawable.img_poster_na)

                Glide.with(this)
                    .load(Uri.fromFile(File("/data/data/com.nf98.moviecatalogue/app_movie/poster_${item.id}.jpg")))
                    .apply(options)
                    .into(iv_poster)

                tv_name.text = getTitle(item.originalLanguage, item.originalTitle, item.title)
                tv_desc.text = item.overview
                tv_date.text = getDate(item.releaseDate)
                setDonut(item.score)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(item) }
                btn_delete.setOnClickListener { onItemDeletedCallback?.onItemClicked(item) }
            }
        }

        private fun getTitle(lang: String?, oriTitle: String?, intTitle: String?): String?{
            return if(Locale.getDefault().language == lang) oriTitle else intTitle
        }

        private fun setDonut(input: Float){
            val score = input.times(10.0).toInt()

            with(itemView) {
                donut_score.progress = score.toFloat()
                if (score > 0) donut_score.text = score.toString()
                else donut_score.text = "NR"

                when (score) {
                    in Int.MIN_VALUE..39 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutRed)
                    in 40..59 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutOrange)
                    in 60..69 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutYellow)
                    in 70..79 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutLime)
                    in 80..Int.MAX_VALUE -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutGreen)
                }
            }
        }

        private fun getDate(input: String?): String {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
            return format.format(parser.parse(input))
        }
    }

    inner class TVShowViewHolder(itemView: View): ItemViewHolder<TVShow>(itemView) {
        override fun bind(item: TVShow) {
            with(itemView) {
                val options = RequestOptions()
                    .placeholder(R.drawable.img_poster_na)
                    .error(R.drawable.img_poster_na)

                Glide.with(this)
                    .load(Uri.fromFile(File(item.posterPath)))
                    .apply(options)
                    .into(iv_poster)

                tv_name.text = getTitle(item.originalLanguage, item.originalName, item.name)
                tv_desc.text = item.overview
                tv_date.text = getDate(item.firstAirDate)
                setDonut(item.score)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(item) }
                btn_delete.setOnClickListener { onItemDeletedCallback?.onItemClicked(item) }
            }
        }

        private fun getTitle(lang: String?, oriTitle: String?, intTitle: String?): String?{
            return if(Locale.getDefault().language == lang) oriTitle else intTitle
        }

        private fun setDonut(input: Float){
            val score = input.times(10.0).toInt()

            with(itemView) {
                donut_score.progress = score.toFloat()
                if (score > 0) donut_score.text = score.toString()
                else donut_score.text = "NR"

                when (score) {
                    in Int.MIN_VALUE..39 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutRed)
                    in 40..59 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutOrange)
                    in 60..69 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutYellow)
                    in 70..79 -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutLime)
                    in 80..Int.MAX_VALUE -> donut_score.finishedStrokeColor = ContextCompat.getColor(context, R.color.donutGreen)
                }
            }
        }

        private fun getDate(input: String?): String {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val format = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
            return format.format(parser.parse(input))
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Any)
    }
}