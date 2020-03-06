package com.nf98.moviecatalogue.app.detail.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Credit
import com.nf98.moviecatalogue.api.model.Season
import kotlinx.android.synthetic.main.item_detail_credit.view.*
import kotlinx.android.synthetic.main.item_detail_season.view.*

class CreditAdapter: RecyclerView.Adapter<CreditAdapter.ItemViewHolder<*>>() {
    private var type: Int = 0
    private val list = ArrayList<Any>()

    fun setData(type: Int, items: ArrayList<*>) {
        this.type = type
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<*> {
        return when(type){
            CreditFragment.TYPE_SEASON -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_season, parent, false)
                SeasonViewHolder(view)
            }
            in CreditFragment.TYPE_CAST..CreditFragment.TYPE_CREW -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_credit, parent, false)
                CreditViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }

    override fun onBindViewHolder(holder: ItemViewHolder<*>, position: Int) {
        when(holder){
            is SeasonViewHolder -> holder.bind(list[position] as Season)
            is CreditViewHolder -> holder.bind(list[position] as Credit)
        }
    }

    override fun getItemCount(): Int = list.size

    abstract class ItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun bind(item: T)
    }

    inner class CreditViewHolder(itemView: View) : ItemViewHolder<Credit>(itemView){
        override fun bind(item: Credit){
            with(itemView){
                val options = RequestOptions()
                        .placeholder(R.drawable.img_actor_na)
                        .error(R.drawable.img_actor_na)

                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w138_and_h175_face${item.profilePath}")
                    .apply(options)
                    .into(iv_photo)

                tv_name.text = item.name
                tv_char.text =
                    when(type){
                        1 -> item.character
                        2 -> item.job
                        else -> throw IllegalArgumentException("Invalid view type")
                    }
            }
        }
    }

    inner class SeasonViewHolder(itemView: View) : ItemViewHolder<Season>(itemView){
        override fun bind(item: Season) {
            with(itemView){
                val options = RequestOptions()
                    .placeholder(R.drawable.img_poster_na)
                    .error(R.drawable.img_poster_na)

                Glide.with(this)
                    .load("https://image.tmdb.org/t/p/w154${item.poster_path}")
                    .apply(options)
                    .into(iv_season)

                tv_title.text = item.name
                tv_sub.text = "${item.air_date?.subSequence(0..3)} | ${item.episode_count} episode"
                tv_seasonDesc.text = item.overview
            }
        }
    }
}