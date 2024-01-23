package com.ab.hicareservices.utils
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ab.hicareservices.BuildConfig
import com.ab.hicareservices.data.SharedPreferenceUtil
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class AppUpdater(private val context: Context, toString: String, isUpdated: Boolean?) {

    private val apkUrl = "https://play.google.com/store/apps/details?id=com.ab.hicareservices" // Replace with your APK download URL
    var Ischeckupdate=false
    var checkupdated=false
    fun checkForUpdate(toString: String, isUpdated: Boolean?) {
        // You would typically check your backend for the latest version information here

        // For simplicity, let's assume the latest version code is 2
        var latestVersionCode = toString.toInt()


        var versioncode:Int= SharedPreferenceUtil.getData(context, "latestversioncode", 0) as Int

//        if (isUpdated != null) {
//            checkupdated=isUpdated
//        }

        checkupdated=true

        if(isUpdated==false) {

            val Isnotupdated=SharedPreferenceUtil.getData(context, "IsUpdated", false)

            if(latestVersionCode>versioncode){
                Log.d("VersionApp",latestVersionCode.toString() + " "+versioncode.toString())
                if (latestVersionCode > BuildConfig.VERSION_CODE) {
                    SharedPreferenceUtil.setData(context, "latestversioncode", latestVersionCode)
                    showUpdateDialog()
                }
            }else if(Isnotupdated==true){
//                if(Ischeckupdate==false){
//                    if (latestVersionCode > BuildConfig.VERSION_CODE) {
//                        SharedPreferenceUtil.setData(context, "latestversioncode", latestVersionCode)
//                        showUpdateDialog()
//                    } else {
//                        Toast.makeText(context, "Your app is up to date", Toast.LENGTH_SHORT).show()
//                    }
//                }else{
//
//                }

            }else{
                if (latestVersionCode > BuildConfig.VERSION_CODE) {
                    Log.d("VersionApp",latestVersionCode.toString() + " "+BuildConfig.VERSION_CODE.toString())
                    SharedPreferenceUtil.setData(context, "latestversioncode", latestVersionCode)
                    showUpdateDialog1()
                } else {
                    Toast.makeText(context, "Your app is up to date", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            if (latestVersionCode > BuildConfig.VERSION_CODE) {
                showUpdateDialog1()
            } else {
                Toast.makeText(context, "Your app is up to date", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showUpdateDialog1() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle("New Update Available")
        builder.setMessage("A new version of the app is available. Do you want to update now?")
        builder.setPositiveButton("Update") { _, _ ->
            // Start the download and installation process
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.ab.hicareservices")
                )
            )
            DownloadTask().execute(apkUrl)
        }
        builder.show()
    }

    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle("New Update Available")
        builder.setMessage("A new version of the app is available. Do you want to update now?")
        builder.setPositiveButton("Update") { _, _ ->
            // Start the download and installation process
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.ab.hicareservices")
                )
            )
            DownloadTask().execute(apkUrl)
        }
        builder.setNegativeButton("Later") { _, _ ->
            // User chose to update later
            Ischeckupdate=true
            SharedPreferenceUtil.setData(context, "IsUpdated", true)
        }
        builder.show()
    }


    private fun showUpdateDialogs() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle("Update Available")
        builder.setMessage("A new version of the app is available. Do you want to update now?")
        builder.setPositiveButton("Update") { _, _ ->
            // Start the download and installation process
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.ab.hicareservices")
                )
            )
            DownloadTask().execute(apkUrl)
        }
//        builder.setNegativeButton("Later") { _, _ ->
//            // User chose to update later
//            Ischeckupdate=true
//        }
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