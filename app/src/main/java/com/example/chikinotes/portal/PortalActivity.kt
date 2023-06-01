package com.example.chikinotes.portal

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chikinotes.Util
import com.example.chikinotes.databinding.ActivityPortalBinding
import com.example.chikinotes.login.LoginActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query

class PortalActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPortalBinding
    private lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPortalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.addNoteBtn.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(this, NoteDetailsActivity::class.java)
            )
        })
        binding.menuBtn.setOnClickListener(View.OnClickListener {
            mostrarMenu()
        })
        setupRecyclerView()
    }

    private fun mostrarMenu() {
        val popupMenu = PopupMenu(this, binding.menuBtn)
        popupMenu.menu.add("Cerrar sesión")
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
            if (menuItem.title === "Cerrar sesión") {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(this, LoginActivity::class.java)
                )
                finish()
                return@OnMenuItemClickListener true
            }
            false
        })
    }

    private fun setupRecyclerView() {
        val query: Query = Util.collectionReferenceForNotes
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val options: FirestoreRecyclerOptions<Nota> = FirestoreRecyclerOptions.Builder<Nota>()
            .setQuery(query, Nota::class.java).build()
        binding.recylerView.layoutManager = LinearLayoutManager(this)
        noteAdapter = NoteAdapter(options, this)
        binding.recylerView.adapter = noteAdapter
    }

    override fun onStart() {
        super.onStart()
        noteAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        noteAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        noteAdapter.notifyDataSetChanged()
    }
}