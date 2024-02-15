package com.example.encript_decript

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.barteksc.pdfviewer.PDFView
import java.io.File
//import com.github.barteksc.pdfviewer.PDFView


class PdfOpen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_open)

//        val pdfView: PDFView = findViewById(R.id.pdfView)
//        val pdfFile: File = // get the PDF file

            // Load the PDF file into PDFView
//            pdfView.fromFile(pdfFile)
//                .enableSwipe(true) // allow horizontal swipe to change pages
//                .swipeHorizontal(true)
//                .load()

//        val pdfFilePath = intent.getStringExtra("pdfUrl")
//        Log.d("dhiuewdhfdsc",pdfFilePath.toString())
//        if (pdfFilePath != null) {
//            val pdfView: PDFView = findViewById(R.id.pdfView)
//            val file = File(pdfFilePath)
//            pdfView.fromFile(file)
//                .load()
//
//            Log.d("kfhgvdsuikhdfv",file.toString())
//
//        }

        val pdfFilePath = intent.getStringExtra("pdfUrl")
        if (pdfFilePath != null) {
            val pdfView: PDFView = findViewById(R.id.pdfView)
            val file = File(pdfFilePath)

            // Check if the file exists
            if (file.exists()) {
                try {
                    pdfView.fromFile(file).load()
                    Log.d("jyegwfdsgf",file.exists().toString())
                } catch (e: Exception) {
                    // Handle any exceptions that occur during loading
                    Log.e("PdfActivity", "Error loading PDF file: ${e.message}", e)
                }
            } else {
                // Handle the case where the file does not exist
                Log.e("PdfActivity", "PDF file does not exist: $pdfFilePath")
            }
        } else {
            // Handle the case where the intent extra is null
            Log.e("PdfActivity", "PDF file path is null")
        }

//        val pdfFilePath = intent.getStringExtra("pdfUrl")
//        if (pdfFilePath != null) {
//            val webView: WebView = findViewById(R.id.pdfWebView)
//            webView.settings.javaScriptEnabled = true
//            webView.webViewClient = WebViewClient()
//            webView.loadUrl("file:///$pdfFilePath")
//        }
    }
}