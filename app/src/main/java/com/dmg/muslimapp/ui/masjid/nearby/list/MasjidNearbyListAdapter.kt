package com.dmg.muslimapp.ui.masjid.nearby.list

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseViewHolder

import butterknife.BindView
import butterknife.ButterKnife
import com.dmg.muslimapp.data.network.model.masjid.MasjidNearbyList
import java.util.*

class MasjidNearbyListAdapter(private val mContext: Context, val dataList: MutableList<MasjidNearbyList>) : RecyclerView.Adapter<BaseViewHolder>() {

    private var arraylist = ArrayList<MasjidNearbyList>(dataList)

    private var mOnClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.masjid_nearby_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        @BindView(R.id.tv_title)
        lateinit var mTvTitle: TextView

        @BindView(R.id.btn_ajak)
        lateinit var mBtnAjak: Button

        private lateinit var list: MasjidNearbyList

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBind(position: Int) {
            super.onBind(position)

            list = dataList.get(position)

            mTvTitle.text = list.nama


            mBtnAjak.setOnClickListener(View.OnClickListener { v ->
                if (mOnClickListener == null) return@OnClickListener
                mOnClickListener!!.onInvite(v, list, position)
            })
        }

        override fun clear() {

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnClickListener {
        fun onInvite(view: View, obj: MasjidNearbyList, pos: Int)
    }
}