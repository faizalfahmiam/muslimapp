package com.dmg.muslimapp.ui.masjid.list

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import com.dmg.muslimapp.ui.masjid.home.MasjidHomeScreenActivity
import com.dmg.muslimapp.utils.ToastMessage
import android.widget.EditText
import butterknife.*


class MasjidListFragment: BottomSheetDialogFragment(), MasjidListView {
    internal lateinit var unbinder: Unbinder

    //internal lateinit var context: Context
    internal lateinit var view: View

    private lateinit var mPresenter: MasjidListPresenter

    @BindView(R.id.tv_search)
    lateinit var mTvSearch: EditText

    //private var mAdapter: MasjidListAdapter? = null
    //private var mLayoutManager: LinearLayoutManager? = null

    //@BindView(R.id.recyclerView)
    //lateinit var mRecyclerView: RecyclerView

    private var mAdapter: MasjidListAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.masjid_list, container, false)
        unbinder = ButterKnife.bind(this, view)

        //context = (activity as MasjidHomeScreenActivity?)!!

        //showLayoutServiceMenu(context, mRecyclerViewOne)

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        mPresenter = MasjidListPresenter(this)
        mPresenter.getListMasjid()

        return view
    }

    override fun showMasjid(list: MutableList<MasjidList>){
        mTvSearch.setText("")

        mAdapter = MasjidListAdapter(requireContext(), list)

        linearLayoutManager = LinearLayoutManager(context)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
        (recyclerView as RecyclerView).adapter = mAdapter

        mAdapter?.setOnClickListener(object : MasjidListAdapter.OnClickListener {
            override fun onDetail(view: View, obj: MasjidList, pos: Int) {
                (context as MasjidHomeScreenActivity).openDetail(obj)
            }

            override fun onTracking(view: View, obj: MasjidList, pos: Int) {
                (context as MasjidHomeScreenActivity).openTracking(obj)
            }
        })
    }

    override fun showMasjidDetail(msg: String){
        ToastMessage(msg, this.context!!)
        //(context as MasjidHomeScreenActivity).openDetail()
    }

    @OnClick(R.id.img_close)
    fun onClose(){
        (context as MasjidHomeScreenActivity).hideBottomSheet()
    }

    @OnClick(R.id.img_setting)
    fun onSetting(){
        (context as MasjidHomeScreenActivity).openSetting()
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

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}