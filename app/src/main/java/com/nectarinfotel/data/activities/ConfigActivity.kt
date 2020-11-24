package com.nectarinfotel.data.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.nectarinfotel.R
import com.nectarinfotel.ui.login.LoginActivity
import com.nectarinfotel.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_login_config.*
import java.net.NetworkInterface
import java.util.*
import kotlin.collections.ArrayList

class ConfigActivity : AppCompatActivity() {

    var langTranslator: FirebaseTranslator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_config)

        translateTextToEnglish("String")

        /*submit.setOnClickListener {

            if (vpnConnected()) {

                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()

            } else {

                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.layout_vpn_config_dialog)

                val cancel = dialog.findViewById(R.id.cancel) as TextView
                val ok = dialog.findViewById(R.id.ok) as TextView

                cancel.setOnClickListener {
                    dialog.dismiss()
                }

                ok.setOnClickListener {
                    dialog.dismiss()
                    val intent = Intent("android.net.vpn.SETTINGS")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                dialog.show()

            }*/

        /*val intent = VpnService.prepare(applicationContext)
        if (intent != null) {
            startActivityForResult(intent, 0)
        } else {
            onActivityResult(0, Activity.RESULT_OK, null)
        }*/

        submit.setOnClickListener {
            if (organizationName.text.toString() != "") {
                if (organizationName.text.toString() == "movicel") {

                    PrefUtils.setORG(this, "movicel")
                    Log.d("test", "ORG : ${PrefUtils.getORG(this)}")
                    val i = Intent(this@ConfigActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()

                } else if (organizationName.text.toString() == "nectar.infotel" || organizationName.text.toString() == "Nectar.infotel") {

                    PrefUtils.setORG(this, organizationName.text.toString())
                    Log.d("test", "ORG : ${PrefUtils.getORG(this)}")
                    val i = Intent(this@ConfigActivity, LoginActivity::class.java)
                    startActivity(i)
                    finish()

                } else {
                    organizationName.requestFocus()
                    organizationName.setError("enter correct organization name")
                    organizationName.isFocusable = true
                }
            } else {
                organizationName.requestFocus()
                organizationName.setError("enter organization name")
                organizationName.isFocusable = true
            }
        }

        // This method will be executed once the timer is over
        // Start your app main activity


        /* Bundle b=getIntent().getExtras();
            if(b!=null)
            {
                String aa=b.getString("click_action");
                if (getIntent().hasExtra("click_action"))
                {
                    Intent resultIntent = new Intent(getApplicationContext(), TicketDetailsActivity.class);
                    startActivity(resultIntent);
                }
            }
            else {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
            }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("test", "Req : $requestCode")
        Log.d("test", "Res : $resultCode")

        if (resultCode == RESULT_OK) {

//            VpnService service = context.getSystemService(VPN_SERVICE);
//            VpnProfile profile = VpnProfile.create(L2TP_PROFILE);
//            profile.setName(myServerName);
//            profile.setServerName(myServerAddress);
//            profile.setRouteList(“192.168.1.0/255.255.255.0,192.168.10.0/255.255.255.0”);
//
//            service.connect(profile, myUserName, myPassword);
//            service.setNotificationIntent(myIntent);

//            var intent = Intent(this, OrbotVpnService::class.java);
//            startService(intent);
        }
        /* if (organizationName.text.toString() != "") {
             if (organizationName.text.toString() == "movicel") {

                 PrefUtils.setORG(this, "movicel")
                 Log.d("test", "ORG : ${PrefUtils.getORG(this)}")
                 val i = Intent(this@ConfigActivity, LoginActivity::class.java)
                 startActivity(i)
                 finish()

             } else if (organizationName.text.toString() == "nectar.infotel" || organizationName.text.toString() == "Nectar.infotel") {

                 PrefUtils.setORG(this, organizationName.text.toString())
                 Log.d("test", "ORG : ${PrefUtils.getORG(this)}")
                 val i = Intent(this@ConfigActivity, LoginActivity::class.java)
                 startActivity(i)
                 finish()

             } else {
                 organizationName.requestFocus()
                 organizationName.setError("enter correct organization name")
                 organizationName.isFocusable = true
             }
         } else {
             organizationName.requestFocus()
             organizationName.setError("enter organization name")
             organizationName.isFocusable = true
         }
 */
    }

    /* override fun onResume() {
         super.onResume()

         Log.d("test", "VPN Connected : ${vpnConnected()}")

     }*/

    private fun vpnConnected(): Boolean {
        val networkList: MutableList<String> = ArrayList()
        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp()) networkList.add(networkInterface.getName())
            }
        } catch (ex: Exception) {
            Log.d("test", "isVpnUsing Network List didn't received")
        }

        return networkList.contains("tun0")
    }

    fun downloadTranslatorAndTranslate(langCode: String?, str: String): String {

        var trans: String = ""

        val sourceLanguage = FirebaseTranslateLanguage
            .languageForLanguageCode(langCode!!)!!

        //create translator for source and target languages
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(FirebaseTranslateLanguage.PT)
            .build()

        langTranslator =
            FirebaseNaturalLanguage.getInstance().getTranslator(options)

        //download language models if needed
        val conditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        langTranslator!!.downloadModelIfNeeded()
            .addOnSuccessListener {
                Log.d("translator", "downloaded lang  model")
                //after making sure language models are available
                //perform translation
//                trans = translateText(langTranslator, str)
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Problem in translating the text entered",
                    Toast.LENGTH_LONG
                ).show()
            }

        return trans
    }

    fun translateTextToEnglish(str: String): String {
        //First identify the language of the entered text
        var trans: String = ""

        val languageIdentifier: FirebaseLanguageIdentification =
            FirebaseNaturalLanguage.getInstance().languageIdentification
        Log.d("languageIdentifier", "languageIdentifier  $languageIdentifier")
        languageIdentifier.identifyLanguage(str)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "en") {
                    Log.d("translator", "lang $languageCode")
                    //download translator for the identified language
                    // and translate the entered text into english
                    trans = downloadTranslatorAndTranslate(languageCode, str)
                } else {
                    Toast.makeText(
                        this,
                        "Could not identify language of the text entered",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Problem in identifying language of the text entered",
                    Toast.LENGTH_LONG
                ).show()
            }

        return trans
    }
}