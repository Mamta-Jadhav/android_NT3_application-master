package com.nectarinfotel.utils

import android.app.Application
import com.nectarinfotel.retrofit.RetroAPIInterface
import com.nectarinfotel.retrofit.Retrofit

class NectarApplication : Application() {

    companion object {
        var mRetroClient: RetroAPIInterface? = null
        var mRetroClient1: RetroAPIInterface? = null
        var userID: String = ""
        const val UserName: String = ""
        var Password: String = ""
    }

    override fun onCreate() {
        super.onCreate()

        mRetroClient =
            Retrofit.run { getClient(applicationContext)!!.create(RetroAPIInterface::class.java) }

        mRetroClient1 =
            Retrofit.run { getClient1(applicationContext)!!.create(RetroAPIInterface::class.java) }

        /*   var change = ""
           val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
           val language = sharedPreferences.getString("language", "bak")
           if (language == "Turkish") {
               change="tr"
           } else if (language=="English" ) {
               change = "en"
           }else {
               change =""
           }

           LoginActivity.dLocale = Locale(change) //set any locale you want here*/
    }
}