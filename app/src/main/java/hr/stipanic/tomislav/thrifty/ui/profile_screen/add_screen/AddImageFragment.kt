package hr.stipanic.tomislav.thrifty.ui.profile_screen.add_screen

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.data.model.Article
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.CITY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.COUNTRY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.DESC_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.LAT_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.LNG_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.PICK_IMAGE_REQUEST
import hr.stipanic.tomislav.thrifty.utils.Constants.PRICE_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.TITLE_EXTRA
import hr.stipanic.tomislav.thrifty.utils.LoadingDialog
import hr.stipanic.tomislav.thrifty.utils.State
import hr.stipanic.tomislav.thrifty.utils.showSnackBar
import hr.stipanic.tomislav.thrifty.viewmodels.AddViewModel
import kotlinx.android.synthetic.main.fragment_add_image.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.util.*

class AddImageFragment : Fragment() {

    private val viewModel by viewModel<AddViewModel>()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        loadingDialog = LoadingDialog()
        locateButton.setOnClickListener { onLocate() }
        addImageUploadButton.setOnClickListener { onUpload() }
        addImageBackButton.setOnClickListener { navController.popBackStack() }
        subscribeObservers()
    }


    private fun onUpload() {
        val category = requireArguments().getString(CATEGORY_EXTRA)
        val title = requireArguments().getString(TITLE_EXTRA)
        val desc = requireArguments().getString(DESC_EXTRA)
        val price = requireArguments().getFloat(PRICE_EXTRA)
        val lat = requireArguments().getFloat(LAT_EXTRA)
        val lng = requireArguments().getFloat(LNG_EXTRA)
        val country = requireArguments().getString(COUNTRY_EXTRA)
        val city = requireArguments().getString(CITY_EXTRA)

        viewModel.uploadArticle(
            Article(
                category = category!!,
                title = title!!,
                desc = desc!!,
                price = price,
                lat = lat,
                lng = lng,
                country = country!!,
                city = city!!,
                date = Date().time
            )
        )
    }

    private fun onLocate() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            selectedPhotoUri
                        )
                        addImageImage.setImageBitmap(bitmap)
                        compressImage(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            selectedPhotoUri
                        )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        addImageImage.setImageBitmap(bitmap)
                        compressImage(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        addImageUploadButton.isEnabled = true
    }

    private fun compressImage(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val imageData = baos.toByteArray()
        viewModel.setImage(imageData)
    }

    private fun subscribeObservers() {
        viewModel.observeInsert().observe(viewLifecycleOwner, Observer<State> {
            if (it != null) {
                when (it) {
                    State.LOADING -> loadingDialog.show(
                        childFragmentManager,
                        "loadingDialog"
                    )
                    State.SUCCESS -> {
                        loadingDialog.dismiss()
                        showSnackBar("Article added Successfully!")
                        goToProfileFragment()
                    }
                    State.ERROR -> {
                        loadingDialog.dismiss()
                        showSnackBar("Something went wrong with inserting new article")
                    }
                }
            }

        })
    }



    private fun goToProfileFragment() =
        navController.navigate(R.id.action_addImageFragment_to_profileFragment)
}