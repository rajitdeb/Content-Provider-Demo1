package com.rajit.contentprovidersample_musicplayerapp

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.rajit.contentprovidersample_musicplayerapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mAdapter by lazy { SongsRvAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.songsRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            adapter = mAdapter
        }

        if (hasPermission(Build.VERSION.SDK_INT)) {
            val audioFiles = MediaUtil.getAllAudioFiles(applicationContext)
            mAdapter.setList(audioFiles)
        } else {
            requestPermission(Build.VERSION.SDK_INT)
            showMandatoryPermissionDialog()
        }

    }

    @SuppressLint("InlinedApi")
    private fun hasPermission(apiLevel: Int): Boolean {

        val permissionToCheck =
            if (apiLevel >= 33) MediaConstants.AUDIO_PERMISSION else MediaConstants.STORAGE_PERMISSION

        return ContextCompat.checkSelfPermission(
            this@MainActivity,
            permissionToCheck
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(apiLevel: Int) {

        val permissionToCheck =
            if (apiLevel >= 33) MediaConstants.AUDIO_PERMISSION else MediaConstants.STORAGE_PERMISSION

        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(permissionToCheck),
            MediaConstants.PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MediaConstants.PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted; proceed with accessing MediaStore.Audio
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                mAdapter.setList(MediaUtil.getAllAudioFiles(this@MainActivity))
            } else {
                // Permission denied; handle the case
                Toast.makeText(
                    this,
                    "Permission denied. Cannot access audio files.",
                    Toast.LENGTH_SHORT
                ).show()

                showMandatoryPermissionDialog()
            }
        }
    }

    private fun showMandatoryPermissionDialog() {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Mandatory Permission")
            .setMessage("STORAGE PERMISSION is required to access all music files present in your phone.")
            .setCancelable(false)
            .setPositiveButton("GRANT") { dialog, _ ->
                dialog.dismiss()
                openAppSettingsPage()
            }
            .show()
    }

    private fun openAppSettingsPage() {
        // If user wants to grant permission from Mandatory Dialog,
        // we can help user navigate to permission page using this intent
        // This intent will take user to our application's settings page
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
        )
    }
}