package hr.stipanic.tomislav.thrifty.ui.feed_screen

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.os.bundleOf
import hr.stipanic.tomislav.thrifty.R
import hr.stipanic.tomislav.thrifty.ui.base_fragment.BaseBottomTabFragment
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_CARS
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_CLOTHES
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_ELECTRONICS
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_EXTRA
import hr.stipanic.tomislav.thrifty.utils.Constants.CATEGORY_OTHER
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseBottomTabFragment(), View.OnClickListener, View.OnTouchListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClick()
        setOnTouch()
    }

    private fun setOnClick() {
        imageCars.setOnClickListener(this)
        categoryTitleCars.setOnClickListener(this)

        imageClothes.setOnClickListener(this)
        categoryTitleClothes.setOnClickListener(this)

        imageElectronics.setOnClickListener(this)
        categoryTitleElectronics.setOnClickListener(this)

        imageOther.setOnClickListener(this)
        categoryTitleOther.setOnClickListener(this)
    }

    private fun setOnTouch() {
        imageCars.setOnTouchListener(this)
        imageClothes.setOnTouchListener(this)
        imageElectronics.setOnTouchListener(this)
        imageOther.setOnTouchListener(this)
    }

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        when (p1?.action) {
            MotionEvent.ACTION_DOWN -> {
                val view = p0 as ImageView
                view.drawable.colorFilter =
                    BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                        0x77000000,
                        BlendModeCompat.SRC_ATOP
                    )
                view.invalidate();
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                val view = p0 as ImageView
                view.drawable.clearColorFilter()
                view.invalidate()
            }
        }
        return false
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            imageCars.id, categoryTitleCars.id -> navigateWithBundle(
                R.id.action_categoryFragment_to_feedListFragment,
                bundleOf(Pair(CATEGORY_EXTRA, CATEGORY_CARS))
            )
            imageClothes.id, categoryTitleClothes.id -> navigateWithBundle(
                R.id.action_categoryFragment_to_feedListFragment,
                bundleOf(Pair(CATEGORY_EXTRA, CATEGORY_CLOTHES))
            )
            imageElectronics.id, categoryTitleElectronics.id -> navigateWithBundle(
                R.id.action_categoryFragment_to_feedListFragment,
                bundleOf(Pair(CATEGORY_EXTRA, CATEGORY_ELECTRONICS))
            )
            imageOther.id, categoryTitleOther.id -> navigateWithBundle(
                R.id.action_categoryFragment_to_feedListFragment,
                bundleOf(Pair(CATEGORY_EXTRA, CATEGORY_OTHER))
            )

        }
    }
}