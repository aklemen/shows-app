package com.aklemen.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_episode_item.view.*
import kotlinx.android.synthetic.main.view_show_item.view.*

class EpisodesAdapter (private val data : MutableList<Episode>) : RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_episode_item, parent, false)
        return EpisodesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    inner class EpisodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(episode: Episode, position: Int){
            itemView.episode_text_title.text = "$position. ${episode.title}"
        }

    }

}
