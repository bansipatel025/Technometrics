package com.technometrics.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.technometrics.R
import com.technometrics.data.api.RestConstant
import com.technometrics.data.model.movie.ResultsItem
import com.technometrics.utils.AppUtils
import java.util.Objects

class MovieListAdapter(private val context: Context, private val mList: List<ResultsItem?>) :
    RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    private var onItemClickLister: OnItemClickLister? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_movie_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]
        holder.textView.text = data!!.title
        holder.tvDate.text = AppUtils.dateChange(data.releaseDate!!)
        holder.txtMoviestar.text = data.voteAverage!!

        holder.itemView.setOnClickListener()
        {
            onItemClickLister!!.itemClicked(holder.itemView, position, data.id!!)
        }
        Glide.with(Objects.requireNonNull(context))
            .load(RestConstant.image_path + mList[position]!!.posterPath)
            .into(holder.imageView)
    }

    fun setOnItemClickLister(onItemClickLister: OnItemClickLister?) {
        this.onItemClickLister = onItemClickLister
    }

    interface OnItemClickLister {
        fun itemClicked(view: View?, position: Int, catId: Int)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivPosterImage)
        val textView: TextView = itemView.findViewById(R.id.textView)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val txtMoviestar: TextView = itemView.findViewById(R.id.txtMoviestar)
    }
}