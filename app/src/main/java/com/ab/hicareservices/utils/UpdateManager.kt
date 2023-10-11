package com.ab.hicareservices.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class UpdateManager(private val context: Context, toString: String) {

    val apkUrl ="https://play.google.com/store/apps/details?id=com.ab.hicareservices"

    fun checkForUpdates(toString: String) {
        // Implement logic to check for updates (e.g., compare current version with server version)
        // If update is available, initiate download and installation
        downloadAndInstallUpdate("https://play.google.com/store/apps/details?id=com.ab.hicareservices",toString)
    }

    private fun downloadAndInstallUpdate(apkUrl: String, toString: String) {
        // Use a library like Retrofit or any other networking library to download the APK
        // Once downloaded, initiate the installation
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(apkUrl))
            .setTitle("My App Update")
            .setDescription("Downloading update...")
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "update.apk")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        val downloadId = downloadManager.enqueue(request)

        // Register a BroadcastReceiver to listen for the completion of the download
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE == intent.action) {
                    // The download is complete, initiate the installation
                    installUpdate(downloadManager, downloadId)
                }
            }
        }

        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun installUpdate(downloadManager: DownloadManager, downloadId: Long) {
        // Get the URI of the downloaded file
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
            val uriString = cursor.getString(uriIndex)
            val file = File(Uri.parse(uriString).path)

            // Create an Intent to install the APK
            val installIntent = Intent(Intent.ACTION_VIEW)
                .setDataAndType(FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file), "application/vnd.android.package-archive")
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            // Start the installation
            context.startActivity(installIntent)
        }
    }
}