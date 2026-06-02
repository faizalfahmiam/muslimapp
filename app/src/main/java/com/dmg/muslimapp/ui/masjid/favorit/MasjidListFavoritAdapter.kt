package com.dmg.muslimapp.ui.masjid.favorit

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
import com.dmg.muslimapp.utils.CommonUtils
import android.util.SparseBooleanArray
import kotlin.collections.ArrayList
import java.util.*


class MasjidListFavoritAdapter(private val mContext: Context, val dataList: MutableList<MasjidList>) : RecyclerView.Adapter<BaseViewHolder>() {
    private val selected_items = SparseBooleanArray()
    private var current_selected_idx = -1
    private var mOnClickListener: OnClickListener? = null

    private var arraylist = ArrayList<MasjidList>(dataList)

    fun setOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.masjid_list_item_favorit, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        @BindView(R.id.img_masjid)
        lateinit var mImgMasjid: ImageView

        @BindView(R.id.img_favorit)
        lateinit var mImgFavorit: ImageView

        @BindView(R.id.tv_no)
        lateinit var mTvNo: TextView

        @BindView(R.id.tv_title)
        lateinit var mTvTitle: TextView

        @BindView(R.id.tv_subtitle)
        lateinit var mTvSubtitle: TextView

        @BindView(R.id.tv_distance)
        lateinit var mTvDistance: TextView

        @BindView(R.id.cb_delete)
        lateinit var mCbDelete: CheckBox

        @BindView(R.id.layout_main)
        lateinit var mLayoutMain: LinearLayout

        private lateinit var list: MasjidList

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBind(position: Int) {
            super.onBind(position)

            list = dataList.get(position)

            mTvNo.text = list.id
            mTvTitle.text = list.nama_tempat
            mTvSubtitle.text = list.alamat

            if (list.visible) {
                mCbDelete.visibility = View.VISIBLE
            } else {
                mCbDelete.visibility = View.GONE
            }

            var distance = 0.00;
            var satuan = "km";
            if (list.jarak < 1.00) {
                distance = list.jarak * 1000
                satuan = "meter";
            } else {
                distance = list.jarak
            }

            val t_distance = CommonUtils.decimal2angka(distance) + " " + satuan + " dari lokasi anda."
            mTvDistance.text = t_distance

            //mCbDelete.text = position.toString()

            mCbDelete.setOnClickListener(View.OnClickListener { v ->
                if (mOnClickListener == null) return@OnClickListener
                mOnClickListener!!.onCheckboxClick(v, list, position)
            })

            toggleCheckedIcon(mCbDelete, position)
        }

        override fun clear() {

        }
    }

    private fun toggleCheckedIcon(cb_delete: CheckBox, position: Int) {
        if (selected_items.size() > 0) {
            if (selected_items.get(position, false)) {
                cb_delete.isChecked = false
                if (current_selected_idx == position) resetCurrentIndex()
            } else {
                cb_delete.isChecked = true
                if (current_selected_idx == position) resetCurrentIndex()
            }
        }else{
            cb_delete.isChecked = false
        }
    }

    fun toggleSelection(pos: Int) {
        current_selected_idx = pos
        if (selected_items.get(pos, false)) {
            selected_items.delete(pos)
            Log.e("delete", pos.toString())
        } else {
            selected_items.put(pos, true)
            Log.e("put", pos.toString())
        }

        //notifyItemChanged(pos)
    }

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>(selected_items.size())
        for (i in 0 until selected_items.size()) {
            items.add(selected_items.keyAt(i))
            Log.e("selected position", selected_items.keyAt(i).toString())
        }
        return items
    }

    fun removeData(position: Int) {
        dataList.removeAt(position)
        resetCurrentIndex()
    }

    fun clearSelections() {
        selected_items.clear()
        notifyDataSetChanged()
    }

    private fun resetCurrentIndex() {
        current_selected_idx = -1
    }

    fun getSelectedItemCount(): Int {
        return selected_items.size()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun getMasjidList(): MutableList<MasjidList> {
        return dataList
    }

    interface OnClickListener {
        fun onCheckboxClick(view: View, obj: MasjidList, pos: Int)
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
