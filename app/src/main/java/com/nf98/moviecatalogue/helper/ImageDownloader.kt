package com.nf98.moviecatalogue.helper

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.ref.WeakReference

class ImageDownloader(val context: Context, private val child: String, private val name: String) {

    private var file: File? = null

    fun downloadImage(url: String){
        val bitmap = Glide.with(context)
            .asBitmap()
            .load(url)
            .submit()
            .get()

        val wrapper = ContextWrapper(context)
        file = wrapper.getDir(child, Context.MODE_PRIVATE)
        file = File(file, "$name.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            Log.i("MovieDB", "Image saved.")
        } catch (e: Exception) {
            Log.i("MovieDB", "Failed to save image.")
        }
    }

    fun getPath(): String? = file?.absolutePath
}