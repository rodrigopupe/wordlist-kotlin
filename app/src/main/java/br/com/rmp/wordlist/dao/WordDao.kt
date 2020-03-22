package br.com.rmp.wordlist.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.rmp.wordlist.entity.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun getWordsSortedAlphabetically(): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWord(word: String)

    @Query("DELETE FROM word_table")
    suspend fun deleteAllWords()
}