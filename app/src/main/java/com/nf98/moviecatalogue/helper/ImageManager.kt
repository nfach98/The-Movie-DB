package com.nf98.moviecatalogue.helper

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageManager(val context: Context, private val child: String, private val name: String) {

    fun downloadImage(url: String){
        GlobalScope.launch(Dispatchers.IO) {
            val bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()

            val wrapper = ContextWrapper(context)
            var file = wrapper.getDir(child, Context.MODE_PRIVATE)
            file = File(file, "$name.jpg")

            try {
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val stream: OutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
            } catch (e: Exception) {
                Log.i("MovieDB", "Failed to save image.")
            }
        }
    }

    fun deleteImage(){
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir(child, Context.MODE_PRIVATE)
        file = File(file, "$name.jpg")
        file.delete()
    }
}