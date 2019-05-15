package com.hoc.uploadimage.upload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hoc.uploadimage.R
import com.hoc.uploadimage.utils.observe
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_upload.*
import org.koin.android.architecture.ext.viewModel

class UploadFragment : Fragment() {

    private val viewModel by viewModel<UploadViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_upload, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonUploadImage.setOnClickListener {
            viewModel.uploadImage(editTextImageName.text.toString())
        }
        buttonSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(Intent.createChooser(intent, "Select image"), SELECT_IMAGE_REQUEST_CODE)
        }

        viewModel.isLoading.observe(this) {
            progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
        }
        viewModel.message.observe(this) {
            Snackbar.make(view, it, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.uri.observe(this) {
            Picasso.get().load(it)
                    .fit()
                    .centerCrop()
                    .into(imageView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_IMAGE_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    viewModel.setImageUri(data?.data)
                }
            }
        }
    }

    companion object {
        const val SELECT_IMAGE_REQUEST_CODE = 2
    }
}


