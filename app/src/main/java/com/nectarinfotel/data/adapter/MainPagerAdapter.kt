package com.nectarinfotel.data.adapter



import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.nectarinfotel.R

/** @author Aidan Follestad (afollestad) */
class MainPagerAdapter : PagerAdapter() {

    override fun instantiateItem(
        collection: ViewGroup,
        position: Int
    ): Any {
        var resId = 0
        when (position) {

            0 -> resId = R.id.first_layout
            1 -> resId = R.id.second_layout
            2 -> resId = R.id.third_layout
        }
        return collection.findViewById(resId)
    }

    override fun isViewFromObject(
        arg0: View,
        arg1: Any
    ) = arg0 === arg1 as View

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "One"
        1 -> "Two"
        2 -> "Two"
        else -> null
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        arg1: Any
    ) = Unit
}