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
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.finalcloudproject.Constants.Constants
import com.example.finalcloudproject.Model.Advices
import com.example.finalcloudproject.Model.Notification
import com.example.finalcloudproject.Model.NotificationData
import com.example.finalcloudproject.Model.PushNotification
import com.example.finalcloudproject.View.Doctor.DoctorActivity
import com.example.finalcloudproject.ViewModel.PalliativeViewModel
import com.example.finalcloudproject.databinding.FragmentAddAdvicesBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID

class AddAdvicesFragment : Fragment() {

    private lateinit var binding: FragmentAddAdvicesBinding
    private lateinit var palliatveViewModel: PalliativeViewModel
    var videoUrl: Uri? = null
    var imageUri: Uri? = null
    lateinit var idAdvice: UUID
    val args: AddAdvicesFragmentArgs by navArgs()
    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAdvicesBinding.inflate(layoutInflater)
        palliatveViewModel = (activity as DoctorActivity).palliatveViewModel
        idAdvice = UUID.randomUUID()
        val topic = args.topic
        auth = Firebase.auth

        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddAdv.setOnClickListener {
            val time = System.currentTimeMillis()
            if (binding.edtNameAdvicesAdd.text.isNotEmpty() && binding.editDesAdvicesAdd.text.isNotEmpty() && imageUri != null) {
                if (videoUrl == null) {
                    palliatveViewModel.addAdvice(
                        binding.root, Advices(
                            idAdvice.toString(),
                            binding.edtNameAdvicesAdd.text.toString(),
                            binding.editDesAdvicesAdd.text.toString(),
                            time,
                            true,
                            imageUri.toString()
                        ), imageUri, Uri.parse(""), topic.id.toString(), "5000"
                    )

                } else {
                    palliatveViewModel.addAdvice(
                        binding.root, Advices(
                            idAdvice.toString(),
                            binding.edtNameAdvicesAdd.text.toString(),
                            binding.editDesAdvicesAdd.text.toString(),
                            time,
                            true,
                            imageUri.toString(),
                            videoUrl.toString()
                        ), imageUri, videoUrl, topic.id.toString(), "80000"
                    )
                }

                for (users in topic?.users!!) {
                    if (users.id != "" && users.id != auth.currentUser!!.uid) {

                        val topics = "/topics/${users.id}"
                        PushNotification(
                            NotificationData(
                                "اضافة نصيحة جديدة لموضوع ${topic.name}",
                                "تم اضافة النصيحة : ${binding.edtNameAdvicesAdd.text}"
                            ),
                            topics
                        ).also {
                            users!!.id?.let { it1 ->
                                palliatveViewModel.sendNotification(
                                    it,
                                    Notification(
                                        UUID.randomUUID().toString(),
                                        "اضافة نصيحة جديدة لموضوع ${topic.name}",
                                        "تم اضافة النصيحة : ${binding.edtNameAdvicesAdd.text}",
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

        binding.imgAddAdvices.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(
                    1080,
                    1080
                )
                .start()
        }
        binding.editVideoAdvicesAdd.setOnClickListener {
            chooseVideo()
        }

        return binding.root
    }

    private fun chooseVideo() {
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 5000)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 5000 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            videoUrl = data.data
            binding.editVideoAdvicesAdd.append(videoUrl.toString())
        } else if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            imageUri = data!!.data

            // Use Uri object instead of File to avoid storage permissions
            binding.imgAddAdvices.setImageURI(imageUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

}