package com.return0.filesviewer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.anggrayudi.storage.SimpleStorageHelper
import com.anggrayudi.storage.file.getAbsolutePath
import com.anggrayudi.storage.file.mimeType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.return0.filesviewer.databinding.FragmentDisplayBinding
import java.util.*


class DisplayFragment : Fragment() {
    //Viewbinding and the navigation host
    private lateinit var binding: FragmentDisplayBinding
    lateinit var navHostFragment: NavHostFragment

    //This is just for reading files with android 11 scoped storage.
    // You wont need it if you already have the functionality implemented
    var simpleStorage=SimpleStorageHelper(this)
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
      binding= FragmentDisplayBinding.inflate(inflater, container, false)
        val addN=binding.addNew
        navHostFragment=requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        //Setup recyclerview for displaying a list of files
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager=LinearLayoutManager(requireActivity());

        //The fab is for adding new files for testing
        addN.setOnClickListener {
simpleStorage.openFilePicker()
        }

simpleStorage.onFileSelected={requestCode, files ->
Log.e("Selected files","${files[0].mimeType} ${files[0].type} ${files[0].getAbsolutePath(requireActivity())}")

  //We are just uploading the file to storage and storing its download url in firestore
    val storage=FirebaseStorage.getInstance();
 val extension=files[0].name!!.split(".").last()
 val newName="${System.nanoTime()}.$extension";

 val refrence=storage.getReference("docs").child(newName)
    binding.progressBar.show()
    val uploadTask=refrence.putFile(files[0].uri)
    val continuation=uploadTask.continueWithTask {
if (!it.isSuccessful) {
    binding.progressBar.hide()
    it.let {
        throw it.exception!!

    }
}
        refrence.downloadUrl
    }.addOnCompleteListener {
        if (it.isSuccessful){
            val firestore=FirebaseFirestore.getInstance()
            firestore.collection("docs")
                .add(Document(Random().nextInt().toString(),it.result.toString(),newName))
                .addOnSuccessListener {
                    getDocs()
                }.addOnFailureListener {
                    binding.progressBar.hide()
                }
        }
    }



}
        getDocs();
        return binding.root
    }

    //Gets a list of all the files.
    private fun getDocs() {
        val list= mutableListOf<Document>()
        FirebaseFirestore.getInstance()
                .collection("docs")
                .get()
                .addOnSuccessListener {
list.addAll(it.toObjects(Document::class.java))
                    val adapter=DocsAdapter(list, object :ClickListener {
                        override fun onClick(document: Document?) {
                           viewDocument(document)
                        }
                    })
                    binding.recycler.adapter=adapter;
                    binding.progressBar.hide()
                }
    }

    private fun viewDocument(document: Document?) {
        //A file has been selected. Navigate to the viewer activity
        Static.document=document;
navHostFragment.navController.navigate(DisplayFragmentDirections.actionDisplayFragmentToViewerActivity())
    }


}