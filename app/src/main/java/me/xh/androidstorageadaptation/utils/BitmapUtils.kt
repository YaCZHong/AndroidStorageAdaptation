package me.xh.androidstorageadaptation.utils

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import me.xh.androidstorageadaptation.App
import me.xh.androidstorageadaptation.R
import java.io.File

const val BITMAP_UTILS_TAG = "BitmapUtils"
const val BITMAP_DIR = "bitmap"

fun saveBitmap(bitmap: Bitmap, bitmapName: String) {
    val resolver = App.sInstance.contentResolver
    val cv = ContentValues().apply {
        put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/*")
        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, bitmapName)
        put(MediaStore.Images.ImageColumns.WIDTH, bitmap.width)
        put(MediaStore.Images.ImageColumns.HEIGHT, bitmap.height)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(
                MediaStore.Images.ImageColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES.plus(File.separator).plus(BITMAP_DIR)
            )
        }

        // MediaStore.Images.ImageColumns.RELATIVE_PATH 在 Android Q 及以上才能使用
        // 如果低于 Android Q，又想划分子目录，可以使用 MediaStore.Images.ImageColumns.DATA
        // 示例代码：
        // put(
        //     MediaStore.Images.ImageColumns.DATA,
        //     Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
        //         .plus(File.separator).plus(BITMAP_DIR).plus(File.separator).plus(bitmapName)
        // )
        // 缺点就是需要获取读写存储权限-原因：Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)


    }
    val bitmapUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)

    bitmapUri?.let { uri ->
        resolver.openOutputStream(uri).use { os ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            cv.clear()
            cv.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, bitmapName)
            resolver.update(uri, cv, null, null)
        }


    } ?: run {

    }
}