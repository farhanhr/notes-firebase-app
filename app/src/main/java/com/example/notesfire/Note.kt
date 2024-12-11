package com.example.notesfire

import com.google.firebase.Timestamp

data class Note(
    val title: String = "",
    val content: String = "",
    val timeStamp: Timestamp = Timestamp.now()
) {
    constructor() : this("", "", Timestamp.now())
}
