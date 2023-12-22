package com.example.florapedia.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.florapedia.R
import com.example.florapedia.ViewModelFactoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CameraFragment : Fragment() {

    private lateinit var cameraXButton: Button
    private lateinit var imageView: ImageView
    private lateinit var predictLabelTextView: TextView
    private var capturedImage: Bitmap? = null
    private lateinit var progressBarCamera: ProgressBar

    private val cameraViewModel by viewModels<CameraViewModel> {
        ViewModelFactoryModel.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scan_preview, container, false)
        cameraXButton = view.findViewById(R.id.cameraXButton)
        imageView = view.findViewById(R.id.previewImageView )
        predictLabelTextView = view.findViewById(R.id.predictLabelTextView)
        progressBarCamera = view.findViewById(R.id.progressBar)

        cameraViewModel.predictResult.observe(viewLifecycleOwner) { result ->
            lifecycleScope.launch(Dispatchers.Main) {
                Log.d("PredictResult", result.toString())
                predictLabelTextView.text = result?.prediction.orEmpty()
                showLoading(false)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraXButton.setOnClickListener {
            showLoading(true)
            openCamera()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            capturedImage = imageBitmap
            imageView.setImageBitmap(capturedImage)
            showLoading(true)
            predictImage()
        }
    }

    private fun predictImage() {
        capturedImage?.let {
            lifecycleScope.launch {
                try {
                    // Panggil fungsi predictModel di CameraViewModel
                    cameraViewModel.predictModel(it)
                } catch (e: Exception) {
                    // Tangani kesalahan jika diperlukan
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBarCamera.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1

        @JvmStatic
        fun newInstance() =
            CameraFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}