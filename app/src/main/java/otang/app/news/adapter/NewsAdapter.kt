package otang.app.news.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import otang.app.news.databinding.LatestNewsItemBinding
import otang.app.news.model.Articles
import otang.app.news.ui.activity.ReadActivity

class NewsAdapter(private val context: Context, private val list: List<Articles>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: LatestNewsItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, tipe: Int): ViewHolder {
        return ViewHolder(
            LatestNewsItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(list[position].urlToImage).into(holder.binding.iv)
        holder.binding.tvSource.text = list[position].source.name
        holder.binding.tvTitle.text = list[position].title
        holder.binding.tvDescription.text = list[position].description
        val time = list[position].publishedAt.replace("T", " ").replace("Z", " ")
        holder.binding.tvTime.text = time
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ReadActivity::class.java)
            intent.putExtra("url", list[position].url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}