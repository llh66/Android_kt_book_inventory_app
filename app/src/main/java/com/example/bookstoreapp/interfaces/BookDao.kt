package com.example.bookstoreapp.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bookstoreapp.models.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: BookEntity)
    @Query("SELECT * FROM books ORDER BY " +
            "CASE WHEN :isAsc = 1 THEN quantity END ASC," +
            "CASE WHEN :isAsc = 0 THEN quantity END DESC")
    fun getAllBooksOptionalSortByQuantity(isAsc: Boolean?): Flow<List<BookEntity>>
    @Update
    suspend fun updateBook(book: BookEntity)
    @Delete
    suspend fun deleteBook(book: BookEntity)
}