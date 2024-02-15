package com.example.encript_decript

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class SecurityManager(private val context: Context) {
//    companion object{
//        private const val KEYPASSWORD="CodingMeetDev123"
//        private const val IVPASSWORD="DevCoding123Meet"
//        private const val ALGORITHEM="AES"
//        private const val TRANSFORMATION="AES/CBC/PCS7Padding"
//    }





    private val secretKeySpec = generateKey()

    private fun generateKey(): SecretKeySpec {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = BuildConfig.KEYPASSWORD.toByteArray()
        digest.update(bytes, 0, bytes.size)
        return SecretKeySpec(digest.digest(), BuildConfig.ALGORITHM)
    }
    fun encryptFile(uri: Uri, encryptFileName: String) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val encryptFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), encryptFileName)
            if (encryptFile.exists()) {
                encryptFile.delete()
            }
            encryptFile.createNewFile()

            val encryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
                init(
                    Cipher.ENCRYPT_MODE,
                    secretKeySpec,
                    IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray())
                )
            }
            val fos = FileOutputStream(encryptFile)
            val cos = CipherOutputStream(fos, encryptCipher)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            if (inputStream != null) {
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    cos.write(buffer, 0, bytesRead)
                }
            }

            cos.flush()
            cos.close()
            if (inputStream != null) {
                inputStream.close()
            }

            Log.d("status", "Encryption File Successfully")
        } catch (e: Exception) {
            Log.e("EncryptionError", "Error encrypting file", e)
            Toast.makeText(context, "Error encrypting data", Toast.LENGTH_SHORT).show()
        }
    }

//    fun encryptFile(fileName: String, encryptFileName: String) {
//        try {
//            val inputStream = context.assets.open(fileName)
//            val encryptFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), encryptFileName)
//            if (encryptFile.exists()) {
//                encryptFile.delete()
//            }
//            encryptFile.createNewFile()
//
//            val encryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
//                init(
//                    Cipher.ENCRYPT_MODE,
//                    secretKeySpec,
//                    IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray())
//                )
//            }
//            val fos = FileOutputStream(encryptFile)
//            val cos = CipherOutputStream(fos, encryptCipher)
//
//            val buffer = ByteArray(1024)
//            var bytesRead: Int
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                cos.write(buffer, 0, bytesRead)
//            }
//
//            cos.flush()
//            cos.close()
//            inputStream.close()
//
//            Log.d("status", "Encryption File Successfully")
//        } catch (e: Exception) {
//            Log.e("EncryptionError", "Error encrypting file", e)
//            Toast.makeText(context, "Error encrypting data", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun decryptFile(encryptFileName: String, newFileName: String): String? {
        return try {
            val encryptedFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), encryptFileName)
            val fis = FileInputStream(encryptedFile)

            val decryptCipher = Cipher.getInstance(BuildConfig.TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(BuildConfig.IVPASSWORD.toByteArray()))
            }

            val inputStream = CipherInputStream(fis, decryptCipher)
            val outputStream = FileOutputStream(File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), newFileName))

            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            Log.d("status", "Decryption File Successfully")
            "File decrypted successfully"
        } catch (e: Exception) {
            Log.e("DecryptionError", "Error decrypting file", e)
            null
        }
    }

//    fun decryptFile(encryptFileName: String, newFileName: String): String? {
////        val secretKeySpec = SecretKeySpec("YourSecretKey".toByteArray(), "AES") // Replace with your actual secret key
//
//        return try {
//            val encryptedFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), encryptFileName)
//            val fis = FileInputStream(encryptedFile)
//
//            val decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding").apply {
//                init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec("DevCoding123Meet".toByteArray())) // Replace with your actual IV password
//            }
//
//            val inputStream = CipherInputStream(fis, decryptCipher)
//            val outputStream = ByteArrayOutputStream()
//
//            val buffer = ByteArray(1024)
//            var bytesRead: Int
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                outputStream.write(buffer, 0, bytesRead)
//            }
//
//            val decryptedData = outputStream.toByteArray()
//
//            val newFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), newFileName)
//            FileOutputStream(newFile).use { it.write(decryptedData) }
//
//            Log.d("status", "Decryption File Successfully")
//
//            newFile.absolutePath
//        } catch (e: Exception) {
//            Log.e("DecryptionError", "Error decrypting file", e)
//            null
//        }
//    }





    private fun Context.loadFromAsset(fileName: String):String{
        val inputStream = assets.open(fileName)
        val size = inputStream.available()
        val byteArray = ByteArray(size)
        inputStream.read(byteArray)
        inputStream.close()
        return String(byteArray,Charsets.UTF_8)
    }

}