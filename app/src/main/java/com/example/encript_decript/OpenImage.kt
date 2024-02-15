package com.example.encript_decript

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide


class OpenImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_image)

//        val decryptingImageView = findViewById<DecryptingImageView>(R.id.decryptingImageView)

        // Check if decryptingImageView is not null before calling decryptAndDisplayImage
//        decryptingImageView?.decryptAndDisplayImage("encrypted_image.jpg")

        val imagePath = intent.getStringExtra("imagePath") // Get the file path of the image
        val imageView = findViewById<ImageView>(R.id.imageView)

        // Load the image into the ImageView using Glide or any other image loading library
        Glide.with(this)
            .load(imagePath)
            .into(imageView)
    }
}