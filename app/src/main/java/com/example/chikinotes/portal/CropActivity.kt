package com.example.chikinotes.portal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity
import com.example.chikinotes.Util.showToast
import com.example.chikinotes.databinding.ActivityCropBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class CropActivity : CropImageActivity() {
    private lateinit var binding: ActivityCropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityCropBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.cropButton.setOnClickListener { cropImage() }
        binding.returnButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        setCropImageView(binding.cropImageView)
    }

    override fun setContentView(view: View) {
        super.setContentView(binding.root)
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val result = CropImage.ActivityResult(
            originalUri = binding.cropImageView.imageUri,
            uriContent = uri,
            error = error,
            cropPoints = binding.cropImageView.cropPoints,
            cropRect = binding.cropImageView.cropRect,
            rotation = binding.cropImageView.rotatedDegrees,
            wholeImageRect = binding.cropImageView.wholeImageRect,
            sampleSize = sampleSize,
        )
        if (result.isSuccessful) {
            var inputImage = InputImage.fromFilePath(this, result.uriContent!!)
            var recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(inputImage).addOnSuccessListener{
                this.showToast("OCR realizado con éxito")
                val resultIntent = Intent()
                resultIntent.putExtra("ocr", it.text)
                setResult(Activity.RESULT_OK, resultIntent)
                finish();

            }.addOnFailureListener {
                this.showToast("OCR no operacional")
            }
        } else {
            this.showToast(result.error.toString(), Toast.LENGTH_LONG)
        }
    }
}