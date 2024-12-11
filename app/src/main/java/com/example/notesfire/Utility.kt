package com.example.notesfire

import android.content.Context
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object Utility {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getCollectionReferenceForNotes(): CollectionReference {
        val currentUser = FirebaseAuth.getInstance().currentUser
            ?: throw IllegalStateException("Login First!")

        return FirebaseFirestore.getInstance()
            .collection("notes")
            .document(currentUser.uid)
            .collection("my_notes")
    }

    fun timeStampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(timestamp.toDate())
    }
}