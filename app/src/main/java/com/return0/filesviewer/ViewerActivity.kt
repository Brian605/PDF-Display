package com.return0.filesviewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.return0.filesviewer.databinding.FragmentViewerBinding
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import java.io.File

//This activity must implement the download listener
class ViewerActivity : AppCompatActivity(),DownloadFile.Listener {

    private lateinit var binding: FragmentViewerBinding
    lateinit var  remotePDFViewPager:RemotePDFViewPager
    lateinit var adapter:PDFPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val document=Static.document

     // Render the pdf
        if (document.documentName!!.endsWith("pdf") || document.documentName!!.endsWith("PDF")){
            remotePDFViewPager =
                RemotePDFViewPager(applicationContext, document!!.documentUrl, this)
        }else{
//launch the system viewer because handling word documents is a complex process and doing so will increase the app size to over 40 mbs
            val url =  "https://firebasestorage.googleapis.com/v0/b/admin-2d168.appspot.com/o/docs/${document.documentName!!}"
            val intent= Intent(Intent.ACTION_VIEW, Uri.fromFile(File(url)))
            startActivity(Intent.createChooser(intent,"Open with..."));


            //Alternatively, uncomment the lines below to use microsoft's online viewer but this will not work if the file is stored in cloud storage


          /*  val url =  "https://firebasestorage.googleapis.com/v0/b/admin-2d168.appspot.com/o/docs/${document.documentName!!}"
            val newUA = "Chrome/43.0.2357.65 "
            binding.webView.settings.javaScriptEnabled=true
            binding.webView.settings.userAgentString = newUA

            binding.webView.webViewClient=object :WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                  binding.progressBar.hide()

                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.progressBar.show()
                }
            }
            binding.webView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=$url")

      */  }
    }

    override fun onSuccess(url: String?, destinationPath: String?) {
adapter= PDFPagerAdapter(this,destinationPath!!)
        remotePDFViewPager.adapter=adapter
        setContentView(remotePDFViewPager)
    }

    override fun onFailure(e: Exception?) {
        e!!.printStackTrace()
    }

    override fun onProgressUpdate(progress: Int, total: Int) {

    }



}