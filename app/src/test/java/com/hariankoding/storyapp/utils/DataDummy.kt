package com.hariankoding.storyapp.utils

import com.hariankoding.storyapp.data.database.entity.ListStoryEntity

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryEntity> {
        val item: MutableList<ListStoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryEntity(
                id = i.toString(),
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666845359145_osWD6l_I.jpg",
                createdAt = "2022-10-27T04:35:59.148Z",
                name = "nama $i",
                description = "desc $i",
                lon = 0.0,
                lat = 0.0
            )
            item.add(story)
        }
        return item
    }
}