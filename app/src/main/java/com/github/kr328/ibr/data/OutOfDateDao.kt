package com.github.kr328.ibr.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface OutOfDateDao {
    @Query("SELECT out_of_date FROM out_of_date WHERE source = :source")
    fun queryOutOfDate(source: String): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveOutOfDate(outOfDateEntity: OutOfDateEntity)
}