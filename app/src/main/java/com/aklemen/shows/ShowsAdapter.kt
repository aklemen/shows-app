package com.aklemen.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_show_item.view.*

class ShowsAdapter (private val data : List<Show>) : RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_show_item, parent, false)
        return ShowsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    class ShowsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(show: Show){
            itemView.shows_image.setImageResource(show.imgResId)
            itemView.shows_title.text = show.name
            itemView.shows_year.text = show.year
        }

    }

}
