package me.xh.androidstorageadaptation.utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.xh.androidstorageadaptation.App
import me.xh.androidstorageadaptation.R
import me.xh.kit.toast.toast
import java.io.File

const val BITMAP_DIR = "bitmap"

suspend fun saveBitmap(bitmap: Bitmap, bitmapName: String) {
    withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            // 设置存储路径和图片名称
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.Images.ImageColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES.plus(File.separator).plus(BITMAP_DIR)
                )
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, bitmapName)
            } else {
                put(
                    MediaStore.Images.ImageColumns.DATA,
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                        .plus(File.separator).plus(BITMAP_DIR).plus(File.separator).plus(bitmapName)
                )
            }
            // 设置媒体类型
            put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/*")
            // 设置图片宽度
            put(MediaStore.Images.ImageColumns.WIDTH, bitmap.width)
            // 设置图片高度
            put(MediaStore.Images.ImageColumns.HEIGHT, bitmap.height)
        }

        val resolver = App.sInstance.contentResolver
        val bitmapUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        bitmapUri?.let { uri ->
            resolver.openOutputStream(uri).use { os ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            }
            toast(R.string.bitmap_save_success)
        } ?: run {
            toast(R.string.bitmap_save_failure)
        }
    }
}