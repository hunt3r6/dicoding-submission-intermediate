package com.hariankoding.storyapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hariankoding.storyapp.data.database.entity.ListStoryEntity
import com.hariankoding.storyapp.data.network.model.ListStoryItem

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<ListStoryEntity>)

    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, ListStoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}