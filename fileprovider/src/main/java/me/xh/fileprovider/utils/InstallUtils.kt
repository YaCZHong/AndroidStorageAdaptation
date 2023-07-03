package me.xh.fileprovider.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import me.xh.fileprovider.BuildConfig
import me.xh.fileprovider.R
import me.xh.kit.toast.toast
import java.io.File

fun installInN(context: Context, apkFile: File) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        val installApkUri = FileProvider.getUriForFile(
            context, "${BuildConfig.APPLICATION_ID}.fileProvider", apkFile
        )
        setDataAndType(installApkUri, "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(R.string.relevance_application_no_found)
    } catch (e: Exception) {
        toast(R.string.install_apk_failure)
    }
}

fun installBeforeN(context: Context, apkFile: File) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        toast(R.string.relevance_application_no_found)
    } catch (e: Exception) {
        toast(R.string.install_apk_failure)
    }
}