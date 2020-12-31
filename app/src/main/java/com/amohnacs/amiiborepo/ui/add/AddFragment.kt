package com.amohnacs.amiiborepo.ui.add

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.amohnacs.amiiborepo.R
import com.amohnacs.amiiborepo.common.InjectionFragment
import com.amohnacs.amiiborepo.common.ViewModelFactory
import com.amohnacs.amiiborepo.databinding.FragmentAddBinding
import com.amohnacs.amiiborepo.ui.details.DetailsFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.*
import javax.inject.Inject

class AddFragment : InjectionFragment() {

    @Inject
    lateinit var factory: ViewModelFactory<AddViewModel>
    private lateinit var viewModel: AddViewModel

    private var binding: FragmentAddBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainDaggerComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAddBinding.inflate(inflater)

        binding?.sampleImage?.setOnClickListener {
            dispatchTakePictureIntent()
        }
        binding?.addButton?.setOnClickListener {
            viewModel.saveAmiibo(
                binding?.characterNameInput?.text.toString().trim(),
                binding?.seriesNameInput?.text.toString().trim(),
                binding?.typeNameInput?.text.toString().trim(),
                binding?.purchasedCheck?.isChecked == true
            )
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val actions = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                    findNavController().navigate(actions)
                }
            }
        )
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(AddViewModel::class.java)

        viewModel.errorEvent.observe(viewLifecycleOwner, Observer { errorString ->
            binding?.root?.let { view ->
                Snackbar.make(view, errorString, Snackbar.LENGTH_LONG).show()
            }
        })
        viewModel.successfulSaveEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                AlertDialog.Builder(requireActivity())
                    .setTitle(requireActivity().getString(R.string.successful_save))
                    .setMessage(requireActivity().getString(R.string.successful_save_message))
                    .setPositiveButton(requireActivity().getString(R.string.ok)
                    ) { _, _ ->
                        val actions = DetailsFragmentDirections.actionDetailsFragmentToMainFragment()
                        findNavController().navigate(actions)
                    }.setCancelable(false)
                    .create()
                    .show()
            } else {
                binding?.root?.let { view ->
                    Snackbar.make(view, "Something went wrong saving your Amiibo yo", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EXTERNAL_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            viewModel.updateAmiiboImage(imageBitmap)

            binding?.sampleImage?.setImageBitmap(imageBitmap)
            binding?.clickCameraText?.visibility = View.GONE
        } else {
            binding?.root?.let { view ->
                Snackbar.make(view, "Something went wrong with camera", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, EXTERNAL_CAMERA_REQUEST)
        } catch (e: ActivityNotFoundException) {

        }
    }

    companion object {
        const val EXTERNAL_CAMERA_REQUEST = 101
    }
}