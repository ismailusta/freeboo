package com.example.fribu.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.fribu.R
import com.example.fribu.databinding.FragmentFeedBinding
import com.example.fribu.databinding.FragmentYukleBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.jar.Manifest

class FragmentYukle : Fragment() {

    private var _binding: FragmentYukleBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityresult : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher : ActivityResultLauncher<String>
    var selectedGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        storage = Firebase.storage
        db = Firebase.firestore
        registerLaunchers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentYukleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView8.setOnClickListener {
            gorselsec(it)
        }
        binding.btnYukle.setOnClickListener {
            yukle(it)
        }
    }
    fun gorselsec(view: View){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            // read ımagestore
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(requireView(), "Galeriye gitmek için izin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", View.OnClickListener {
                        permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                    }).show()
                }
                else{
                    permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
            else{
                val intenttoGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityresult.launch(intenttoGallery)
            }
        }
        else{
            if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(requireView(), "Galeriye gitmek için izin gerekli", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", View.OnClickListener {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
                }
                else{
                    permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            else{
                val intenttoGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityresult.launch(intenttoGallery)
            }
        }
    }
    fun yukle(view: View){
        val uuid = java.util.UUID.randomUUID()
        val imagename = "${uuid}.jpg"
        val reference = storage.reference
        val gorselreference = reference.child("images").child(imagename)
        if(selectedGorsel!= null){
            gorselreference.putFile(selectedGorsel!!).addOnSuccessListener {uploadTask->
                // url alma işlemi,
                gorselreference.downloadUrl.addOnSuccessListener {url->
                    val dowloandurl = url.toString()

                    val postMap = hashMapOf<String, Any>()
                    postMap.put("dowloandurl", dowloandurl)
                    postMap.put("userEmail", auth.currentUser!!.email.toString())
                    postMap.put("comment",binding.edittextCommentYukle.text.toString())
                    postMap.put("date", com.google.firebase.Timestamp.now())

                    db.collection("Posts").add(postMap).addOnSuccessListener {doc->
                        val action = FragmentYukleDirections.actionFragmentYukleToFragmentFeed()
                        Navigation.findNavController(requireView()).navigate(action)
                    }.addOnFailureListener {
                        exception->
                        Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }



                }
            }.addOnFailureListener {exception->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun registerLaunchers(){
        activityresult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if (intentFromResult!=null){
                    selectedGorsel = intentFromResult.data
                    try {
                        if (Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(requireActivity().contentResolver, selectedGorsel!!)
                            secilenBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageView8.setImageBitmap(secilenBitmap)
                        }
                        else{
                            secilenBitmap= MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedGorsel)
                            binding.imageView8.setImageBitmap(secilenBitmap)
                        }
                    }
                    catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            result->
            if (result){
                val intenttoGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityresult.launch(intenttoGallery)
            }
            else{
                Toast.makeText(requireContext(), "İzin Gerekli", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}