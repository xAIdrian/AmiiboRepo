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
    fun saveToInternalStorage(bitmap: Bitmap, tail: String): String? {
        val contextWrapper = ContextWrapper(context)
        val directory: File = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        val directoryPath = File(directory, "$tail.png")
        var fileOuputStream: FileOutputStream? = null
        try {
            fileOuputStream = FileOutputStream(directoryPath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOuputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOuputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directoryPath.path
    }

    fun loadImageFromStorage(path: String): Bitmap? {
        return try {
            val file = File(path)
            val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
            bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}