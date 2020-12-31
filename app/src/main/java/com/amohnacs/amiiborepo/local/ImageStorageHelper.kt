package com.amohnacs.amiiborepo.local

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageStorageHelper @Inject constructor(
        private val context: Context
) {
    fun saveToInternalStorage(imageUrl: String, tail: String): String? {
        val contextWrapper = ContextWrapper(context)
        // path to /data/data/myapp/app_data/imageDir
        // the getDir method of ContextWrapper will automatically create the imageDir
        // directory if it does not already exist according to the docs.
        val directory: File = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        // Create specific image file
        val directoryPath = File(directory, "$tail.png")
        //streams allow us to write the bytes of our image
        var fileOuputStream: FileOutputStream? = null
        try {
            fileOuputStream = FileOutputStream(directoryPath)
            // when we finished loading our image asynchronously we store it in our file stystem
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                            // this is called when imageView is cleared from lifecycle
                        }
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            resource.compress(Bitmap.CompressFormat.PNG, 100, fileOuputStream)
                        }
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOuputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.absolutePath
    }

    fun loadImageFromStorage(path: String, tail: String): Bitmap? {
        return try {
            val file = File(path, "$tail.png")
            val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
            bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}