package me.xh.androidstorageadaptation

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.xh.androidstorageadaptation.databinding.ActivityMainBinding
import me.xh.androidstorageadaptation.utils.saveBitmap
import me.xh.androidstorageadaptation.utils.view2Bitmap
import me.xh.kit.toast.toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                saveBitmapToPhotoAlbum()
            } else {
                toast(R.string.request_storage_permission_be_refused)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.apply {
            btnSaveBitmap.setOnClickListener {
                checkStoragePermission()
            }
        }
    }

    private fun checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveBitmapToPhotoAlbum()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun saveBitmapToPhotoAlbum() {
        lifecycleScope.launch { saveBitmap(view2Bitmap(binding.root), "bitmap.png") }
    }
}