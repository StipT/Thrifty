package hr.stipanic.tomislav.thrifty.ui.profile_screen.edit_screen

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
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.databinding.FragmentEditBinding
import hr.stipanic.tomislav.thrifty.utils.*
import hr.stipanic.tomislav.thrifty.utils.Constants.ARTICLE_ID_EXTRA
import hr.stipanic.tomislav.thrifty.viewmodels.EditViewModel
import kotlinx.android.synthetic.main.fragment_edit.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class EditFragment : Fragment() {

    private val viewModel by viewModel<EditViewModel>()
    private lateinit var navController: NavController
    private lateinit var articleId: String
    private lateinit var userId: String
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var binding: FragmentEditBinding
    private var isNewPhotoSelected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        articleId = requireArguments().getString(ARTICLE_ID_EXTRA)!!

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = findNavController()
        editFragmentToolbar.navigationIcon = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_baseline_arrow_back_24,
            null
        )
        loadingDialog = LoadingDialog()
        subscribeObservers()
        editDeleteButton.setOnClickListener { onDeleteClick() }
        editButton.setOnClickListener { onEditClick() }
        editFragmentHeaderImage.setOnClickListener { onLocate() }
        editFragmentToolbar.setNavigationOnClickListener { navController.popBackStack() }
    }

    private fun onEditClick() {
        val title: String = editFragmentTitle.text.toString()
        val price: Float = editFragmentPrice.text.toString().toFloat()
        val desc: String = editFragmentDescription.text.toString()

        viewModel.updateArticle(title, price, desc, isNewPhotoSelected)
    }

    private fun onDeleteClick() {
        DeleteDialog.newInstance(object : BooleanListener {
            override fun onYes() {
                viewModel.deleteArticle(articleId)
            }

        }).show(childFragmentManager, "deleteFragment")
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun subscribeObservers() {
        viewModel.observeDelete().observe(viewLifecycleOwner,
            Observer<Any?> {
                if (it != null) {
                    when (it) {
                        State.LOADING -> loadingDialog.show(
                            childFragmentManager,
                            "loadingDialog"
                        )
                        State.SUCCESS -> {
                            loadingDialog.dismiss()
                            showSnackBar("Delete Successful!")
                            navController.popBackStack()
                        }
                        State.ERROR -> {
                            loadingDialog.dismiss()
                            showSnackBar("Delete Failed!")
                        }
                    }
                }
            })

        viewModel.observeUpdate().observe(viewLifecycleOwner,
            Observer<Any?> {
                if (it != null) {
                    when (it) {
                        State.LOADING -> loadingDialog.show(
                            childFragmentManager,
                            "loadingDialog"
                        )
                        State.SUCCESS -> {
                            loadingDialog.dismiss()
                            showSnackBar("Update Successful!")
                            navController.popBackStack()
                        }
                        State.ERROR -> {
                            loadingDialog.dismiss()
                            showSnackBar("Update Failed!")
                        }
                    }
                }
            })
    }

    private fun onLocate() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            Constants.PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            selectedPhotoUri
                        )
                        editFragmentHeaderImage.setImageBitmap(bitmap)
                        isNewPhotoSelected = true
                        compressImage(bitmap)
                    } else {
                        val source = ImageDecoder.createSource(
                            requireActivity().contentResolver,
                            selectedPhotoUri
                        )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        editFragmentHeaderImage.setImageBitmap(bitmap)
                        isNewPhotoSelected = true
                        compressImage(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun compressImage(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
        val imageData = baos.toByteArray()
        viewModel.setImage(imageData)
    }

    private fun observeData() {
        viewModel.getArticle(articleId).removeObservers(this)
        viewModel.getArticle(articleId).observe(viewLifecycleOwner, Observer { task ->
            run {
                binding.article = task
                viewModel.getUser(task.userId).removeObservers(this)
                viewModel.getUser(task.userId).observe(viewLifecycleOwner, Observer { task ->
                    run {
                        userId = task.userId
                        binding.user = task
                        binding.stringUtils = StringUtils
                    }
                })
            }
        })
    }
}