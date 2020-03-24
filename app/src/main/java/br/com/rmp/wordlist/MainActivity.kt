package br.com.rmp.wordlist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.rmp.wordlist.adapter.WordListAdapter
import br.com.rmp.wordlist.entity.Word
import br.com.rmp.wordlist.viewmodel.WordViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var wordViewModel: WordViewModel
    private val newWordActivityRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = WordListAdapter(this)
        recyclerview.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        wordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        wordViewModel.wordsList.observe (this, Observer { words ->
            // Update the cached copy of the words in the adapter
            words?.let { adapter.setWords(it) }
        })

        fab.setOnClickListener {
            val intentNewWord = Intent(this, NewWordActivity::class.java)
            startActivityForResult(intentNewWord, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let {
                val word = Word(it)
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
