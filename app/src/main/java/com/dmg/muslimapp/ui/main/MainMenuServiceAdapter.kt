package com.dmg.muslimapp.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.db.model.service_menu.ServiceMenu
import com.dmg.muslimapp.ui.base.BaseViewHolder

import butterknife.BindView
import butterknife.ButterKnife


class MainMenuServiceAdapter(private val context: Context, private val listMenu: List<ServiceMenu>, private val param: Int) : RecyclerView.Adapter<BaseViewHolder>() {

    private fun openActivity(intent: Intent) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        this.context.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_menu_service, parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return listMenu.size
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        @BindView(R.id.main_menu)
        lateinit var mainMenu: LinearLayout

        @BindView(R.id.icon_menu)
        lateinit var iconMenu: ImageView

        @BindView(R.id.title_menu)
        lateinit var titleMenu: TextView

        @BindView(R.id.icon_menu_togel)
        lateinit var iconMenuTogel: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onBind(position: Int) {
            super.onBind(position)

            Log.e("@menu_list : ", position.toString() + "")

            val menuid = listMenu[position].menu_id
            val status = listMenu[position].menu_status

            var icon = 0
            var title = R.string.quran_title
            when (menuid) {
                "other" -> {
                    icon = R.drawable.lainnya_ic
                    title = R.string.more_title
                }
                "quran" -> {
                    icon = R.drawable.quran_ic
                    title = R.string.quran_title
                }
                "aqiqah" -> {
                    icon = R.drawable.aqiqah_ic
                    title = R.string.aqiqah_title
                }
                "qurban" -> {
                    icon = R.drawable.qurban_ic
                    title = R.string.qurban_title
                }
                "adzan" -> {
                    icon = R.drawable.adzan_ic
                    title = R.string.adzan_title
                }
                "kiblat" -> {
                    icon = R.drawable.qiblat_ic
                    title = R.string.kiblat_title
                }
                "masjid" -> {
                    icon = R.drawable.masjid_ic
                    title = R.string.masjid_title
                }
                "doa" -> {
                    icon = R.drawable.doa_ic
                    title = R.string.doa_title
                }
                "hadist" -> {
                    icon = R.drawable.hadist_ic
                    title = R.string.hadist_title
                }
                "livemekah" -> {
                    icon = R.drawable.live_mekah_ic
                    title = R.string.live_mecca_title
                }
                "gpstrack" -> {
                    icon = R.drawable.gps_tracking_ic
                    title = R.string.gps_tracking_title
                }
                "wedding" -> {
                    icon = R.drawable.wedding_ic
                    title = R.string.wedding_title
                }
                "khitanan" -> {
                    icon = R.drawable.khitan_ic
                    title = R.string.khitan_title
                }
                "tasbih" -> {
                    icon = R.drawable.tasbih_ic
                    title = R.string.tasbih_title
                }
                "bookstore" -> {
                    icon = R.drawable.gps_tracking_ic
                    title = R.string.bookstore_title
                }
                "donasi" -> {
                    icon = R.drawable.donasi_ic
                    title = R.string.donasi_title
                }
                "syahadat" -> {
                    icon = R.drawable.syahadat_ic
                    title = R.string.syahadat_title
                }

                "islamicproduct" -> {
                    icon = R.drawable.islamicproduct_ic
                    title = R.string.islamicproduct_title
                }
            }

            titleMenu.setText(title)
            iconMenu.setImageResource(icon)
            iconMenuTogel.visibility = View.GONE

            if (param == 0) {
                iconMenuTogel.visibility = View.GONE
            } else {
                if (param == 1) {
                    if (status == true) {
                        iconMenuTogel.setImageResource(R.drawable.ic_menu_togel_favourites)
                        iconMenuTogel.setColorFilter(ContextCompat.getColor(context,
                                R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
                        iconMenuTogel.visibility = View.VISIBLE
                    } else {
                        iconMenuTogel.visibility = View.GONE
                    }
                }
            }

            mainMenu.setOnClickListener {
                when (menuid) {
                    "other" -> (context as MainActivity).openLainnyaActivity()
                    "quran" -> (context as MainActivity).openQuranActivity()
                    //"aqiqah" -> (context as MainActivity).openAqiqahDialog()
                    //"qurban" -> (context as MainActivity).openQurbanActivity()
                    "adzan" -> (context as MainActivity).openAdzanActivity()
                    //"kiblat" -> (context as MainActivity).openQiblaActivity()
                    "masjid" -> (context as MainActivity).openMasjidOnBoardingActivity()
                    //"doa" -> (context as MainActivity).openDoaActivity()
                    //"hadist" -> (context as MainActivity).openHadistActivity()
                    //"livemekah" -> (context as MainActivity).openLiveActivity()
                    //"gpstrack" -> (context as MainActivity).openTrackingActivity()
                    //"wedding" -> (context as MainActivity).openWeddingActivity()
                    //"khitanan" -> (context as MainActivity).openKhitanActivity()
                    //"tasbih" -> (context as MainActivity).openTasbihActivity()
                    //"bookstore" -> (context as MainActivity).openBookstoreActivity()
                    //"donasi" -> (context as MainActivity).openDonasiActivity()
                    //"syahadat" -> (context as MainActivity).openSyahadatActivity()
                    //"bursasajadah" -> (context as MainActivity).openBursaSajadahActivitySplash()*/
                    else -> {
                    }
                }
            }
        }

        override fun clear() {

        }
    }
}
