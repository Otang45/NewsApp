package otang.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import otang.news.adapter.NewsAdapter.ViewHolder;
import otang.news.databinding.LatestNewsItemBinding;
import otang.news.model.Articles;
import otang.news.ui.activity.ReadActivity;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

	public class ViewHolder extends RecyclerView.ViewHolder {
		private LatestNewsItemBinding binding;

		public ViewHolder(LatestNewsItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
	}

	private Context context;
	private List<Articles> list;

	public NewsAdapter(Context context, List<Articles> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int tipe) {
		return new ViewHolder(LatestNewsItemBinding.inflate(LayoutInflater.from(context), viewGroup, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		Glide.with(context).load(list.get(position).getUrlToImage()).into(holder.binding.iv);
		holder.binding.tvSource.setText(list.get(position).getSource().getName());
		holder.binding.tvTitle.setText(list.get(position).getTitle());
		holder.binding.tvDescription.setText(list.get(position).getDescription());
		String time = list.get(position).getPublishedAt().replace("T", " ").replace("Z", " ");
		holder.binding.tvTime.setText(time);
		holder.binding.getRoot().setOnClickListener(v -> {
			Intent intent = new Intent(context, ReadActivity.class);
			intent.putExtra("url", list.get(position).getUrl());
			context.startActivity(intent);
		});
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

}