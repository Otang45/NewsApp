package otang.app.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import otang.app.news.R
import otang.app.news.databinding.CategoryItemBinding
import otang.app.news.model.Category
import otang.app.news.util.AppUtils

class CategoryAdapter(
    private val context: Context,
    private val list: List<Category>,
    private val onItemClick: OnItemClick
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private var checkedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(
                    context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(category: Category) {
            // Size
            val dp5 = AppUtils.dipToPixels(context, 5f).toInt()
            val dp10 = AppUtils.dipToPixels(context, 10f).toInt()
            val dp20 = AppUtils.dipToPixels(context, 20f).toInt()
            when (adapterPosition) {
                0 -> {
                    val params = binding.root
                        .layoutParams as MarginLayoutParams
                    params.setMargins(dp20, dp5, dp5, dp10)
                    binding.root.requestLayout()
                }
                list.size - 1 -> {
                    val params = binding.root
                        .layoutParams as MarginLayoutParams
                    params.setMargins(dp5, dp5, dp20, dp10)
                    binding.root.requestLayout()
                }
                else -> {
                    val params = binding.root
                        .layoutParams as MarginLayoutParams
                    params.setMargins(dp5, dp5, dp5, dp10)
                    binding.root.requestLayout()
                }
            }
            if (checkedPosition == -1) {
                binding.rl.setBackgroundResource(category.image)
                binding.tvCategory.visibility = View.VISIBLE
                binding.ivCheck.visibility = View.GONE
            } else {
                if (checkedPosition == adapterPosition) {
                    binding.rl.setBackgroundResource(R.drawable.source_background)
                    binding.tvCategory.visibility = View.GONE
                    binding.ivCheck.visibility = View.VISIBLE
                } else {
                    binding.rl.setBackgroundResource(category.image)
                    binding.tvCategory.visibility = View.VISIBLE
                    binding.ivCheck.visibility = View.GONE
                }
            }
            binding.tvBottom.text = category.category
            binding.tvCategory.text = category.category
            binding.rl.setOnClickListener {
                binding.rl.setBackgroundResource(R.drawable.source_background)
                binding.tvCategory.visibility = View.GONE
                binding.ivCheck.visibility = View.VISIBLE
                if (checkedPosition != adapterPosition) {
                    notifyItemChanged(checkedPosition)
                    checkedPosition = adapterPosition
                }
                onItemClick.onItemClick(adapterPosition)
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(position: Int)
    }
}