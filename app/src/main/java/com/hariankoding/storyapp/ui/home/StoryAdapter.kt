package com.hariankoding.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hariankoding.storyapp.databinding.ItemStoryBinding
import com.hariankoding.storyapp.model.ListStoryItem
import com.hariankoding.storyapp.ui.detail.DetailActivity

class StoryAdapter :
    ListAdapter<ListStoryItem, StoryAdapter.StoryViewHolder>(diffCallback) {

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListStoryItem) {
            with(binding) {
                ivItemPhoto.load(item.photoUrl)
                tvItemName.text = item.name
                cvStory.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_STORY, item)
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "photo"),
                            Pair(tvItemName, "name")
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder =
        StoryViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean = oldItem == newItem

        }
    }
}