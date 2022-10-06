package com.hariankoding.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.hariankoding.storyapp.R
import com.hariankoding.storyapp.databinding.ActivityDetailBinding
import com.hariankoding.storyapp.model.ListStoryItem

class DetailActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setView()
    }

    private fun setView() = with(binding) {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                finish()
            }
            title = getString(R.string.detail_story)
        }
        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY) as ListStoryItem
        ivDetailPhoto.load(story.photoUrl)
        tvDetailName.text = story.name
        tvDetailDescription.text = story.description
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}