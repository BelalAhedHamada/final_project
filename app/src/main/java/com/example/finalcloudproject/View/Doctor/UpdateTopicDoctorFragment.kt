package com.example.finalcloudproject.View.Doctor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Topic
import com.example.finalcloudproject.Model.User
import com.example.finalcloudproject.R
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentUpdateTopicDoctorBinding
import com.example.finalcloudproject.databinding.PickImageDialogBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class UpdateTopicDoctorFragment : Fragment() {
    private lateinit var binding: FragmentUpdateTopicDoctorBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    lateinit var topic: Topic
    val args: UpdateTopicDoctorFragmentArgs by navArgs()
    var imgUrl: Uri? = null
    var user: User? = null
    lateinit var listUser: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateTopicDoctorBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        topic = args.topic
        listUser = arrayListOf()

        binding.edtNameTopicUp.append(topic.name)
        binding.editDescriptionTopicUp.append(topic.description)
        Glide.with(requireActivity()).load(topic.image).into(binding.imageTopicUp)
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        palliatveViewModel.getUser()
        palliatveViewModel.user!!.observe(viewLifecycleOwner, Observer { it ->
            for (item in it) {
                user = item
            }
        })

        binding.imageTopicUp.setOnClickListener {
//            showDialog(requireContext())
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
        binding.btnEditTopic.setOnClickListener {
            listUser.add(user!!)

            if (binding.edtNameTopicUp.text.isNotEmpty() && binding.editDescriptionTopicUp.text.isNotEmpty()) {
                palliatveViewModel.updateTopic(
                    binding.root, Topic(
                        topic.id.toString(),
                        binding.edtNameTopicUp.text.toString(),
                        binding.editDescriptionTopicUp.text.toString(),
                        imgUrl.toString(),
                        System.currentTimeMillis(),
                        topic.idDoctor.toString(),
                        topic.state,
                        user!!.name, listUser
                    ), imgUrl, topic.id.toString()
                )
            } else {
                Constants.showSnackBar(
                    binding.root, "إملا الحقول المطلوبة", Constants.redColor
                )

            }

        }
        return binding.root
    }

    private fun openMedia() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 111)
    }

//    private fun showDialog(context: Context) {
//        var binding: PickImageDialogBinding = PickImageDialogBinding.inflate(layoutInflater)
//
//        var dialog = MaterialAlertDialogBuilder(context).setView(binding.root).show()
//
//        binding.loCamera.setOnClickListener {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
//            startActivityForResult(intent, 111)
//            dialog.dismiss()
//        }
//
//        binding.loGallery.setOnClickListener {
//            val i = Intent(Intent.ACTION_PICK)
//            i.type = "image/*"
//            startActivityForResult(i, 222)
//            dialog.dismiss()
//        }
//        binding.tvBtnCancel.setOnClickListener {
//            dialog.dismiss()
//        }
//    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if ((requestCode == 222 || requestCode == 111) && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
//            imgUrl = data.data
//            binding.imageTopicUp.setImageURI(imgUrl)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Toast.makeText(requireContext(),"ASDASDASDASD", Toast.LENGTH_SHORT).show()

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            imgUrl = data!!.data

            // Use Uri object instead of File to avoid storage permissions
            binding.imageTopicUp.setImageURI(imgUrl)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkImageUrl(): Uri {
        if (imgUrl == null) {
            imgUrl = Uri.parse(topic.image)
        } else {
            imgUrl
        }
        return imgUrl!!
    }
}