package com.aklemen.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_episode_item.view.*
import java.text.DecimalFormat

class EpisodesAdapter(private var data: MutableList<Episode>) : RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

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

    inner class EpisodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(episode: Episode, position: Int) {
            itemView.episodeTextEpisodeNumber.text = formatEpisode(episode.episodeNumber.season, episode.episodeNumber.episode)
            itemView.episodeTextTitle.text = "${episode.title}"
        }

    }

    fun formatEpisode(season: Int, episode: Int) : String{
        val df = DecimalFormat("00")
        val seasonFormatted = df.format(season)
        val episodeFormatted = df.format(episode)
        return "S$seasonFormatted E$episodeFormatted"
    }
}
