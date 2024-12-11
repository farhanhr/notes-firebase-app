package com.example.notesfire

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesfire.databinding.ItemNoteBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NoteAdapter(options: FirestoreRecyclerOptions<Note>) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder>(options) {
    class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteContent.text = note.content
            binding.tvNoteDate.text = Utility.timeStampToString(note.timeStamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.bind(model)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val adapterPosition = holder.bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                val docId = snapshots.getSnapshot(position).id
                val intent = Intent(context, NoteActivity::class.java)
                intent.putExtra("TITLE_EXTRA", model.title)
                intent.putExtra("CONTENT_EXTRA", model.content)
                intent.putExtra("DOCID_EXTRA", docId)

                context.startActivity(intent)
            }
        }
    }
}