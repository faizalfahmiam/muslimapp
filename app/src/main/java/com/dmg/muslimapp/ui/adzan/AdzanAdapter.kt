package com.dmg.muslimapp.ui.adzan

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.model.Adzan
import com.dmg.muslimapp.utils.AdzanSoundStore
import com.suke.widget.SwitchButton

import java.util.ArrayList

import butterknife.BindView
import butterknife.ButterKnife
import com.dmg.muslimapp.utils.DateUtils

class AdzanAdapter(private val adzansList: MutableList<Adzan>, internal var context: Context, internal var mItemClickListener: OnItemClickListener, internal var ShowLayoutFormat: String) : RecyclerView.Adapter<AdzanAdapter.MyViewHolder>() {
    private var isToday = true
    internal var drawables_new = intArrayOf(R.drawable.imsak_box, R.drawable.subuh_box, R.drawable.terbit_box, R.drawable.dzuhur_box, R.drawable.ashar_box, R.drawable.magrib_box, R.drawable.isya_box)

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.tv_tms)
        internal lateinit var tdmgms: TextView

        @BindView(R.id.lin_delete_prayer)
        internal lateinit var linDeletePrayer: LinearLayout

        internal var tdmgitle: TextView
        internal var tdmgime: TextView
        internal var tvSubtitle: TextView
        internal var tvSound: TextView
        internal var layoutDetail: LinearLayout
        internal var layoutMain: LinearLayout
        internal lateinit var layoutBox: RelativeLayout
        internal var imgBel: ImageView
        internal var imgDel: ImageView
        //Switch sMute;
        internal var sMute: SwitchButton
        private val adzan: Adzan? = null

        init {
            ButterKnife.bind(this, view)

            tdmgime = view.findViewById(R.id.tv_time)
            tdmgitle = view.findViewById(R.id.tv_title)
            tvSubtitle = view.findViewById(R.id.tv_subtitle)
            layoutDetail = view.findViewById(R.id.layout_detail)
            layoutMain = view.findViewById(R.id.layout_main)
            imgBel = view.findViewById(R.id.img_bel)
            imgDel = view.findViewById(R.id.img_del)
            tvSound = view.findViewById(R.id.tv_sound)
            sMute = view.findViewById(R.id.switch1)

            if (ShowLayoutFormat == "grid") {
                layoutBox = view.findViewById(R.id.layout_box)
            }

            tdmgms!!.text = "+0"


            if (ShowLayoutFormat == "list") {
                imgDel.setOnClickListener { v ->
                    if (mItemClickListener != null) {
                        mItemClickListener!!.onDeletePrayer(adapterPosition, adzansList[adapterPosition])
                    }
                }
            }
        }
    }

    fun setToday(today: Boolean) {
        isToday = today
    }

    fun addAdzan(adzan: Adzan) {
        adzansList.add(adzan)

        val customAdzans = ArrayList<Adzan>()

        for (adzan1 in adzansList) {
            if (adzan1.custom) {
                customAdzans.add(adzan1)
            }
        }

        AdzanSoundStore.getInstance(context)!!.setCustomAdzan(customAdzans)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var itemView: View? = null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.adzan_item_view_vertical, parent, false)
        if (ShowLayoutFormat == "grid") {
            itemView = LayoutInflater.from(parent.context).inflate(R.layout.adzan_item_view_horizontal, parent, false)
        }

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val adzan = adzansList[position]
        var adzanName = adzan.name
        adzan.notif = AdzanSoundStore.getSetting(adzanName!!)

        if (adzan.name == "Fajr") {
            adzanName = context.getString(R.string.fajr)
        } else if (adzan.name == "Sunrise") {
            adzanName = context.getString(R.string.sunrise)
        } else if (adzan.name == "Dhuhr") {
            adzanName = context.getString(R.string.dhuhr)
        } else if (adzan.name == "Asr") {
            adzanName = context.getString(R.string.asr)
        } else if (adzan.name == "Maghrib") {
            adzanName = context.getString(R.string.maghrib)
        } else if (adzan.name == "Sunset") {
            adzanName = context.getString(R.string.sunset)
        } else if (adzan.name == "Isha") {
            adzanName = context.getString(R.string.isha)
        }

        Log.e("List Adzan", adzan.time)

        holder.tdmgitle.text = adzanName
        holder.tdmgime.text = adzan.time
        holder.tvSound.text = context.getString(R.string.adzan_nabawi)

        when (adzan.notif) {
            AdzanSoundStore.Companion.NotifType.NON_ACTIVE, AdzanSoundStore.Companion.NotifType.SOUND_OFF -> holder.imgBel.setImageResource(R.drawable.ic_bel_ring_off)
            AdzanSoundStore.Companion.NotifType.SOUND -> holder.imgBel.setImageResource(R.drawable.ic_bel_ring_on)
            AdzanSoundStore.Companion.NotifType.VIBRATE -> holder.imgBel.setImageResource(R.drawable.ic_bel_ring_vibrate)
        }

        if (adzan.notif == AdzanSoundStore.Companion.NotifType.NON_ACTIVE || adzan.notif == AdzanSoundStore.Companion.NotifType.VIBRATE) {
            holder.sMute.isChecked = false
        } else if (adzan.notif == AdzanSoundStore.Companion.NotifType.SOUND) {
            holder.sMute.isChecked = true
        }

        holder.layoutDetail.visibility = View.GONE
        holder.layoutMain.setOnClickListener { view ->
            if (mItemClickListener != null) {
                mItemClickListener!!.onSettingAlarm(adzan, position, true)
            }
        }

        val timeLeft = DateUtils.getTimeleft(adzansList[position].time!!)
        //String timeLeft = DateUtils.getTimeleft("23:00");
        if (isToday && !TextUtils.isEmpty(timeLeft)) {
            val times = timeLeft.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val hour = Integer.parseInt(times[0])
            val minute = Integer.parseInt(times[1])
            //holder.tvSubtitle.setVisibility(View.VISIBLE);
            if (hour > 0) {
                holder.tvSubtitle.text = context.getString(R.string.time_remaining_hour, hour, minute)
            } else {
                holder.tvSubtitle.text = context.getString(R.string.time_remaining_minute, minute)
            }
        } else {
            holder.tvSubtitle.text = ""
            //holder.tvSubtitle.setVisibility(View.GONE);
        }

        if (ShowLayoutFormat == "grid") {
            if (adzan.name == "Fajr") {
                holder.layoutBox.setBackgroundResource(drawables_new[1])
            } else if (adzan.name == "Dhuhr") {
                holder.layoutBox.setBackgroundResource(drawables_new[3])
            } else if (adzan.name == "Asr") {
                holder.layoutBox.setBackgroundResource(drawables_new[4])
            } else if (adzan.name == "Maghrib") {
                holder.layoutBox.setBackgroundResource(drawables_new[5])
            } else if (adzan.name == "Isha") {
                holder.layoutBox.setBackgroundResource(drawables_new[6])
            }

            if (adzan.custom) {
                holder.layoutBox.setBackgroundResource(R.drawable.terbit_box)
            }
        } else {
            if (adzan.custom) {
                holder.layoutMain.setBackgroundResource(R.drawable.background_item_view_custom_rounded)
                holder.tdmgime.setBackgroundResource(0)
                holder.imgDel.visibility = View.VISIBLE
            } else {
                holder.layoutMain.setBackgroundResource(R.drawable.background_item_view_white_rounded)
                holder.tdmgime.setBackgroundResource(R.drawable.background_item_view_text_blusky_rounded_left)
                holder.imgDel.visibility = View.GONE
            }
        }


        holder.sMute.setOnCheckedChangeListener { view1, isChecked ->
            if (isChecked) {
                adzan.notif = AdzanSoundStore.Companion.NotifType.SOUND
            } else {
                if (adzan.notif != AdzanSoundStore.Companion.NotifType.VIBRATE) {
                    adzan.notif = AdzanSoundStore.Companion.NotifType.SOUND_OFF
                }
            }

            //            DbHandler.config(DbHandler.adzanConfig).updateAdzan(adzan);

            if (isChecked) {
                holder.imgBel.setImageResource(R.drawable.ic_bel_ring_on)
                AdzanSoundStore.addSetting(adzan.name!!, AdzanSoundStore.Companion.NotifType.SOUND)
            } else {
                if (adzan.notif == AdzanSoundStore.Companion.NotifType.VIBRATE) {
                    holder.imgBel.setImageResource(R.drawable.ic_bel_ring_vibrate)
                    AdzanSoundStore.addSetting(adzan.name!!, AdzanSoundStore.Companion.NotifType.VIBRATE)
                } else {
                    holder.imgBel.setImageResource(R.drawable.ic_bel_ring_off)
                    AdzanSoundStore.addSetting(adzan.name!!, AdzanSoundStore.Companion.NotifType.NON_ACTIVE)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return adzansList.size
    }

    interface OnItemClickListener {
        fun onDeletePrayer(position: Int, adzan: Adzan)
        fun onUpdateAlarm(name: String, position: Int, sound: Boolean?)
        fun onSettingAlarm(adzan: Adzan, position: Int, sound: Boolean?)
    }
}
