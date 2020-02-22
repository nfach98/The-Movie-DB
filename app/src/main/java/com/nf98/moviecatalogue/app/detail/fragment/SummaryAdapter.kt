package com.nf98.moviecatalogue.app.detail.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Credit
import kotlinx.android.synthetic.main.item_summary_cast.view.*
import kotlinx.android.synthetic.main.item_summary_cast.view.tv_char
import kotlinx.android.synthetic.main.item_summary_cast.view.tv_name
import kotlinx.android.synthetic.main.item_summary_crew.view.*

class SummaryAdapter: RecyclerView.Adapter<SummaryAdapter.CreditViewHolder>() {

    companion object{
        const val TYPE_CAST = 0
        const val TYPE_CREW = 1
    }

    private var type: Int = 0
    private val list = ArrayList<Credit>()

    fun setData(type: Int, items: ArrayList<Credit>) {
        this.type = type
        list.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val view = if(type== TYPE_CAST) LayoutInflater.from(parent.context).inflate(R.layout.item_summary_cast, parent, false)
                   else LayoutInflater.from(parent.context).inflate(R.layout.item_summary_crew, parent, false)
        return CreditViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class CreditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Credit){
            with(itemView){
                val options = RequestOptions()
                    .placeholder(R.drawable.img_poster_na)
                    .error(R.drawable.img_poster_na)

                when(type){
                    TYPE_CAST -> {
                        Glide.with(this)
                            .load("https://image.tmdb.org/t/p/w138_and_h175_face${item.profilePath}")
                            .apply(options)
                            .into(iv_photo)

                        tv_name.text = item.name
                        tv_char.text = item.character
                    }
                    TYPE_CREW -> {
                        tv_name.text = item.name
                        tv_job.text = item.job
                    }
                }
            }
        }
    }
}