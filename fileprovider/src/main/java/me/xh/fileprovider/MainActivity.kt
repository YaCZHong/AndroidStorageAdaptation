package me.xh.fileprovider

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import me.xh.fileprovider.databinding.ActivityMainBinding
import me.xh.fileprovider.utils.string
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.apply {
            btnSharePicture.setOnClickListener {
                sharePicture()
            }
            btnInstallApk.setOnClickListener {
                installApk()
            }
        }
    }

    private fun sharePicture() {
        val sharePicturePath = App.sInstance.getExternalFilesDir(IMAGES_PATH)!!.absolutePath
            .plus(File.separator).plus(SHARE_PICTURE_NAME)

        val sharePictureFile = File(sharePicturePath)
        if (!sharePictureFile.exists()) {
            Toast.makeText(
                this, string(R.string.share_picture_file_not_found), Toast.LENGTH_SHORT
            ).show()
            return
        }

        val sharePictureUri =
            FileProvider.getUriForFile(
                this, "${BuildConfig.APPLICATION_ID}.fileProvider", sharePictureFile
            )
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_STREAM, sharePictureUri)
            putExtra(Intent.EXTRA_TEXT, string(R.string.share_picture_content))
            type = "image/*"
        }

        // 这里不需要设置 Intent.FLAG_GRANT_READ_URI_PERMISSION 是因为当我们 startActivity 后，会辗转调用 Instrumentation 的 execStartActivity 方法，在该方法内部，会调用 intent.migrateExtraStreamToClipData() 方法
        // 里面有一段代码如下
        // else if (ACTION_SEND.equals(action)) {
        //     try {
        //         final Uri stream = getParcelableExtra(EXTRA_STREAM);
        //         final CharSequence text = getCharSequenceExtra(EXTRA_TEXT);
        //         final String htmlText = getStringExtra(EXTRA_HTML_TEXT);
        //         if (stream != null || text != null || htmlText != null) {
        //             final ClipData clipData = new ClipData (
        //                     null, new String[] { getType() },
        //             new ClipData . Item (text, htmlText, null, stream));
        //             setClipData(clipData);
        //             addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        //             return true;
        //         }
        //     } catch (ClassCastException e) {
        //     }
        // }
        // http://androidxref.com/9.0.0_r3/xref/frameworks/base/core/java/android/content/Intent.java#10349
        // 可见是底层帮我们添加好了

        try {
            startActivity(Intent.createChooser(intent, string(R.string.share_picture_title)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, string(R.string.relevance_application_no_found), Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, string(R.string.share_picture_failure), Toast.LENGTH_SHORT).show()
        }
    }

    private fun installApk() {
        val installApkPath = App.sInstance.getExternalFilesDir(APK_PATH)!!.absolutePath
            .plus(File.separator).plus(INSTALL_APK_NAME)

        val installApkFile = File(installApkPath)
        if (!installApkFile.exists()) {
            Toast.makeText(
                this, string(R.string.install_apk_file_not_found), Toast.LENGTH_SHORT
            ).show()
            return
        }
        val installApkUri =
            FileProvider.getUriForFile(
                this, "${BuildConfig.APPLICATION_ID}.fileProvider", installApkFile
            )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(installApkUri, "application/vnd.android.package-archive")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, string(R.string.relevance_application_no_found), Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(this, string(R.string.install_apk_failure), Toast.LENGTH_SHORT).show()
        }
    }
}