package com.aklemen.shows.ui.shows.episode.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aklemen.shows.R
import com.aklemen.shows.data.api.RestClient
import com.aklemen.shows.data.model.Comment
import kotlinx.android.synthetic.main.view_comment_item.view.*

class CommentsAdapter(private var data: MutableList<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
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

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun setData(list: List<Comment>){
        this.data = list.toMutableList()
        notifyDataSetChanged()
    }


    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(comment: Comment) {
            with(itemView) {
                commentTextUser.text = comment.userEmail
                commentTextComment.text = comment.text
            }
        }

    }

}
