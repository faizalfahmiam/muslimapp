package com.dmg.muslimapp.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.dmg.muslimapp.ui.main.MainActivity

class UriToIntentMapper//mIntents = intentHelper
(context: Context) {
    private var mContext: Context = context
    //private lateinit var mIntents: IntentHelper

    fun dispatchIntent(intent: Intent) {
        val uri = intent.data
        var dispatchIntent: Intent? = null

        if (uri == null) throw IllegalArgumentException("Uri cannot be null")

        val scheme = uri.scheme!!.toLowerCase()
        val host = uri.host!!.toLowerCase()

        if (("http" == scheme || "https" == scheme) && ("www.muslimapp.id" == host || "muslimapp.id" == host)) {
            //dispatchIntent = mapWebLink(uri)
            mapWebLink(uri)
        }

        if (dispatchIntent != null) {
            mContext.startActivity(dispatchIntent)
        }
    }

    private fun mapWebLink(uri: Uri) {
        val path = uri.path
        Log.e("Path", path)
        Log.e("Uri", uri.toString())

        if (path!!.startsWith("/masjid")) {
            val cQuery = uri.getQueryParameter("id")
            Log.e("Link", cQuery)

            /*if (cQuery == "777") {
                val choice = Integer.parseInt(uri.getQueryParameter("id")!!)
                //return mIntents.newCActivityIntent(mContext, cQuery, choice)
            }*/

            val intent = MainActivity.getStartIntent(mContext)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra("dispatcher", "masjid")
            intent.putExtra("id", cQuery)
            mContext.startActivity(intent)
        }

        /*switch (path) {
            case "/a":
                return mIntents.newAActivityIntent(mContext);
            case "/c":
                String cQuery = uri.getQueryParameter("query");
                int choice = Integer.parseInt(uri.getQueryParameter("choice"));
                return mIntents.newCActivityIntent(mContext, cQuery, choice);
        }*/
        //return null
    }
}