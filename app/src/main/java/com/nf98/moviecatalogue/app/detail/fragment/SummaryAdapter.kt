package com.nf98.moviecatalogue.app.detail.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nf98.moviecatalogue.R
import com.nf98.moviecatalogue.api.model.Credit
import com.nf98.moviecatalogue.app.detail.DetailPagerAdapter
import kotlinx.android.synthetic.main.item_summary_credit.view.*

class SummaryAdapter: RecyclerView.Adapter<SummaryAdapter.CreditViewHolder>() {

    private var type: Int = 0
    private val list = ArrayList<Credit>()

    fun setData(type: Int, items: ArrayList<Credit>) {
        this.type = type
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_summary_credit, parent, false)
        return CreditViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreditViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class CreditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Credit){
            with(itemView){
                if(type == 1) {
                    iv_photo.visibility = View.VISIBLE
                    Glide.with(this)
                        .load("https://image.tmdb.org/t/p/w138_and_h175_face${item.profilePath}")
                        .into(iv_photo)
                }
                else iv_photo.visibility = View.GONE

                tv_name.text = item.name
                tv_char.text = if(type == 1) item.character else item.job
            }
        }
    }
}