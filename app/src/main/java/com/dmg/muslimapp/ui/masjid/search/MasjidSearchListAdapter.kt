package com.dmg.muslimapp.ui.masjid.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.dmg.muslimapp.R
import com.dmg.muslimapp.ui.base.BaseViewHolder

import butterknife.BindView
import butterknife.ButterKnife
import com.dmg.muslimapp.data.network.model.masjid.MasjidList
import java.util.*

class MasjidSearchListAdapter(private val mContext: Context, val dataList: MutableList<MasjidList>) : RecyclerView.Adapter<BaseViewHolder>() {

    private var arraylist = ArrayList<MasjidList>(dataList)

    private var mOnClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.masjid_search_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        @BindView(R.id.layout_main)
        lateinit var mLayoutMain: LinearLayout

        @BindView(R.id.tv_title)
        lateinit var mTvTitle: TextView

        private lateinit var list: MasjidList

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBind(position: Int) {
            super.onBind(position)

            list = dataList.get(position)

            Log.e("data", list.nama_tempat)

            mTvTitle.text = list.nama_tempat

            mLayoutMain.setOnClickListener(View.OnClickListener { v ->
                if (mOnClickListener == null) return@OnClickListener
                mOnClickListener!!.onDetail(v, list, position)
            })
        }

        override fun clear() {

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    interface OnClickListener {
        fun onDetail(view: View, obj: MasjidList, pos: Int)
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        dataList.clear()
        if (charText.isEmpty()) {
            dataList.addAll(arraylist)
        } else {
            for (wp in arraylist) {
                if (wp.nama_tempat.toLowerCase(Locale.getDefault()).contains(charText)) {
                    dataList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }
}