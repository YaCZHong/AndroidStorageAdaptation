package me.xh.androidstorageadaptation.utils

import android.Manifest
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import me.xh.androidstorageadaptation.App

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(Manifest.permission.READ_MEDIA_IMAGES)
fun queryAllImagesIn13() {
    queryAllImages()
}

@RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
fun queryAllImagesBefore13() {
    queryAllImages()
}

private fun queryAllImages() {
    val resolver = App.sInstance.contentResolver
    resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        ?.use { cursor ->
            while (cursor.moveToNext()) {
                val displayNameIndex =
                    cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
                val imageWidthIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)
                val imageHeightIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)
                val imageSizeIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)
                val displayName = cursor.getString(displayNameIndex)
                val imageWidth = cursor.getString(imageWidthIndex)
                val imageHeight = cursor.getString(imageHeightIndex)
                val imageSize = cursor.getString(imageSizeIndex)
                Log.d(
                    "queryAllImages",
                    "名称：$displayName, 宽高：$imageWidth * $imageHeight, 大小：$imageSize"
                )
            }
        }
}