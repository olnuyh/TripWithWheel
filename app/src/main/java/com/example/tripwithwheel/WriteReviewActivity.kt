package com.example.tripwithwheel

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tripwithwheel.databinding.ActivityWriteReviewBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WriteReviewActivity : AppCompatActivity() {
    lateinit var binding : ActivityWriteReviewBinding
    lateinit var filePath : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_write_review)

        binding = ActivityWriteReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestGalleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
                if(it.resultCode === android.app.Activity.RESULT_OK) {
                    Glide
                        .with(applicationContext)
                        .load(it.data?.data)
                        .apply(RequestOptions().override(250, 200))
                        .centerCrop()
                        .into(binding.reviewImg)

                    val cursor = contentResolver.query(
                        it.data?.data as Uri,
                        arrayOf<String>(MediaStore.Images.Media.DATA), null, null, null
                    )
                    cursor?.moveToFirst().let {
                        filePath = cursor?.getString(0) as String
                    }
                }
        }

        binding.galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        val requestCameraFileLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val calRatio = calculateInSampleSize(Uri.fromFile(File(filePath)), 150, 150)
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let{
                binding.reviewImg.setImageBitmap(bitmap)
            } ?: let{
                Log.d("mobileApp", "bitmap null")
            }
        }

        val timeS : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storeDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile("JPEG_${timeS}_", ",jpg", storeDir)
        filePath = file.absolutePath
        val photoURI : Uri = FileProvider.getUriForFile(
            this, "com.example.tripwithwheel.fileprovider",
            file
        )

        binding.cameraBtn.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            requestCameraFileLauncher.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_review_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId === R.id.addReview){
            if(binding.reviewImg.drawable !== null && binding.reviewText.text.isNotEmpty()){
                saveStore()
                finish()
            }
            else{
                Toast.makeText(this, "사진을 추가하거나 내용을 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveStore(){
        val data = mapOf(
            "email" to MyApplication.email,
            "content" to binding.reviewText.text.toString(),
            "date" to dateToString(Date())
        )

        MyApplication.db.collection("reviews_" + MyApplication.markerName)
            .add(data)
            .addOnSuccessListener {
                uploadImage(it.id)
            }
            .addOnFailureListener{
                Log.d("mobileApp", "data save error")
            }
    }

    private fun uploadImage(docId : String){
        val storage = MyApplication.storage
        val storageRef = storage.reference
        val imageRef = storageRef.child("images/" + MyApplication.markerName + "/${docId}.jpg")

        val file = Uri.fromFile(File(filePath))
        imageRef.putFile(file)
            .addOnSuccessListener {
                Toast.makeText(this, "리뷰 등록 완료", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Log.d("mobileApp", "file save error")
            }
    }

    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)

            //inJustDecodeBounds 값을 true 로 설정한 상태에서 decodeXXX() 를 호출.
            //로딩 하고자 하는 이미지의 각종 정보가 options 에 설정 된다.
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream!!.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //비율 계산........................
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        //inSampleSize 비율 계산
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}