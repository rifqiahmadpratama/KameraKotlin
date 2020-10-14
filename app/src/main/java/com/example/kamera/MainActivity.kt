package com.example.kamera

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    companion object{
        val MY_CAMERA_REQUEST_CODE  = 7171 //Any number you want

        val hitung  = 1 + 3
    }

    var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d("Hitungan adalah = ", hitung.toString())
        //Event
        btn_camera.setOnClickListener {
            Dexter.withContext(this)
                .withPermissions(listOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
                )
                ).withListener(object : MultiplePermissionsListener{
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        if (p0 != null) {
                            if (p0.areAllPermissionsGranted()) {
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE,"New Picture")
                                values.put(MediaStore.Images.Media.DESCRIPTION,"From Your Camera")
                                imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)!!
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
                                startActivityForResult(intent, MY_CAMERA_REQUEST_CODE)
                            } else {
                                Toast.makeText(this@MainActivity,"You must accept all permission", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {

                    }

                }).check()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_CAMERA_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri!!)
                    img_preview.setImageBitmap(bitmap)
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }
}