package br.com.rmp.wordlist.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import br.com.rmp.wordlist.database.WordRoomDatabase
import br.com.rmp.wordlist.entity.Word
import br.com.rmp.wordlist.repository.WordRepository
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter
class WordViewModel(application: Application) : AndroidViewModel(application) {

    // Tue ViewModel maintains a reference to the repository to get data.
    private val repository: WordRepository

    // LiveData gives us updated words when they change.
    val wordsList: LiveData<List<Word>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct the correct WordRepository
        val wordDao = WordRoomDatabase.getDatabase(application).wordDao()
        repository = WordRepository(wordDao)
        wordsList = repository.allWords
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on the main Thread, blocking the UI,
     * so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called viewModelScope
     * which we can use here.
     * */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}
