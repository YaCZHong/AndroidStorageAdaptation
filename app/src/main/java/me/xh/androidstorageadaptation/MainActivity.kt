package me.xh.androidstorageadaptation

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import me.xh.androidstorageadaptation.databinding.ActivityMainBinding
import me.xh.androidstorageadaptation.utils.queryAllImagesBefore13
import me.xh.androidstorageadaptation.utils.queryAllImagesIn13
import me.xh.androidstorageadaptation.utils.saveBitmapBeforeQ
import me.xh.androidstorageadaptation.utils.saveBitmapInQ
import me.xh.androidstorageadaptation.utils.view2Bitmap
import me.xh.kit.toast.toast

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
            btnSaveBitmap.setOnClickListener { saveBitmap() }
            btnQueryAllImages.setOnClickListener { queryAllImages() }
        }
    }

    /**
     * *************
     *
     * 保存图片到本地
     *
     * *************
     */
    @SuppressLint("MissingPermission")
    private val requestReadWriteStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantedMap ->
            val grantedAll = grantedMap.entries.all { entry -> entry.value }
            if (grantedAll) {
                saveBitmap()
            } else {
                toast(R.string.permission_not_completed)
            }
        }

    private fun saveBitmap() {
        val bitmap = view2Bitmap(binding.root)
        val bitmapName = "bitmap.png"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveBitmapInQ(bitmap, bitmapName)
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestReadWriteStoragePermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
                return
            }
            saveBitmapBeforeQ(bitmap, bitmapName)
        }
    }

    /**
     * *************
     *
     * 查询设备所有图片
     *
     * *************
     */
    private val requestReadImagesPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                queryAllImages()
            } else {
                toast(R.string.permission_not_completed)
            }
        }

    private val requestReadStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                queryAllImages()
            } else {
                toast(R.string.permission_not_completed)
            }
        }

    private fun queryAllImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestReadImagesPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                return
            }
            queryAllImagesIn13()
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestReadStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return
            }
            queryAllImagesBefore13()
        }
    }
}