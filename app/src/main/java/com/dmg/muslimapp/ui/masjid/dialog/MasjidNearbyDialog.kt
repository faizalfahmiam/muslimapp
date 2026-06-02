package com.dmg.muslimapp.ui.masjid.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidNearbyList
import com.dmg.muslimapp.ui.base.BaseDialog
import com.dmg.muslimapp.ui.masjid.nearby.MasjidNearbyScreenActivity

class MasjidNearbyDialog: BaseDialog(){
    private val TAG = "NearbyDialog"
    internal lateinit var unbinder: Unbinder
    internal lateinit var view: View

    companion object {
        fun newInstance(): MasjidNearbyDialog {
            val fragment = MasjidNearbyDialog()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.masjid_nearby_dialog, container, false)

        //setUnBinder(ButterKnife.bind(this, view))

        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun setUp(view: View) {

    }

    private fun dismissDialog() {
        super.dismissDialog(TAG)
    }

    fun showAdd(activity: MasjidNearbyScreenActivity, obj: MasjidNearbyList) {
        super.show(activity.supportFragmentManager, TAG)
    }

    @OnClick(R.id.btn_ok)
    fun onOK(){
        showLoading()
    }

    @OnClick(R.id.btn_cancel)
    fun onCancel(){
        hideLoading()
        dismissDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

}