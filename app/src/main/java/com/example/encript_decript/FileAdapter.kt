import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.encript_decript.BuildConfig
import com.example.encript_decript.DecryptingImageView
import com.example.encript_decript.OpenImage
import com.example.encript_decript.PdfOpen
import com.example.encript_decript.R
import kotlinx.coroutines.CoroutineStart
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

import kotlin.io.encoding.ExperimentalEncodingApi

class FilesAdapter(private val context: Context, private val filesList: List<File>) : RecyclerView.Adapter<FilesAdapter.FileViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = filesList[position]
        holder.bind(file)
        holder.rrelative.setOnClickListener{


//            val context = context
            val fileName = file.name
            val ext=file.extension
            Log.d("jfhgfdkjf",ext)
            val newFileName = "decrypted_$fileName"
            Log.d("hgygjfuygh", fileName)
            Log.d("hgfjkhhj",newFileName)
//            holder.decryptingImageView?.decryptAndDisplayImage(fileName)
//            Log.d("hgttfghghfxgfh",holder.decryptingImageView.toString())

            try {
                val encryptedFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
                val fis = FileInputStream(encryptedFile)
                val fileType = getFileType(fileName)

                if (file.extension.equals("jpg")){
                    decryptAndOpenImage(fis,fileType)
                }else if (file.extension.equals("pdf")) {
                    decryptAndOpenFilePdf(fis,fileType)
                }
                else{
                    decryptAndOpenFilePdfImage(fis,fileType)
                }

//
            } catch (e: Exception) {
                Log.e("DecryptionError", "Error opening encrypted file", e)
                Toast.makeText(context, "Error opening encrypted file", Toast.LENGTH_SHORT).show()
            }



        }
    }

    override fun getItemCount(): Int {
        return filesList.size
    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fileTextView: TextView = itemView.findViewById(R.id.txt)
        val rrelative: RelativeLayout=itemView.findViewById(R.id.rrelative)
        val decryptingImageView = itemView.findViewById<DecryptingImageView>(R.id.decryptingImageView)

        fun bind(file: File) {
            fileTextView.text = file.name
        }
    }


    private fun getMimeType(file: File): String {
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
    }

    private val secretKeySpec = generateKey()

    private fun generateKey(): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = BuildConfig.KEYPASSWORD.toByteArray()
        digest.update(bytes, 0, bytes.size)
        return SecretKeySpec(digest.digest(), BuildConfig.ALGORITHM)
    }


    fun decryptAndOpenImage(encryptedInputStream: InputStream, fileType: String) {
        try {
            val decryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray()))
            }

            val inputStream = CipherInputStream(encryptedInputStream, decryptCipher)

            // Read decrypted data into a byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            inputStream.close()
            byteArrayOutputStream.close()

            val decryptedData = byteArrayOutputStream.toByteArray()

            // Save the decrypted data to a temporary file
            // Save the decrypted data to a temporary file
            val tempFile = File.createTempFile("decrypted_file", getFileExtension(fileType), context.cacheDir)
            val fos = FileOutputStream(tempFile)
            fos.write(decryptedData)
            fos.close()

// Get the content URI for the temporary file using FileProvider
            val tempFileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

// Open the temporary file using an Intent
            val intent = Intent(context, OpenImage::class.java).apply {
                putExtra("imagePath", tempFile.absolutePath) // Pass the file path to the activity
            }
            context.startActivity(intent)
//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setDataAndType(tempFileUri, fileType) // Change the MIME type according to your file type
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            context.startActivity(intent)

        } catch (e: Exception) {
            Log.e("DecryptionError", "Error decrypting and opening file", e)
            Toast.makeText(context, "Error decrypting and opening file", Toast.LENGTH_SHORT).show()
        }
    }

    fun decryptAndOpenFilePdfImage(encryptedInputStream: InputStream, fileType: String) {
        try {
            val decryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray()))
            }

            val inputStream = CipherInputStream(encryptedInputStream, decryptCipher)

            // Read decrypted data into a byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            inputStream.close()
            byteArrayOutputStream.close()

            val decryptedData = byteArrayOutputStream.toByteArray()

            // Save the decrypted data to a temporary file
            // Save the decrypted data to a temporary file
            val tempFile = File.createTempFile("decrypted_file", getFileExtension(fileType), context.cacheDir)
            val fos = FileOutputStream(tempFile)
            fos.write(decryptedData)
            fos.close()

// Get the content URI for the temporary file using FileProvider
            val tempFileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)

// Open the temporary file using an Intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(tempFileUri, fileType) // Change the MIME type according to your file type
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)

        } catch (e: Exception) {
            Log.e("DecryptionError", "Error decrypting and opening file", e)
            Toast.makeText(context, "Error decrypting and opening file", Toast.LENGTH_SHORT).show()
        }
    }
    fun decryptAndOpenFilePdf(encryptedInputStream: InputStream, fileType: String) {
        try {
            val decryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray()))
            }

            val inputStream = CipherInputStream(encryptedInputStream, decryptCipher)

            // Read decrypted data into a byte array
            val byteArrayOutputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
            }

            inputStream.close()
            byteArrayOutputStream.close()

            val decryptedData = byteArrayOutputStream.toByteArray()

            // Save the decrypted data to a temporary file
            // Save the decrypted data to a temporary file
            val tempFile = File.createTempFile("decrypted_file", getFileExtension(fileType), context.cacheDir)
            val fos = FileOutputStream(tempFile)
            fos.write(decryptedData)
            fos.close()

// Get the content URI for the temporary file using FileProvider
            val tempFileUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
            val intent = Intent(context, PdfOpen::class.java).apply {
                data = tempFileUri
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra("pdfUrl", tempFile.absolutePath)
            }
            context.startActivity(intent)

// Open the temporary file using an Intent
//            val fileUri: Uri =
//                PdfUtils.openPdf(context, fileUri)fileUri


//            val intent = Intent(Intent.ACTION_VIEW)
//            intent.setDataAndType(tempFileUri, fileType) // Change the MIME type according to your file type
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//            context.startActivity(intent)

        } catch (e: Exception) {
            Log.e("DecryptionError", "Error decrypting and opening file", e)
            Toast.makeText(context, "Error decrypting and opening file", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getFileType(fileName: String): String {
        val extension = fileName.substringAfterLast('.')
        return when (extension.toLowerCase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "pdf" -> "application/pdf"
            "xls", "xlsx" -> "application/vnd.ms-excel"
            "doc", "docx" -> "application/msword"
            else -> "application/octet-stream" // Default to binary data if unknown extension
        }
    }

    private fun getFileExtension(fileType: String): String {
        return when (fileType) {
            "image/jpeg" -> ".jpg"
            "image/png" -> ".png"
            "application/pdf" -> ".pdf"
            "application/vnd.ms-excel" -> ".xls"
            "application/msword" -> ".doc"
            else -> ""
        }
    }

    private fun openImage(context: Context, file: File) {
        val intent = Intent(context, OpenImage::class.java).apply {
            putExtra("imagePath", file.absolutePath) // Pass the file path to the activity
        }
        context.startActivity(intent)
    }

    object PdfUtils {

        fun openPdf(context: Context, fileUri: Uri) {
            // Create an intent with ACTION_VIEW to open the PDF file
            val intent = Intent(Intent.ACTION_VIEW).apply {
                // Set the data and type of the intent to open PDF
                setDataAndType(fileUri, "application/pdf")

                // Set flags to grant read permission to other apps
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            // Check if there's an application available to handle the intent
            if (intent.resolveActivity(context.packageManager) != null) {
                // Start the activity to open the PDF file
                context.startActivity(intent)
            } else {
                // No application available to handle the intent
                // You can display a toast or show a dialog to inform the user
                // For example: Toast.makeText(context, "No PDF viewer application found", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


