package com.ab.hicareservices.utils
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ab.hicareservices.BuildConfig
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class AppUpdater(private val context: Context) {

    private val apkUrl = "https://play.google.com/store/apps/details?id=com.ab.hicareservices" // Replace with your APK download URL

    fun checkForUpdate() {
        // You would typically check your backend for the latest version information here

        // For simplicity, let's assume the latest version code is 2
        val latestVersionCode = 2

        if (latestVersionCode > BuildConfig.VERSION_CODE) {
            showUpdateDialog()
        } else {
            Toast.makeText(context, "Your app is up to date", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Do you want to update now?")
        builder.setPositiveButton("Update") { _, _ ->
            // Start the download and installation process
            DownloadTask().execute(apkUrl)
        }
        builder.setNegativeButton("Later") { _, _ ->
            // User chose to update later
        }
        builder.show()
    }

    private inner class DownloadTask : AsyncTask<String, Int, File?>() {

        override fun doInBackground(vararg params: String): File? {
            try {
                val url = URL(params[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                // Get the file length
                val fileLength = connection.contentLength

                // Input stream to read file
                val input = BufferedInputStream(url.openStream(), 8192)

                // Output stream to write file
                val outputDir = context.cacheDir
                val outputFile = File(outputDir, "update.apk")
                val output = FileOutputStream(outputFile)

                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int

                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    // Publishing the progress....
                    publishProgress((total * 100 / fileLength).toInt())
                    output.write(data, 0, count)
                }

                // Flush the output stream
                output.flush()

                // Close streams
                output.close()
                input.close()

                return outputFile
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: File?) {
            if (result != null) {
                installApk(result)
            } else {
                Toast.makeText(context, "Failed to download update", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onProgressUpdate(vararg values: Int?) {
            // Update your progress bar here
        }
    }

    private fun installApk(apkFile: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        val uri: Uri

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                apkFile
            )
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(apkFile)
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}