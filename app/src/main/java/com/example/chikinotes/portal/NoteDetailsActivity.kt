package com.example.chikinotes.portal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chikinotes.Util.collectionReferenceForNotes
import com.example.chikinotes.Util.showToast
import com.example.chikinotes.databinding.ActivityNoteDetailsBinding
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference


class NoteDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNoteDetailsBinding
    private lateinit var ocr : ActivityResultLauncher<Intent>

    private var docId = "" //Se queda vacio si no se está editando
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        //Recibir datos
        intent.getStringExtra("docId")?.let {
            docId = it
            binding.notesTitleText.setText(intent.getStringExtra("titulo"))
            binding.notesContentText.setText(intent.getStringExtra("contenido"))
            binding.pageTitle.text = "Editar apunte"
            binding.deleteNoteTextViewBtn.visibility = View.VISIBLE
        }
        ocr = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK)
                binding.notesContentText.setText(result.data?.getStringExtra("ocr"))
        }
        //Crear conexiones
        binding.saveNoteBtn.setOnClickListener(View.OnClickListener { saveNote() })
        binding.deleteNoteTextViewBtn.setOnClickListener(View.OnClickListener { deleteNoteFromFirebase() })
        binding.ocrButton.setOnClickListener{
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            ocr.launch(Intent(this, CropActivity::class.java))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    //Comprobar cada vez que se acepta o rechaza una solicitud de servicio
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ocr.launch(Intent(this, CropActivity::class.java))
            } else {
                this.showToast(
                    "Se necesita de la camara para hacer uso de esta función",
                    Toast.LENGTH_LONG
                )
            }
        }
    }

    private fun saveNote() {
        val noteTitle = binding.notesTitleText.text.toString()
        val noteContent = binding.notesContentText.text.toString()
        if (noteTitle.isEmpty()) {
            binding.notesTitleText.error = "Título requerido"
            return
        }
        val nota = Nota()
        nota.titulo = noteTitle
        nota.contenido = noteContent
        nota.timestamp = Timestamp.now()
        saveNoteToFirebase(nota)
    }

    private fun saveNoteToFirebase(nota: Nota?) {
        val docRef : DocumentReference = if (docId=="") collectionReferenceForNotes.document()
                    else collectionReferenceForNotes.document(docId)
        docRef.set(nota!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                this.showToast("Apunte añadido con éxito")
                finish()
            } else {
                this.showToast("Fallo al añadir apunte")
            }
        }
    }

    private fun deleteNoteFromFirebase() {
        var doc = if (docId=="") collectionReferenceForNotes.document()
                    else collectionReferenceForNotes.document(docId)
        doc.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                this.showToast("Apunte eliminado con éxito")
                finish()
            } else {
                this.showToast("Error al borrar apunte")
            }
        }
    }
}