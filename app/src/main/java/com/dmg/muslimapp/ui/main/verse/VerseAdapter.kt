package com.dmg.muslimapp.ui.main.verse

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.model.quran.Ayah
import com.dmg.muslimapp.ui.base.BaseViewHolder

import butterknife.BindView
import butterknife.ButterKnife

class VerseAdapter(private val mContext: Context, val ayahList: List<Ayah>, internal var mItemClickListener: OnItemClickListener) : RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed_verse, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        val tf = Typeface.createFromAsset(mContext.assets, "fonts/quran/LPMQ.ttf")

        @BindView(R.id.tv_ayah_translate)
        lateinit var mTvAyahTranslate: TextView

        @BindView(R.id.tv_ayah_arab)
        lateinit var mTvAyahArab: TextView

        private var ayah: Ayah? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBind(position: Int) {
            super.onBind(position)

            ayah = ayahList[position]

            mTvAyahArab.typeface = tf
            mTvAyahArab.text = ayah!!.AyahText
            mTvAyahTranslate.text = ayah!!.Translation
        }

        override fun clear() {

        }
    }

    override fun getItemCount(): Int {
        return ayahList.size
    }

    interface OnItemClickListener{
        fun onDetailVerse()
    }
}