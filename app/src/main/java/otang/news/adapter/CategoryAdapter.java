package otang.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import otang.news.R;
import otang.news.adapter.CategoryAdapter.ViewHolder;
import otang.news.databinding.CategoryItemBinding;
import otang.news.model.Category;
import otang.news.util.AppUtils;
import otang.news.util.ColourUtils;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

	private Context context;
	private List<Category> list;
	private OnItemClick onItemClick;
	private RecyclerView recyclerView;

	private int checkedPosition = 0;

	public CategoryAdapter(Context context, List<Category> list, RecyclerView recyclerView, OnItemClick onItemClick) {
		this.context = context;
		this.list = list;
		this.recyclerView = recyclerView;
		this.onItemClick = onItemClick;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(context), parent, false));
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.bind(list.get(position));
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private CategoryItemBinding binding;

		public ViewHolder(CategoryItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}

		public void bind(final Category category) {
			// Size
			int dp5 = (int) AppUtils.dipToPixels(context, 5);
			int dp10 = (int) AppUtils.dipToPixels(context, 10);
			int dp16 = (int) AppUtils.dipToPixels(context, 16);
			int dp20 = (int) AppUtils.dipToPixels(context, 20);
			if (getAdapterPosition() == 0) {
				ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.getRoot()
						.getLayoutParams();
				params.setMargins(dp20, dp5, dp5, dp10);
				binding.getRoot().requestLayout();
			} else if (getAdapterPosition() == list.size() - 1) {
				ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.getRoot()
						.getLayoutParams();
				params.setMargins(dp5, dp5, dp20, dp10);
				binding.getRoot().requestLayout();
			} else {
				ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) binding.getRoot()
						.getLayoutParams();
				params.setMargins(dp5, dp5, dp5, dp10);
				binding.getRoot().requestLayout();
			}
			if (checkedPosition == -1) {
				binding.rl.setBackgroundResource(category.getImage());
				binding.tvCategory.setVisibility(View.VISIBLE);
				binding.ivCheck.setVisibility(View.GONE);
			} else {
				if (checkedPosition == getAdapterPosition()) {
					binding.rl.setBackgroundResource(R.drawable.source_background);
					binding.tvCategory.setVisibility(View.GONE);
					binding.ivCheck.setVisibility(View.VISIBLE);
				} else {
					binding.rl.setBackgroundResource(category.getImage());
					binding.tvCategory.setVisibility(View.VISIBLE);
					binding.ivCheck.setVisibility(View.GONE);
				}
			}
			binding.tvBottom.setText(category.getCategory());
			binding.tvCategory.setText(category.getCategory());
			binding.rl.setOnClickListener(v -> {
				binding.rl.setBackgroundResource(R.drawable.source_background);
				binding.tvCategory.setVisibility(View.GONE);
				binding.ivCheck.setVisibility(View.VISIBLE);
				if (checkedPosition != getAdapterPosition()) {
					notifyItemChanged(checkedPosition);
					checkedPosition = getAdapterPosition();
				}
				onItemClick.onItemClick(getAdapterPosition());
			});
		}
	}

	public interface OnItemClick {
		public void onItemClick(int position);
	}

}