package com.dmg.muslimapp.utils

import android.content.Context
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.widget.NestedScrollView
import android.view.View
import android.widget.LinearLayout
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.quran.QuranActivity

class BottomBarUtils {
    constructor(context: Context, bottomSheetLayout: LinearLayout, bottomNavigation: BottomNavigationView, nestedScroll: NestedScrollView) {
        context.setupBottomBar(bottomSheetLayout = bottomSheetLayout, bottomNavigation = bottomNavigation, nestedScroll = nestedScroll)
    }

    fun Context.setupBottomBar(context: Context = this, bottomSheetLayout: LinearLayout, bottomNavigation: BottomNavigationView, nestedScroll: NestedScrollView) {
        BottomSheetBehavior.from(bottomSheetLayout).state = BottomSheetBehavior.STATE_COLLAPSED
        if (context is QuranActivity) {
            ToastMessage(context.toString(), context)
            nestedScroll.visibility = View.VISIBLE
        }
        //bottomNavigation.visibility = View.VISIBLE
        //bottomNavigation.animate().translationY(0f).alpha(1.0f)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.walk -> {
                    ToastMessage("walk", context)
                }
                R.id.bike -> {
                    ToastMessage("bike", context)
                }
                R.id.bus -> {
                    ToastMessage("bus", context)
                }
                R.id.car -> {
                    ToastMessage("car", context)
                }
            }
            true
        }
    }
}