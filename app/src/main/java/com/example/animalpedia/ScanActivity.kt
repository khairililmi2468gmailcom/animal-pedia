package com.example.animalpedia

import com.example.animalpedia.TFLiteHelper
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScanActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private val CAMERA_PERMISSION_CODE = 100

    private lateinit var imgScanImage: ImageView
    private lateinit var tfliteHelper: TFLiteHelper
    private var selectedBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgScanImage = findViewById(R.id.img_scanImage)
        tfliteHelper = TFLiteHelper(this)

        val btnCamera: Button = findViewById(R.id.btn_camera)
        btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        }

        val btnGallery: Button = findViewById(R.id.btn_gallery)
        btnGallery.setOnClickListener {
            openGallery()
        }

        val btnProcess: Button = findViewById(R.id.btn_process)
        btnProcess.setOnClickListener {
            selectedBitmap?.let {
                processImage(it)
            } ?: Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun openGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (pickPhotoIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to use the camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    if (imageBitmap != null) {
                        imgScanImage.setImageBitmap(imageBitmap)
                        selectedBitmap = imageBitmap
                    } else {
                        Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show()
                    }
                }
                REQUEST_IMAGE_PICK -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImageUri)
                        imgScanImage.setImageURI(selectedImageUri)
                        selectedBitmap = bitmap
                    } else {
                        Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun processImage(bitmap: Bitmap) {
        val (label, probability) = tfliteHelper.classifyImage(bitmap)
        Toast.makeText(this, "Prediction: $label, Confidence: ${"%.2f".format(probability * 100)}%", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        tfliteHelper.close()
    }
}
