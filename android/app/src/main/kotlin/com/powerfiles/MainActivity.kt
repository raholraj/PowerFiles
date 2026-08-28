package com.powerfiles

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.powerfiles.channels.*
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    private lateinit var fileChannel: FileChannel
    private lateinit var storageChannel: StorageChannel
    private lateinit var terminalChannel: TerminalChannel
    private lateinit var safChannel: SAFChannel
    private lateinit var packageChannel: PackageChannel

    companion object {
        const val REQUEST_MANAGE_STORAGE = 1001
        const val REQUEST_SAF_ANDROID_DATA = 1002
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Initialize all channels
        fileChannel = FileChannel(this, flutterEngine.dartExecutor.binaryMessenger)
        storageChannel = StorageChannel(this, flutterEngine.dartExecutor.binaryMessenger)
        terminalChannel = TerminalChannel(this, flutterEngine.dartExecutor.binaryMessenger)
        safChannel = SAFChannel(this, flutterEngine.dartExecutor.binaryMessenger)
        packageChannel = PackageChannel(this, flutterEngine.dartExecutor.binaryMessenger)

        // Permission channel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.powerfiles/permissions")
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "requestManageStorage" -> requestManageStoragePermission(result)
                    "checkManageStorage" -> result.success(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                            Environment.isExternalStorageManager()
                        else true
                    )
                    "requestSAFAndroidData" -> safChannel.requestAndroidDataAccess(result)
                    "openAppSettings" -> {
                        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.parse("package:$packageName")
                        })
                        result.success(null)
                    }
                    else -> result.notImplemented()
                }
            }
    }

    private fun requestManageStoragePermission(result: MethodChannel.Result) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    intent.data = Uri.parse("package:$packageName")
                    startActivityForResult(intent, REQUEST_MANAGE_STORAGE)
                } catch (e: Exception) {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    startActivityForResult(intent, REQUEST_MANAGE_STORAGE)
                }
                result.success("requested")
            } else {
                result.success("granted")
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_MANAGE_STORAGE
                )
            }
            result.success("requested")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SAF_ANDROID_DATA -> safChannel.handleActivityResult(resultCode, data)
            REQUEST_MANAGE_STORAGE -> { /* Permission result handled by system */ }
        }
    }
}
