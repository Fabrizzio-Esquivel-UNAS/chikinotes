package com.example.chikinotes

import android.app.Activity
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

//ESTE OBJETO CONTIENE FUNCIONES USABLES EN TODO EL PROYECTO
object Util {
    @JvmStatic
    val collectionReferenceForNotes: CollectionReference get() {
            val currentUser = FirebaseAuth.getInstance().currentUser
            return FirebaseFirestore.getInstance().collection("apuntes")
                .document(currentUser!!.uid).collection("mis_apuntes")
        }


    //A PARTIR DE AQUI SON METODOS AÑADIDOS A CLASES DE KOTLIN

    fun Timestamp.timestampToString(): String {
        return SimpleDateFormat("MM/dd/yyyy").format(this.toDate())
    }

    fun Activity.showToast(msg: String?, duracion: Int=Toast.LENGTH_SHORT){
        Toast.makeText(this, msg, duracion).show()
    }

    //ESTE METODO HACE PARTES DEL TEXTO DE UN TEXTVIEW CLICKABLES
    fun TextView.makeLink(evento: View.OnClickListener, texto: String = this.text.toString()) {
        val spannableString = SpannableString(this.text)
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                textPaint.color = textPaint.linkColor
                textPaint.isUnderlineText = true
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as TextView).text as Spannable, 0)
                view.invalidate()
                evento.onClick(view)
            }
        }
        val index = this.text.toString().indexOf(texto, -1)
        spannableString.setSpan(
            clickableSpan, index, index+texto.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }
}