package com.example.almacenamientoext

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

import android.content.pm.PackageManager
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.widget.Toast

import android.util.Log

import java.io.*


class MainActivity : AppCompatActivity() {

    private val REQUEST_ID_READ_PERMISSION = 100
    private val REQUEST_ID_WRITE_PERMISSION = 200

    private val fileName = "note.txt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bt_write.setOnClickListener { askPermissionAndWriteFile() }
        bt_read.setOnClickListener { askPermissionAndReadFile() }

    }

    private fun askPermissionAndWriteFile() {
        val canWrite = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        //
        if (canWrite) {
            this.writeFile()
        }
    }

    private fun askPermissionAndReadFile() {
        val canRead = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        //
        if (canRead) {
            this.readFile()
        }
    }

    private fun askPermission(requestId: Int, permissionName: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            val permission = ActivityCompat.checkSelfPermission(this, permissionName)


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        arrayOf(permissionName),
                        requestId
                )
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.size > 0) {
            when (requestCode) {
                REQUEST_ID_READ_PERMISSION -> {
                    run {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            readFile()
                        }
                    }
                    run {
                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            writeFile()
                        }
                    }
                }
                REQUEST_ID_WRITE_PERMISSION -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        writeFile()
                    }
                }
            }
        } else {
            Toast.makeText(applicationContext, "Permission Cancelled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeFile() {

        val extStore = Environment.getExternalStorageDirectory()
        // ==> /storage/emulated/0/note.txt
        val path = extStore.getAbsolutePath() + "/" + fileName
        Log.i("ExternalStorageDemo", "Save to: $path")

        val data = et_01.text.toString()

        try {
            val myFile = File(path)
            myFile.createNewFile()
            val fOut = FileOutputStream(myFile)
            val myOutWriter = OutputStreamWriter(fOut)
            myOutWriter.append(data)
            myFile.setWritable(true,false)
            myOutWriter.close()
            fOut.close()


            Toast.makeText(applicationContext, "$fileName saved", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun readFile() {

        val extStore = Environment.getExternalStorageDirectory()
        // ==> /storage/emulated/0/note.txt
        val path = extStore.absolutePath + "/" + fileName
        Log.i("ExternalStorageDemo", "Read file: $path")

        var s :String
        var fileContent = ""
        try {
            val myFile = File(path)
            val fIn = FileInputStream(myFile)

           fileContent = fIn.bufferedReader().use(BufferedReader::readText)


            tv_01.text = fileContent
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }


}

