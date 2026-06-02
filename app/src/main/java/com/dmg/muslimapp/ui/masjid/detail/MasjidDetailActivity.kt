package com.dmg.muslimapp.ui.masjid.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.ui.masjid.MasjidDirectionsActivity
import com.dmg.muslimapp.ui.masjid.favorit.MasjidListFavoritActivity
import com.dmg.muslimapp.utils.CommonUtils
import android.graphics.Bitmap
import android.widget.LinearLayout
import com.dmg.muslimapp.utils.ScreenshotUtils
import com.dmg.muslimapp.utils.ToastMessage


class MasjidDetailActivity: BaseActivity(){

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.rootContent)
    lateinit var rootContent: LinearLayout

    @BindView(R.id.layout_img)
    lateinit var mLayoutImg: LinearLayout

    @BindView(R.id.layout_distance)
    lateinit var mLayoutDistance: LinearLayout

    @BindView(R.id.tv_title)
    lateinit var mTvTitle: TextView

    @BindView(R.id.tv_subtitle)
    lateinit var mTvSubtitle: TextView

    @BindView(R.id.tv_distance)
    lateinit var mTvDistance: TextView

    private lateinit var objData: MasjidList

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidDetailActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_list_detail)
        ButterKnife.bind(this)

        setup()
    }

    fun setup(){
        setToolbar(mToolbar)
        showBackButton(true)

        objData = intent.getSerializableExtra("obj") as MasjidList

        mTvTitle.text = objData.nama_tempat
        mTvSubtitle.text = objData.alamat

        var distance = 0.00;
        var satuan = "km";
        if(objData.jarak < 1.00){
            distance = objData.jarak * 1000
            satuan = "meter";
        }else{
            distance = objData.jarak
        }

        mTvDistance.text = CommonUtils.decimal2angka(distance) + " " + satuan + " " + "dari lokasi anda."
    }

    private fun takeScreenshot(id: String){
        var b: Bitmap? = null

        //mLayoutDistance.visibility = View.INVISIBLE //set the visibility to INVISIBLE of first button
        //mLayoutImg.visibility = View.INVISIBLE
        //mToolbar.visibility = View.INVISIBLE

        b = ScreenshotUtils.getScreenShot(rootContent)

        if (b != null) {
            //showScreenShotImage(b);//show bitmap over imageview

            val saveFile = ScreenshotUtils.getMainDirectoryName(this);//get the path to save screenshot
            val file = ScreenshotUtils.store(b, "screenshot" + "masjid-01" + ".jpg", saveFile);//save the screenshot to selected path
            val text = "http://muslimapp.id/masjid/?id=$id"
            ScreenshotUtils.shareScreenshot(this, file, text)//finally share screenshot
        } else {
        //If bitmap is null show toast message
            ToastMessage("Failed Save Image after ScreenShot", this)

        }
    }

    @OnClick(R.id.img_tracking)
    fun openTracking(){
        val intent = MasjidDirectionsActivity.getStartIntent(this)
        intent.putExtra("obj", objData)
        startActivity(intent)
    }

    @OnClick(R.id.img_favorit)
    fun openFavorit(){
        val intent = MasjidListFavoritActivity.getStartIntent(this)
        startActivity(intent)
    }

    @OnClick(R.id.img_share)
    fun onShare(){
        takeScreenshot(objData.id)
    }
}