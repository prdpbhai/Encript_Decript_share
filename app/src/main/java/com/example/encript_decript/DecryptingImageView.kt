package com.example.encript_decript

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.Toast
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class DecryptingImageView : ImageView {

    constructor(context: Context) : super(context) {
        // Initialize the view
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        // Initialize the view
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        // Initialize the view
        init(context)
    }

    private fun init(context: Context) {
        // Add any initialization code here
    }

    fun decryptAndDisplayImage(fileName: String) {
        try {
            val encryptedFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
            val fis = FileInputStream(encryptedFile)
            val decryptedBitmap = decryptAndReadImage(fis)

            // Display the decrypted image in the ImageView
            if (decryptedBitmap != null) {
                setImageBitmap(decryptedBitmap)
            } else {
                // Show error message if decryption failed
                Toast.makeText(context, "Error decrypting image", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Show error message if file cannot be opened
            Toast.makeText(context, "Error opening encrypted file", Toast.LENGTH_SHORT).show()
        }
    }
    private val secretKeySpec = generateKey()

    private fun generateKey(): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = BuildConfig.KEYPASSWORD.toByteArray()
        digest.update(bytes, 0, bytes.size)
        return SecretKeySpec(digest.digest(), BuildConfig.ALGORITHM)
    }

    private fun decryptAndReadImage(encryptedInputStream: InputStream): Bitmap? {
        return try {
            val decryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray()))
            }

            val inputStream = CipherInputStream(encryptedInputStream, decryptCipher)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}
