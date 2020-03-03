package com.nf98.moviecatalogue.helper

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference

class ImageDownloader(context: Context, private val child: String, private val name: String) : AsyncTask<String, Unit, Unit>() {

    var path = ""
    private var mContext: WeakReference<Context> = WeakReference(context)

    override fun doInBackground(vararg params: String?) {
        val url = params[0]
        val requestOptions = RequestOptions()
            .downsample(DownsampleStrategy.CENTER_INSIDE)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        mContext.get()?.let {
            val bitmap = Glide.with(it)
                .asBitmap()
                .apply(requestOptions)
                .load(url)
                .submit()
                .get()

            try {
                var file = File(it.filesDir, child)
                if (!file.exists()) file.mkdir()
                file = File(file, "$name.jpg")
                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                out.flush()
                out.close()
                path = file.absolutePath
                Log.i("MovieDB", "Image saved.")
            } catch (e: Exception) {
                Log.i("MovieDB", "Failed to save image.")
            }
        }
    }
}