package com.example.finalcloudproject.View.Doctor

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.NotificationData
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentUpdateAdvicesBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID

class UpdateAdvicesFragment : Fragment() {
    private lateinit var binding: FragmentUpdateAdvicesBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    val args: UpdateAdvicesFragmentArgs by navArgs()
    var videoUrl: Uri? = null
    var imageUri: Uri? = null
    lateinit var advice: Advices
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateAdvicesBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        advice = args.advice
        val topic = args.idTopic
        auth = Firebase.auth

        binding.edtNameAdvicesUp.append(advice.name)
        binding.editDesAdvicesUp.append(advice.description)
        binding.editVideoAdvicesUp.append(advice.video)
        Glide.with(requireActivity()).load(advice.image).into(binding.imgUpAdvices)

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnUpAdv.setOnClickListener {
            val time = System.currentTimeMillis()
            if (binding.edtNameAdvicesUp.text.isNotEmpty() && binding.editDesAdvicesUp.text.isNotEmpty()) {
                if (videoUrl == null) {
                    palliatveViewModel.updateAdvice(
                        binding.root, Advices(
                            advice.id.toString(),
                            binding.edtNameAdvicesUp.text.toString(),
                            binding.editDesAdvicesUp.text.toString(),
                            time,
                            true,
                            imageUri.toString()
                        ), imageUri, Uri.parse(""), topic.id.toString(), advice.id.toString(), "5000"
                    )

                } else {
                    palliatveViewModel.updateAdvice(
                        binding.root, Advices(
                            advice.id.toString(),
                            binding.edtNameAdvicesUp.text.toString(),
                            binding.editDesAdvicesUp.text.toString(),
                            time,
                            true,
                            imageUri.toString(),
                            videoUrl.toString()
                        ), imageUri, videoUrl, topic.id.toString(), advice.id.toString(), "80000"
                    )
                }

                for (users in topic?.users!!) {
                    if (users.id != "" && users.id != auth.currentUser!!.uid) {

                        val topics = "/topics/${users.id}"
                        PushNotification(
                            NotificationData(
                                "تعديل نصيحة جديدة لموضوع ${topic.name}",
                                "تم تعديل النصيحة : ${binding.edtNameAdvicesUp.text}"
                            ),
                            topics
                        ).also {
                            users!!.id?.let { it1 ->
                                palliatveViewModel.sendNotification(
                                    it,
                                    Notification(
                                        UUID.randomUUID().toString(),
                                        "تعديل نصيحة جديدة لموضوع ${topic.name}",
                                        "تم تعديل النصيحة : ${binding.edtNameAdvicesUp.text}",
                                        System.currentTimeMillis()
                                    ),
                                    it1,
                                )
                            }
                        }
                    }


                }

            } else {
                Constants.showSnackBar(
                    binding.root, "إملا الحقول المطلوبة",
                    Constants.redColor
                )
            }
        }

        binding.editVideoAdvicesUp.setOnClickListener {
            chooseVideo()
        }

        binding.imgUpAdvices.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        return binding.root
    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 5000)
    }

    private fun openMedia() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 111)
    }

//    @Deprecated("Deprecated in Java")
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 5000 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
//            videoUrl = data.data
//            binding.editVideoAdvicesUp.append(videoUrl.toString())
//
//        } else if (requestCode == 111 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
//            imageUri = data.data
//            binding.imgUpAdvices.setImageURI(imageUri)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            videoUrl = data.data
            binding.editVideoAdvicesUp.append(videoUrl.toString())
        } else if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            imageUri = data!!.data

            // Use Uri object instead of File to avoid storage permissions
            binding.imgUpAdvices.setImageURI(imageUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkVideoUrl(): Uri {
        if (videoUrl == null) {
            videoUrl = Uri.parse(advice.video)
        } else {
            videoUrl
        }
        return videoUrl!!
    }

    fun checkImageUrl(): Uri {
        if (imageUri == null) {
            imageUri = Uri.parse(advice.image)
        } else {
            imageUri
        }
        return imageUri!!
    }
}