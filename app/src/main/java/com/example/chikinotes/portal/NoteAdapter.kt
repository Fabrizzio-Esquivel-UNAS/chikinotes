package com.example.chikinotes.portal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chikinotes.R
import com.example.chikinotes.portal.NoteAdapter.NoteViewHolder
import com.example.chikinotes.Util.timestampToString
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class NoteAdapter(options: FirestoreRecyclerOptions<Nota>, var context: Context) :
    FirestoreRecyclerAdapter<Nota, NoteViewHolder>(options) {
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, nota: Nota) {
        holder.titleTextView.text = nota.titulo
        holder.contentTextView.text = nota.contenido
        holder.timestampTextView.text = nota.timestamp?.timestampToString()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NoteDetailsActivity::class.java)
            intent.putExtra("titulo", nota.titulo)
            intent.putExtra("contenido", nota.contenido)
            val docId = this.snapshots.getSnapshot(position).id
            intent.putExtra("docId", docId)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_note_item, parent, false)
        return NoteViewHolder(view)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var contentTextView: TextView
        var timestampTextView: TextView

        init {
            titleTextView = itemView.findViewById(R.id.note_title_text_view)
            contentTextView = itemView.findViewById(R.id.note_content_text_view)
            timestampTextView = itemView.findViewById(R.id.note_timestamp_text_view)
        }
    }
}