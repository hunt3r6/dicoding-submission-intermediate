package com.hariankoding.storyapp.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.hariankoding.storyapp.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

// Untuk kasus Intent Camera
fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

// Untuk kasus CameraX
fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}

fun uriToFile(selectImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun reduceFileImage(file: File): File {
    val bitmap = getRotate(file)
    var compressQuality = 100
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

private fun getRotate(image: File): Bitmap? {
    var bmp = BitmapFactory.decodeFile(image.path)
    try {
        val exif = ExifInterface(image.absolutePath)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        bmp = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bmp, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bmp, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bmp, 270)
            ExifInterface.ORIENTATION_NORMAL -> bmp
            else -> bmp
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return bmp
}

private fun rotateImage(imageToOrient: Bitmap, degreesToRotate: Int): Bitmap? {
    var result: Bitmap? = imageToOrient
    try {
        if (degreesToRotate != 0) {
            val matrix = Matrix()
            matrix.setRotate(degreesToRotate.toFloat())
            result = Bitmap.createBitmap(
                imageToOrient,
                0,
                0,
                imageToOrient.width,
                imageToOrient.height,
                matrix,
                true /*filter*/
            )
        }
    } catch (e: Exception) {
        if (Log.isLoggable(
                "TAG",
                Log.ERROR
            )
        ) {
            Log.e(
                "TAG",
                "Exception when trying to orient image",
                e
            )
        }
    }
    return result
}