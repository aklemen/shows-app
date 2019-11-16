package com.aklemen.shows.ui.shows.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aklemen.shows.R
import com.aklemen.shows.data.model.Show
import com.aklemen.shows.data.api.RestClient
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_show_item.view.*

class ShowsAdapter(private val data: MutableList<Show>, val action: (Show) -> Unit) :
    RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_show_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ShowsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(show: Show) {
            with(itemView) {
                Picasso.get().load(RestClient.BASE_URL + show.imageUrl).into(showImage)
                showTextTitle.text = show.title
                setOnClickListener {
                    action(show)
                }
            }
        }

    }

}
