package com.dmg.muslimapp.ui.masjid.nearby.list

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmg.muslimapp.R
import com.dmg.muslimapp.utils.ToastMessage
import butterknife.*
import com.dmg.muslimapp.data.network.model.masjid.MasjidNearbyList
import com.dmg.muslimapp.ui.custom.RoundedBottomSheetDialogFragment
import com.dmg.muslimapp.ui.masjid.nearby.MasjidNearbyScreenActivity


class MasjidNearbyListFragment: RoundedBottomSheetDialogFragment(), MasjidNearbyListView {
    internal lateinit var unbinder: Unbinder

    //internal lateinit var context: Context
    internal lateinit var view: View

    private lateinit var mPresenter: MasjidNearbyListPresenter

    private var mAdapter: MasjidNearbyListAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.masjid_nearby_list, container, false)
        unbinder = ButterKnife.bind(this, view)

        //context = (activity as MasjidHomeScreenActivity?)!!

        //showLayoutServiceMenu(context, mRecyclerViewOne)

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        mPresenter = MasjidNearbyListPresenter(this)
        mPresenter.getListMasjid()

        return view
    }

    override fun showPerson(list: MutableList<MasjidNearbyList>){
        mAdapter = MasjidNearbyListAdapter(requireContext(), list)

        linearLayoutManager = LinearLayoutManager(context)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
        (recyclerView as RecyclerView).adapter = mAdapter

        mAdapter?.setOnClickListener(object : MasjidNearbyListAdapter.OnClickListener {
            override fun onInvite(view: View, obj: MasjidNearbyList, pos: Int) {
                (context as MasjidNearbyScreenActivity).openDialogInvite(obj)
            }
        })
    }

    override fun showMasjidDetail(msg: String){
        ToastMessage(msg, this.context!!)
        //(context as MasjidHomeScreenActivity).openDetail()
    }

    @OnClick(R.id.img_close)
    fun onClose(){
        (context as MasjidNearbyScreenActivity).hideBottomSheet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }
}