package com.dmg.muslimapp.ui.masjid.favorit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.dmg.muslimapp.ui.base.BaseActivity
import android.widget.RelativeLayout
import butterknife.OnClick
import com.dmg.muslimapp.utils.ToastMessage
import butterknife.OnTextChanged




class MasjidListFavoritActivity: BaseActivity(), MasjidListFavoritView{
    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.layout_bottom)
    lateinit var mLayoutBottom: RelativeLayout

    @BindView(R.id.tv_search)
    lateinit var mTvSearch: EditText

    private lateinit var mPresenter: MasjidListFavoritPresenter

    private var mAdapter: MasjidListFavoritAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    @BindView(R.id.recyclerView)
    lateinit var mRecycleView: RecyclerView

    private lateinit var mList: MutableList<MasjidList>

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MasjidListFavoritActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.masjid_list_favorit)
        ButterKnife.bind(this)

        setup()
    }

    fun setup(){
        setToolbar(mToolbar)
        showBackButton(true)

        mPresenter = MasjidListFavoritPresenter(this)
        mPresenter.getListMasjid()
    }

    override fun showMasjid(list: MutableList<MasjidList>){
        mList = list

        mAdapter = MasjidListFavoritAdapter(applicationContext, list)

        linearLayoutManager = LinearLayoutManager(this)
        mRecycleView.setHasFixedSize(true);
        mRecycleView.layoutManager = linearLayoutManager
        mRecycleView.adapter = mAdapter

        mAdapter?.setOnClickListener(object : MasjidListFavoritAdapter.OnClickListener {

            override fun onCheckboxClick(view: View, obj: MasjidList, pos: Int) {
                mAdapter?.toggleSelection(pos)
            }
        })
    }

    @OnClick(R.id.tv_delete_show)
    fun onDeleteShow(){
        if(mAdapter?.itemCount!! > 0) {
            bootom()
        }else{
            ToastMessage("Masjid favorit tidak tersedia.", this)
        }
    }

    private fun bootom(){
        mLayoutBottom.visibility = View.VISIBLE

        for (model in mList) {
            model.visible = true
        }
        mAdapter?.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_delete)
    fun onDelete(){
        if(mAdapter?.getSelectedItemCount()!! > 0) {
            deleteInboxes()
            mAdapter?.clearSelections()
        }else{
            ToastMessage("Pilih masjid favorit yang akan di hapus.", this)
        }
    }

    private fun deleteInboxes() {
        val selectedItemPositions = mAdapter?.getSelectedItems()
        for (i in selectedItemPositions!!.indices.reversed()) {
            mAdapter?.removeData(selectedItemPositions[i])
        }

        for (model in mList) {
            model.visible = false
        }

        mLayoutBottom.visibility = View.GONE
        mAdapter?.notifyDataSetChanged()
    }

    @OnClick(R.id.tv_cancel)
    fun onCancel(){
        mAdapter?.clearSelections()

        for (model in mList) {
            model.visible = false
        }

        mLayoutBottom.visibility = View.GONE
        mAdapter?.notifyDataSetChanged();
    }

    /*@OnTextChanged(value = R.id.tv_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun nameChanged(text: CharSequence) {
        //do stuff
        Log.e("Serach",text.toString())
        mAdapter?.filter(text.toString())
    }*/

    @OnClick(R.id.img_clear_search)
    fun onClearSearch(){
        mTvSearch.text = null
        mAdapter?.filter("")
    }
}