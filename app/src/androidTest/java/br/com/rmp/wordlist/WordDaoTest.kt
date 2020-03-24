package br.com.rmp.wordlist

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.rmp.wordlist.database.WordDao
import br.com.rmp.wordlist.database.WordRoomDatabase
import br.com.rmp.wordlist.entity.Word
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WordDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wordDao: WordDao
    private lateinit var db: WordRoomDatabase

    @Before
    fun createDB() {
        val context: Context = ApplicationProvider.getApplicationContext()

        // Using an in-memory database because the information stored here disappears when the process is killed
        db = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        wordDao = db.wordDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val word = Word("word")
        wordDao.insertWord(word)
        val allWords = wordDao.getWordsSortedAlphabetically().waitForValue()
        assertEquals(allWords.first().word, word.word)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val word = Word("AAA")
        wordDao.insertWord(word)
        val word2 = Word("BBB")
        wordDao.insertWord(word2)
        val allWords = wordDao.getWordsSortedAlphabetically().waitForValue()
        assertEquals(allWords.first().word, word.word)
        assertEquals(allWords[1].word, word2.word)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val word = Word("word")
        wordDao.insertWord(word)
        val word2 = Word("word2")
        wordDao.insertWord(word2)
        wordDao.deleteAllWords()
        val allWords = wordDao.getWordsSortedAlphabetically().waitForValue()
        assertTrue(allWords.isEmpty())
    }
}