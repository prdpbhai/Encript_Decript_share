package com.example.encript_decript

import FilesAdapter
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var selectFileButton: RelativeLayout
    private lateinit var selectedFileNameTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val encript=findViewById<Button>(R.id.encript)
//        val decript=findViewById<Button>(R.id.decript)

//        val decryptingImageView = findViewById<DecryptingImageView>(R.id.decryptingImageView)

        // Replace "encrypted_image.jpg" with the actual file name of your encrypted image



        val recyclerView: RecyclerView = findViewById(R.id.recycler)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val filesList = directory?.listFiles()?.toList() ?: emptyList()
        val adapter = FilesAdapter(this, filesList) // Pass 'this' as the context
        recyclerView.adapter = adapter



        selectFileButton = findViewById(R.id.selectFileButton)
        selectedFileNameTextView=findViewById(R.id.selectedFileNameTextView)
        selectFileButton.setOnClickListener {
            selectFile()
        }



        val securityManger = SecurityManager(this)
        encript.setOnClickListener {
            val selectedFileUri = selectedFileUri // Get the Uri of the selected file
            if (selectedFileUri != null) {
                securityManger.encryptFile(selectedFileUri, "encrypted_${filenam}")
                Toast.makeText(this, "Data Encrypted", Toast.LENGTH_SHORT).show()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please select a file first", Toast.LENGTH_SHORT).show()
            }
        }

//        encript.setOnClickListener {
////            securityManger.encryptFile(
////                "demo.txt",
////                "encryptedDemo.txt"
////            )
//
//            securityManger.encryptFile(
//                filenam,
//                "encrypted$filenam"
//            )
//
////            securityManger.encryptFile(
////                "Cap.png",
////                "encryptedCap.png"
////            )
////            securityManger.encryptFile(
////                "pdff.pdf",
////                "encryptedpdff.pdf"
////            )
////            securityManger.encryptFile(
////                "pro.jpeg",
////                "encryptepro.jpeg"
////            )
//            Toast.makeText(this,"Data Encripted", Toast.LENGTH_SHORT).show()
//        }

//        encript.setOnClickListener {
////            securityManger.encryptFile(
////                "demo.txt",
////                "encryptedDemo.txt"
////            )
//
//            securityManger.encryptFile(
//                "exel.xlsx",
//                "encryptedexel.xlsx"
//            )
//
////            securityManger.encryptFile(
////                "Cap.png",
////                "encryptedCap.png"
////            )
////            securityManger.encryptFile(
////                "pdff.pdf",
////                "encryptedpdff.pdf"
////            )
////            securityManger.encryptFile(
////                "pro.jpeg",
////                "encryptepro.jpeg"
////            )
//            Toast.makeText(this,"Data Encripted", Toast.LENGTH_SHORT).show()
//        }



//        decript.setOnClickListener {
//            try {
//                val decryptionFile = securityManger.decryptFile("encrypted_${filenam}","decripted_${filenam}")
//
//                Toast.makeText(this,"Decript Successful",Toast.LENGTH_SHORT).show()
//                if (decryptionFile != null) {
//
////                    val newFileName = "decryptedCapnew.png"
////                    val newFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), newFileName)
////                    val dd=File(decryptionFile)
////                    dd.copyTo(newFile, overwrite = true)
//
//                    Log.d("decryptionFileToString", decryptionFile.toString())
//                } else {
//                    // Error decrypting file, show a message
//                    Toast.makeText(this, "Error decrypting file", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: Exception) {
//                // Log any decryption errors
//                Log.e("DecryptionError", "Error decrypting file", e)
//                Toast.makeText(this, "Error decrypting data", Toast.LENGTH_SHORT).show()
//            }
//        }



    }


    private fun shareQRCode(qrCodeFile: File) {
        // Create a share intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
        }

        try {
            val contentUri = FileProvider.getUriForFile(this, "$packageName.provider", qrCodeFile)
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        // Start the share activity
        try {
            startActivity(Intent.createChooser(shareIntent, "Share QR Code"))
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            handleSelectedFile(uri)
            Log.d("vtytrctrv", data.toString())
            Log.d("tcevyuhuytfd", uri.toString())
        }
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        filePickerLauncher.launch(intent)
        Log.d("jehddsdfew", intent.toString())
    }

    private var selectedFileUri: Uri? = null
    var filenam : String=""

    private fun handleSelectedFile(uri: Uri?) {
        uri?.let { selectedUri ->
            contentResolver.query(selectedUri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                cursor.moveToFirst()
                val displayName = cursor.getString(nameIndex)
                selectedFileNameTextView.text = displayName
                filenam = displayName
                selectedFileUri = selectedUri // Store the selected file URI

            }
        }
    }
}





