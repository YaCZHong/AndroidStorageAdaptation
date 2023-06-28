package me.xh.fileprovider.utils

import me.xh.fileprovider.APK_PATH
import me.xh.fileprovider.App
import me.xh.fileprovider.IMAGES_PATH
import me.xh.fileprovider.INSTALL_APK_NAME
import me.xh.fileprovider.SHARE_PICTURE_NAME
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object AssetsUtils {

    fun copyFile() {
        copyPictureToAppExternalFileDir()
        copyApkToAppExternalFileDir()
    }

    private fun copyPictureToAppExternalFileDir() {
        val newPicturePath = App.sInstance.getExternalFilesDir(IMAGES_PATH)!!.absolutePath
            .plus(File.separator).plus(SHARE_PICTURE_NAME)
        copyAssetsFileToAppExternalFileDir(SHARE_PICTURE_NAME, newPicturePath)
    }

    private fun copyApkToAppExternalFileDir() {
        val newApkPath = App.sInstance.getExternalFilesDir(APK_PATH)!!.absolutePath
            .plus(File.separator).plus(INSTALL_APK_NAME)
        copyAssetsFileToAppExternalFileDir(INSTALL_APK_NAME, newApkPath)
    }

    private fun copyAssetsFileToAppExternalFileDir(assetsFileName: String, dstPath: String) {
        val dstFile = File(dstPath)
        if (dstFile.exists()) {
            return
        }
        var inputStream: InputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            inputStream = App.sInstance.assets.open(assetsFileName)
            fileOutputStream = FileOutputStream(dstFile)
            val buffer = ByteArray(1024)
            var count: Int
            while (inputStream.read(buffer).also { count = it } != -1) {
                fileOutputStream.write(buffer, 0, count)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
                fileOutputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}