package com.aklemen.shows.ui.shows.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aklemen.shows.R
import com.aklemen.shows.data.model.Episode
import kotlinx.android.synthetic.main.view_episode_item.view.*
import java.text.DecimalFormat

class EpisodesAdapter(private var data: MutableList<Episode>, val action: (Episode) -> Unit) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_episode_item, parent, false)
        return EpisodesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(list: List<Episode>){
        this.data = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class EpisodesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(episode: Episode) {
//            itemView.episodeTextEpisodeNumber.text = formatEpisode(episode.episodeNumber, episode.season)
            itemView.episodeTextEpisodeNumber.text = "S${episode.episodeNumber} E${episode.season}"
            itemView.episodeTextTitle.text = episode.title
            itemView.setOnClickListener {
                action(episode)
            }
        }

    }

    fun formatEpisode(season: String, episode: String) : String{
        val df = DecimalFormat("00")
        val seasonFormatted = df.format(season)
        val episodeFormatted = df.format(episode)
        return "S$seasonFormatted E$episodeFormatted"
    }
}
