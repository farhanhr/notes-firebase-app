package com.example.notesfire

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notesfire.databinding.ActivityNoteBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private var isEdit = false
    private lateinit var docId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbarAddNote
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24)

        val title = intent.getStringExtra("TITLE_EXTRA") ?: ""
        val content = intent.getStringExtra("CONTENT_EXTRA") ?: ""
        docId = intent.getStringExtra("DOCID_EXTRA") ?: ""

        if (docId.isNotEmpty()) {
            isEdit = true
        }

        if (isEdit) {
            binding.toolbarAddNote.title = "Edit Note"
            binding.edtTitle.setText(title)
            binding.edtContent.setText(content)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.save_note -> {
                saveNote()
            }
            R.id.delete_note -> {
                deleteNote()
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)

        val deleteNoteMenu: MenuItem? = menu?.findItem(R.id.delete_note)
        deleteNoteMenu?.isVisible = isEdit

        return true
    }

    private fun saveNote() {
        val titleText = binding.edtTitle.text.toString()
        val contentText = binding.edtContent.text.toString()

        if (titleText.isNotEmpty()) {

            val note = Note(
                title = titleText,
                content = contentText,
                timeStamp = Timestamp.now()
            )
            saveNoteToFirebase(note)

        } else {
            binding.edtTitle.error = "Title is required"
        }

    }

    private fun saveNoteToFirebase(note: Note) {
        val documentReference: DocumentReference =  if (isEdit) {
            Utility.getCollectionReferenceForNotes().document(docId)
        }
        else {
           Utility.getCollectionReferenceForNotes().document()
        }

        documentReference.set(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Utility.showToast(baseContext, "Note Saved")
                finish()
            } else {
                Utility.showToast(baseContext, "Failed to save note")
            }
        }
    }

    private fun deleteNote() {
        val documentReference: DocumentReference = Utility.getCollectionReferenceForNotes().document(docId)

        documentReference.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Utility.showToast(baseContext, "Note deleted")
                finish()
            } else {
                Utility.showToast(baseContext, "Failed to delete note")
            }
        }
    }

}