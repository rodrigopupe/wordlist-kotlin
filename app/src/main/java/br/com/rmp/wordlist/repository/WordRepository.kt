package br.com.rmp.wordlist.repository

import androidx.lifecycle.LiveData
import br.com.rmp.wordlist.database.WordDao
import br.com.rmp.wordlist.entity.Word

// Declares a DAO as a private property in the constructor
// Pass in the DAO instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate Thread.
    // Observed LiveData will notify the observer when the data hsa changed.
    val allWords: LiveData<List<Word>> = wordDao.getWordsSortedAlphabetically()

    suspend fun insert(word: Word) {
        wordDao.insertWord(word)
    }
}