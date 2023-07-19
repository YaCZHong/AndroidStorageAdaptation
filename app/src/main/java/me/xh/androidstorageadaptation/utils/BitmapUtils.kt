package me.xh.androidstorageadaptation.utils

import android.Manifest
import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import me.xh.androidstorageadaptation.App
import me.xh.androidstorageadaptation.R
import me.xh.kit.toast.toast
import java.io.File

/**
 * 下面两个方法最好放到后台线程当中执行，这里为了演示方便，就直接在主线程执行了
 *
 * 保存图片到本地，在Android Q以上不需要权限
 */

const val BITMAP_DIR = "bitmap"
fun saveBitmapInQ(bitmap: Bitmap, bitmapName: String) {
    val cv = ContentValues().apply {
        // 设置存储路径
        put(
            MediaStore.Images.ImageColumns.RELATIVE_PATH,
            Environment.DIRECTORY_PICTURES.plus(File.separator).plus(BITMAP_DIR)
        )
        // 设置图片名称
        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, bitmapName)
        // 设置媒体类型
        put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/*")
        // 设置图片宽度
        put(MediaStore.Images.ImageColumns.WIDTH, bitmap.width)
        // 设置图片高度
        put(MediaStore.Images.ImageColumns.HEIGHT, bitmap.height)
        // 设置独占访问权限
        put(MediaStore.Images.ImageColumns.IS_PENDING, 1)
    }

    val resolver = App.sInstance.contentResolver
    val bitmapUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
    bitmapUri?.let { uri ->
        resolver.openOutputStream(uri).use { os ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        }
        cv.clear()
        cv.put(MediaStore.Images.ImageColumns.IS_PENDING, 0)
        resolver.update(uri, cv, null, null)
        toast(R.string.bitmap_save_success)
    } ?: run {
        toast(R.string.bitmap_save_failure)
    }
}

@RequiresPermission(
    allOf = [
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ]
)
fun saveBitmapBeforeQ(bitmap: Bitmap, bitmapName: String) {
    val cv = ContentValues().apply {
        // 设置存储路径和图片名称
        put(
            MediaStore.Images.ImageColumns.DATA,
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                .plus(File.separator).plus(BITMAP_DIR).plus(File.separator).plus(bitmapName)
        )
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