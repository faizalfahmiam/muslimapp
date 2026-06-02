package com.dmg.muslimapp.ui.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.View
import butterknife.ButterKnife
import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseActivity
import kotlinx.android.synthetic.main.scanner_activity.*

class ScannerActivity : BaseActivity(), View.OnClickListener {

    //private lateinit var mScannerView: ZXingScannerView

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, ScannerActivity::class.java)
        }
    }

    @Nullable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner_activity)
        ButterKnife.bind(this)
        setup()
    }

    fun setup() {

       // mScannerView = ZXingScannerView(this);
        btn_generate.setOnClickListener(this);
        btn_scan.setOnClickListener(this);
    }

    override fun onClick(v: View) {

        if (v === btn_generate) {
            //val i = Intent(this, QRCodeActivity::class.java)
           // startActivity(i)
        }
        if (v === btn_scan) {
            ScanCode()
        }
    }

    fun ScanCode() {

        //mScannerView = ZXingScannerView(this)

        // Programmatically initialize the scanner view

      //  setContentView(mScannerView)

       // mScannerView.setResultHandler(this)

        // Register ourselves as a handler for scan results.

      //  mScannerView.startCamera()
    }


    override fun onPause() {
        super.onPause();
      //  mScannerView.stopCamera();
    }

    /*override fun handleResult(p0: Result?) {
        //Toast.makeText(MainActivity.this,"This is your Text"+result.getText(),Toast.LENGTH_SHORT);
        showMessage(p0.toString())
        Log.e("handler", p0.toString());

        // Prints scan results

        Log.e("handler", p0?.barcodeFormat.toString());

        // Prints the scan format (qrcode)
    }*/
}
