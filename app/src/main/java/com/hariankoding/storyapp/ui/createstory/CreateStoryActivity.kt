package com.hariankoding.storyapp.ui.createstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.hariankoding.storyapp.R
import com.hariankoding.storyapp.databinding.ActivityCreateStoryBinding
import com.hariankoding.storyapp.ui.home.MainActivity
import com.hariankoding.storyapp.utils.*
import com.hariankoding.storyapp.viewmodel.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class CreateStoryActivity : AppCompatActivity() {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCreateStoryBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<CreateStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private var isBackCamera: Boolean = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setUi()
        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            binding.btnUpload.isEnabled = false
            uploadImage()
        }
        setOnChange()
    }

    private fun setOnChange() {
        binding.edAddDescription.apply {
            doAfterTextChanged { binding.tlDescription.isErrorEnabled = false }
            setOnFocusChangeListener { _, _ ->
                binding.tlDescription.isErrorEnabled = false
            }
        }
    }

    private fun uploadStory(imageMultipart: MultipartBody.Part, toRequestBody: RequestBody) {
        viewModel.uploadImage(imageMultipart, toRequestBody).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    UtilsUi.showDialog(this)
                }
                is Result.Success -> {
                    UtilsUi.closeDialog()
                    result.data.let {
                        if (!it.error) {
                            val intent = Intent()
                            intent.putExtra("isUpdate", true)
                            setResult(MainActivity.CREATE_STORY, intent)
                            finish()
                            binding.btnUpload.isEnabled = true
                        }
                    }
                }
                is Result.Error -> {
                    UtilsUi.closeDialog()
                    showMessage(result.error)
                    binding.btnUpload.isEnabled = true
                }
            }
        }
    }

    private fun uploadImage() {
        val description = binding.edAddDescription.text.toString()
        if (description.isEmpty()) {
            binding.tlDescription.error = getString(R.string.is_not_empty)
            binding.btnUpload.isEnabled = true
            return
        }
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpg".toMediaType())
            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)
            uploadStory(
                imageMultipart,
                description.toRequestBody("text/plain".toMediaType())
            )
        } else {
            binding.btnUpload.isEnabled = true
            showMessage("Image is Required")
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)

    }

    private var getFile: File? = null
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)
            binding.imgPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val selectedImg: Uri = result.data?.data as Uri
                val myFile = uriToFile(selectedImg, this@CreateStoryActivity)
                getFile = myFile
                binding.imgPreview.setImageURI(selectedImg)
            }
        }

    private fun setUi() {
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                finish()
            }
            title = getString(R.string.new_story)
        }
    }
}