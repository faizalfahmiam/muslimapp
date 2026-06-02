package com.dmg.muslimapp.ui.masjid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.home.MasjidHomeScreenActivity
import kotlinx.android.synthetic.main.masjid_on_boarding.*

class MasjidOnBoardingActivity: BaseActivity(){
    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private var boardShow: Int = 0

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidOnBoardingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_on_boarding)
        ButterKnife.bind(this)
        setup()
    }

    fun setup(){
        setToolbar(mToolbar)
        showBackButton(true)

        boardShow = 1
    }

    @OnClick(R.id.btn_next)
    fun onNext(){
        when (boardShow) {
            1 -> {
                layout_board_1.visibility = View.GONE
                layout_board_2.visibility = View.VISIBLE
                layout_board_gps_warning.visibility = View.GONE
                boardShow = 2
            }
            2 -> {
                layout_board_1.visibility = View.GONE
                layout_board_2.visibility = View.GONE
                layout_board_gps_warning.visibility = View.VISIBLE
                boardShow = 3
            }
            3 -> {
                layout_board_1.visibility = View.VISIBLE
                layout_board_2.visibility = View.GONE
                layout_board_gps_warning.visibility = View.GONE
                //boardShow = 1

                val intent = MasjidHomeScreenActivity.getStartIntent(this)
                startActivity(intent)
                finish()
            }
        }
    }
}