package com.nectarinfotel.data.activities

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.Window
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nectarinfotel.R
import com.nectarinfotel.data.adapter.*
import com.nectarinfotel.data.jsonmodel.DetailInfo
import com.nectarinfotel.data.jsonmodel.DetailResponse
import com.nectarinfotel.data.model.MySingleton
import com.nectarinfotel.ui.login.LoginActivity
import com.nectarinfotel.utils.*
import com.nectarinfotel.utils.isColorLight
import com.nectarinfotel.utils.onPageSelected
import kotlinx.android.synthetic.main.affacted_site_popup_layout.*
import kotlinx.android.synthetic.main.dialog_incident_created.view.*
import kotlinx.android.synthetic.main.new_incident_layout.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NewIncidentActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    CompoundButton.OnCheckedChangeListener {

    val site: ArrayList<String> = ArrayList()
    private var siteValueList: ArrayList<DetailInfo> = ArrayList()
    private var siteValueListid: ArrayList<DetailInfo> = ArrayList()
    private var sitelist: ArrayList<DetailInfo> = ArrayList()
    private var arealist: MutableList<DetailInfo> = mutableListOf()
    private var provincelist: MutableList<DetailInfo> = mutableListOf()
    private var categorylist: MutableList<DetailInfo> = mutableListOf()
    private var eventlist: MutableList<DetailInfo> = mutableListOf()
    private var reasonlist: MutableList<DetailInfo> = mutableListOf()
    private var subreasonlist: MutableList<DetailInfo> = mutableListOf()
    private var servicelist: MutableList<DetailInfo> = mutableListOf()
    private var callerList: ArrayList<DetailInfo> = ArrayList()
    private var callerList_filter: ArrayList<DetailInfo> = ArrayList()
    var progress: ProgressDialog? = null
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val contentType = "application/json"
    val TAG = "NOTIFICATION TAG"

    // Initialize a new array with elements
    val urgency = arrayOf(
        "Low", "High", "Medium", "Critical"
    )
    val urgencypt = arrayOf(
        "Baixa", "Alta", "Média", "Crítica"
    )

    var technology: String? = ""
    var technology1: String? = ""
    var technology2: String? = ""
    var technology3: String? = ""
    var serviceaffacted: String? = ""
    var serviceaffacted1: String? = ""
    var serviceaffacted2: String? = ""
    var serviceaffacted3: String? = ""
    var serviceaffacted4: String? = ""
    var serviceaffacted5: String? = ""
    var serviceaffacted6: String? = ""
    var serviceaffacted7: String? = ""
    var serviceaffacted8: String? = ""
    var serviceaffacted9: String? = ""
    var serviceaffacted10: String? = ""
    var serviceaffacted11: String? = ""
    var serviceaffacted12: String? = ""
    var serviceaffacted13: String? = ""
    var urgency_value: String? = null
    var provinceid: String? = null
    var area: String? = null
    var areaid: String? = null
    var services: String? = null
    var servicesid: String? = null
    var categoryid: String? = null
    var eventid: String? = null
    var reason: String? = null
    var reasonid: String? = "0"
    var subreason: String? = null
    var sub_reasonid: String? = "0"
    var siteid: String? = null
    var callerid: String? = null
    internal lateinit var info: DetailInfo

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nectarinfotel.R.layout.new_incident_layout)
        setSupportActionBar(toolbar_incident)

        //language conversion
        changelanguage()

        g2_checkbox.setOnCheckedChangeListener(this);
        g3_checkbox.setOnCheckedChangeListener(this);
        g4_checkbox.setOnCheckedChangeListener(this);

        internationcall_checkbox.setOnCheckedChangeListener(this);
        voicemail_checkbox.setOnCheckedChangeListener(this);
        dice_checkbox.setOnCheckedChangeListener(this);
        face_checkbox.setOnCheckedChangeListener(this);
        prepaid_checkbox.setOnCheckedChangeListener(this);
        sms_checkbox.setOnCheckedChangeListener(this);
        supervise_checkbox.setOnCheckedChangeListener(this);
        callcentre_checkbox.setOnCheckedChangeListener(this);
        international_checkbox.setOnCheckedChangeListener(this);
        ussd_checkbox.setOnCheckedChangeListener(this);
        you_checkbox.setOnCheckedChangeListener(this);
        sales_checkbox.setOnCheckedChangeListener(this);
        voice_checkbox.setOnCheckedChangeListener(this);

        incident_area_text.setText(
            Html.fromHtml(
                resources.getString(R.string.select_area),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        );
        incident_services_text.setText(Html.fromHtml(resources.getString(R.string.select_services)));
        incident_title_text.setText(Html.fromHtml(resources.getString(R.string.title), 63));
        incident_reported_by_text.setText(Html.fromHtml(resources.getString(R.string.Caller)));

        //call api for get provincelist
        GetALlProvinceList()

        //call api for get Arealist
        GetALlAreaList()

        //call api for get callerlist
        GetALlServiceList()

        //call api for get categorylist
        GetALlCategoryList()

        //call api for get Eventlist
        GetALlEventList()

        //call api for get ReasonList
        GetALlReasonstList()

        //call api for get ReasonList
        GetALlSubReasonstList()

        //call api for get AffatcedSiteslist
        GetALlAffactedSitestList()

        //call api for get callerlist
        GetALlCallerList()

        seturgency()

        pager.setOnTouchListener(OnTouchListener { v, event -> true })

        pager.adapter = MainPagerAdapter()
        pager.offscreenPageLimit = 3
        dots.attachViewPager(pager)

        pager.onPageSelected {
            val colorRes = when (it) {
                0 -> com.nectarinfotel.R.color.white
                1 -> com.nectarinfotel.R.color.white
                else -> com.nectarinfotel.R.color.white
            }
            val color = ContextCompat.getColor(this, colorRes)
            dots.setDotTintRes(if (color.isColorLight()) com.nectarinfotel.R.color.colorPrimaryDark else com.nectarinfotel.R.color.white)
        }
        dots.setDotTintRes(com.nectarinfotel.R.color.colorPrimaryDark)

        extra_site.setOnClickListener {
            showallsites()
        }
        next_layout.setOnClickListener {

            if (incident_title.text.toString().length == 0) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(com.nectarinfotel.R.string.enter_title),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (areaid.equals("0")) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(com.nectarinfotel.R.string.enter_area),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (servicesid.equals("0")) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(com.nectarinfotel.R.string.enter_services),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (incident_reported_by.text.toString().length == 0) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(com.nectarinfotel.R.string.enter_caller),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                previous_layout.visibility = View.VISIBLE

                if (pager.currentItem == 0) {

                    pager.setCurrentItem(1, true);
                    affacted_site.setText("")
                    // Set the AutoCompleteTextView for site adapter
                    GetALlAffactedSitestList()
                } else if (pager.currentItem == 1) {
                    next_layout.visibility = View.GONE
                    pager.setCurrentItem(2, true);
                }
            }
        }

        previous_layout.setOnClickListener {
            if (pager.currentItem == 2) {
                next_layout.visibility = View.VISIBLE
                pager.setCurrentItem(1, true);
                affacted_site.setText("")
            } else if (pager.currentItem == 1) {
                previous_layout.visibility = View.GONE
                next_layout.visibility = View.VISIBLE
                pager.setCurrentItem(0, true);
            }
        }

        affactedsite_delete1.setOnClickListener(View.OnClickListener {
            site1_layout.visibility = View.GONE
        })

        affactedsite_delete2.setOnClickListener(View.OnClickListener {
            site2_layout.visibility = View.GONE
        })

        // Set an item click listener for auto complete text view
        incident_reported_by.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()
                // Display the clicked item using toast
                Log.d("callerListcallerList", "" + AutoCompleteAdapter.callerlist.size);
                var name = AutoCompleteAdapter.callerlist.get(position).name
                callerid = AutoCompleteAdapter.callerlist.get(position).id
                Log.d("name", "" + name);
                Log.d("id", "" + callerid);
                //Toast.makeText(applicationContext,"Selected : $name"+id+position+selectedItem, Toast.LENGTH_SHORT).show()
            }
        incident_reported_by.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                // Set a focus change listener for auto complete text view
                incident_reported_by.showDropDown()
            }
        }
        // Set a dismiss listener for auto complete text view
        incident_reported_by.setOnDismissListener {

        }

        // Set an item click listener for auto complete text view
        affacted_site.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position).toString()

                Log.d("sitelistfilter", "" + SiteAutoCompleteAdapter.Sitelist.size);
                var name = SiteAutoCompleteAdapter.Sitelist.get(position).site_name
                var parent = SiteAutoCompleteAdapter.Sitelist.get(position).parent_site
                siteid = SiteAutoCompleteAdapter.Sitelist.get(position).site_id
                Log.d("name", "" + name);
                Log.d("id", "" + siteid);
                Log.d("parent", "" + parent);
                info = DetailInfo()
                info.site_name = SiteAutoCompleteAdapter.Sitelist.get(position).site_name
                info.site_code = SiteAutoCompleteAdapter.Sitelist.get(position).site_code
                info.site_id = SiteAutoCompleteAdapter.Sitelist.get(position).site_id
                info.province = SiteAutoCompleteAdapter.Sitelist.get(position).province
                info.parent_site = SiteAutoCompleteAdapter.Sitelist.get(position).parent_site

                siteValueList.add(info)
                siteValueListid.add(info)

                for (site in sitelist) { //Auto Add childs for parent site
                    if (site.parent_site == siteid) {
                        Log.d("test", "Parent Site : " + site.parent_site)
                        Log.d("test", "Site Id : " + siteid)
                        info = DetailInfo()
                        info.site_name = site.site_name
                        info.site_code = site.site_code
                        info.site_id = site.site_id
                        info.province = site.province
                        info.parent_site = site.parent_site

                        siteValueList.add(info)
                        siteValueListid.add(info)
                    }
                }

                if (siteValueList.size > 0) {
                    affacted_site.setText("")
                    var count = siteValueList.size
                    extra_site.visibility = View.VISIBLE
                    extra_site.text = "" + count + " " + resources.getString(R.string.site_added)
                }

                // Toast.makeText(applicationContext,"Selected : $selectedItem", Toast.LENGTH_SHORT).show()
            }
        // Set a focus change listener for auto complete text view
        affacted_site.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                affacted_site.showDropDown()
            }
        }
        // Set a dismiss listener for auto complete text view
        affacted_site.setOnDismissListener {

        }
        backImageView_incident.setOnClickListener {
            finish()
        }
        create_incident_button.setOnClickListener {
            /* if(incident_title.text.toString().length==0)
             {
                 Toast.makeText(applicationContext,resources.getString(com.nectarinfotel.R.string.enter_title),Toast.LENGTH_SHORT).show()
             }else if (area==null)
             {
                 Toast.makeText(applicationContext,resources.getString(com.nectarinfotel.R.string.enter_area),Toast.LENGTH_SHORT).show()
             }
             else if (services==null)
             {
                 Toast.makeText(applicationContext,resources.getString(com.nectarinfotel.R.string.enter_services),Toast.LENGTH_SHORT).show()
             }
             else if (incident_reported_by.text.toString().length==0)
             {
                 Toast.makeText(applicationContext,resources.getString(com.nectarinfotel.R.string.enter_caller),Toast.LENGTH_SHORT).show()
             }
               else
             {*/

            progress = ProgressDialog(this)
            progress?.setMessage(resources.getString(R.string.ticketAddingMessage))
            progress?.setCancelable(false)
            progress?.show()

            create_incident_button.isClickable = false

            val siteid = siteValueList.joinToString { it -> "${it.site_id}" }
            /* Log.d("commaSeperatedString",siteid);

             Log.d("area",areaid)
             Log.d("reportedby",callerid);
             Log.d("title",incident_title.text.toString());
             Log.d("desc","desc");
             Log.d("description_format","text");
             Log.d("province",provinceid);
             Log.d("reasonid",reasonid);
             Log.d("subreason",sub_reasonid);
             Log.d("event",eventid);
             Log.d("category",categoryid);
             Log.d("services",servicesid);
             Log.d("urgency","4");
             Log.d("service_aftd_id",serviceaffacted);
             Log.d("network",technology);
             Log.d("userid",NectarApplication.userID);
             Log.d("siteid",siteid);*/

            Log.d("test", "areaid : " + areaid)
            Log.d("test", "callerid : " + callerid)
            Log.d("test", "incident_title.text.toString() ${incident_title.text.toString()}")
            Log.d(
                "test",
                "incident_description.text.toString() ${incident_description.text.toString()}"
            )
            Log.d("test", "text")
            Log.d("test", "provinceid : " + provinceid)
            Log.d("test", "reasonid : " + reasonid)
            Log.d("test", "sub_reasonid : " + sub_reasonid)
            Log.d("test", "eventid : " + eventid)
            Log.d("test", "categoryid : " + categoryid)
            Log.d("test", "servicesid " + servicesid)
            Log.d("test", "urgency_value : " + urgency_value)
            Log.d("test", "serviceaffacted " + serviceaffacted)
            Log.d("test", "tech " + technology)
            Log.d("test", "NectarApplication.userID: " + NectarApplication.userID)
            Log.d("test", "siteid : " + siteid)

            IncidentCreateAPI(
                areaid,
                callerid,
                incident_title.text.toString(),
                incident_description.text.toString(),
                "text",
                provinceid,
                reasonid,
                sub_reasonid,
                eventid,
                categoryid,
                servicesid,
                urgency_value,
                serviceaffacted,
                technology,
                NectarApplication.userID,
                siteid,
                this
            )
        }
    }

    private fun seturgency() {
        incident_qualifications.onItemSelectedListener = this

        //Creating the ArrayAdapter instance having the country list
        if (LoginActivity.language == "Portuguese") {
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, urgencypt)

            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //Setting the ArrayAdapter data on the Spinner
            incident_qualifications.adapter = aa
        } else {
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, urgency)

            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            //Setting the ArrayAdapter data on the Spinner
            incident_qualifications.adapter = aa
        }
    }

    private fun IncidentCreateAPI(

        areaid: String?,
        callerid: String?,
        title: String,
        desc: String,
        descformat: String,
        provinceid: String?,
        reasonid: String?,
        subReasonid: String?,
        eventid: String?,
        categoryid: String?,
        servicesid: String?,
        urgency: String?,
        serviceaffacted: String?,
        technology: String?,
        userID: String,
        siteid: String,
        newIncidentActivity: NewIncidentActivity
    ) {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callIncidentCretaeAPI(
                    areaid,
                    callerid,
                    title,
                    desc,
                    descformat,
                    provinceid,
                    reasonid,
                    subReasonid,
                    eventid,
                    categoryid,
                    urgency,
                    serviceaffacted,
                    siteid,
                    technology,
                    userID,
                    servicesid
                )
            } else {
                call = NectarApplication.mRetroClient?.callIncidentCretaeAPI(
                    areaid,
                    callerid,
                    title,
                    desc,
                    descformat,
                    provinceid,
                    reasonid,
                    subReasonid,
                    eventid,
                    categoryid,
                    urgency,
                    serviceaffacted,
                    siteid,
                    technology,
                    userID,
                    servicesid
                )
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("incidentcreate_response", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)

                Log.d("message", "" + detailResponse.msg)

                var ticketId = detailResponse.ticketid + "";
                var ticketTitle = incident_title.text.toString();
                Log.d("test", " Ticket Id : " + ticketId)

                if (detailResponse.msg.equals("Data Added Successfully")) {
                    progress?.dismiss()

                    val mDialogView = LayoutInflater.from(this@NewIncidentActivity)
                        .inflate(R.layout.dialog_incident_created, null)
                    //AlertDialogBuilder
                    val mBuilder = AlertDialog.Builder(this@NewIncidentActivity)
                        .setView(mDialogView)
                        .setCancelable(false)
                    //show dialog
                    val mAlertDialog = mBuilder.show()
                    //login button click of custom layout
                    mDialogView.msg.setText("TT-000$ticketId - ${resources.getString(R.string.ticketAddedMessage)}")
                    mDialogView.ok.setOnClickListener {
                        //dismiss dialog
                        mAlertDialog.dismiss()

                        Log.d("test", "bshdhsdhsgjdgjhsgjdagjkf");
                        finish()

                        val TOPIC =
                            "/topics/user_NT3" //topic must match with what the receiver subscribed to
                        Log.d(
                            "test",
                            "FirebaseInstanceId.getInstance().getToken() : " + FirebaseInstanceId.getInstance()
                                .getToken()
                        );

                        val notification = JSONObject()
                        val notifcationBody = JSONObject()
                        try {
                            notifcationBody.put("status", "new")
                            notifcationBody.put("urgency", "1")
                            notifcationBody.put("id", ticketId)
                            notifcationBody.put("category", "1")
                            notifcationBody.put("title", ticketTitle)
                            Log.d("test_Notification: ", "Msg : " + notifcationBody.toString())
                            notification.put("to", TOPIC)
                            notification.put("data", notifcationBody)
                        } catch (e: JSONException) {
                            Log.e(TAG, "onCreate: " + e.message)
                        }
                        val user: FirebaseUser? = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // Name, email address, and profile photo Url
                            var name = user.getDisplayName();
                            var email = user.getEmail();
                            var photoUrl = user.getPhotoUrl();

                            // Check if user's email is verified
                            var emailVerified = user.isEmailVerified();
                            Log.d("test", name)
                            Log.d("test", email)
                            Log.d("test", "$photoUrl ")
                            Log.d("test", "$emailVerified ")
                        }

                        sendNotification(notification)

//                            .addOnCompleteListener { task ->
//                                var msg = "{\"status\":\"new\",\"urgency\":\"1\",\"id\":\""+ticketId+"\",\"category\":\"1\",\"title\":\""+ticketTitle+"\"}"
//                                if (!task.isSuccessful) {
//                                    msg = "_failed"
//                                }
//                                Log.d("test_TAG", msg)
//                                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                            }

                        if (urgency != "1") {
                            DetailActivity.getStartIntent(
                                newIncidentActivity,
                                "status",
                                1,
                                "new",
                                "New", null
                            )
                        } else {
                            DetailActivity.getStartIntent(
                                newIncidentActivity,
                                "status",
                                1,
                                "escalated_tto",
                                "Escalated_tto", null
                            )
                        }
                    }
                } else {

                    progress?.dismiss()
                    Toast.makeText(applicationContext, "" + detailResponse.msg, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("incidentcreate_fail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun changelanguage() {
        if (LoginActivity.language.equals("Portuguese")) {
            val config = resources.configuration
            val locale = Locale("pt")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            incident_title_text.setText(resources.getString(com.nectarinfotel.R.string.title))
            incident_area_text.setText(resources.getString(com.nectarinfotel.R.string.select_area))
            incident_services_text.setText(resources.getString(com.nectarinfotel.R.string.select_services))
            incident_reported_by_text.setText(resources.getString(com.nectarinfotel.R.string.Caller))
            create_incident_button.setText(resources.getString(com.nectarinfotel.R.string.create))
            General_info.setText(resources.getString(com.nectarinfotel.R.string.general_info))
            incident_next_text.setText(resources.getString(com.nectarinfotel.R.string.next))
            incident_previous_text.setText(resources.getString(com.nectarinfotel.R.string.previous))
            incident_description_text.setText(resources.getString(com.nectarinfotel.R.string.description))

            network.setText(resources.getString(com.nectarinfotel.R.string.network))
            technologies.setText(resources.getString(com.nectarinfotel.R.string.technology))
            service_affacted.setText(resources.getString(com.nectarinfotel.R.string.service_affacted))
            internationcall_checkbox.setText(resources.getString(com.nectarinfotel.R.string.internation_calls))
            voicemail_checkbox.setText(resources.getString(com.nectarinfotel.R.string.voice_mails))
            dice_checkbox.setText(resources.getString(com.nectarinfotel.R.string.dice))
            face_checkbox.setText(resources.getString(com.nectarinfotel.R.string.fact))
            prepaid_checkbox.setText(resources.getString(com.nectarinfotel.R.string.prepaid))
            sms_checkbox.setText(resources.getString(com.nectarinfotel.R.string.sms))
            supervise_checkbox.setText(resources.getString(com.nectarinfotel.R.string.supervise))
            callcentre_checkbox.setText(resources.getString(com.nectarinfotel.R.string.support_callcentre))
            international_checkbox.setText(resources.getString(com.nectarinfotel.R.string.inter_operators))
            ussd_checkbox.setText(resources.getString(com.nectarinfotel.R.string.ussd))
            you_checkbox.setText(resources.getString(com.nectarinfotel.R.string.you))
            sales_checkbox.setText(resources.getString(com.nectarinfotel.R.string.sales))
            voice_checkbox.setText(resources.getString(com.nectarinfotel.R.string.voice))
            province_text.setText(resources.getString(com.nectarinfotel.R.string.province))

            reason_qualifications.setText(resources.getString(com.nectarinfotel.R.string.reason_qualifications))
            incident_reason_text.setText(resources.getString(com.nectarinfotel.R.string.reason))
            incident_subreason_text.setText(resources.getString(com.nectarinfotel.R.string.subreason))
            incident_event_text.setText(resources.getString(com.nectarinfotel.R.string.event))
            incident_categories_text.setText(resources.getString(com.nectarinfotel.R.string.categories))
            incident_qualifications_text.setText(resources.getString(com.nectarinfotel.R.string.qualification))
            affacted_site_text.setText(resources.getString(com.nectarinfotel.R.string.affacted_site))
        } else {
            val config = resources.configuration
            val locale = Locale("en")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            incident_title_text.setText(resources.getString(com.nectarinfotel.R.string.title))
            incident_area_text.setText(resources.getString(com.nectarinfotel.R.string.select_area))
            incident_services_text.setText(resources.getString(com.nectarinfotel.R.string.select_services))
            incident_reported_by_text.setText(resources.getString(com.nectarinfotel.R.string.Caller))
            create_incident_button.setText(resources.getString(com.nectarinfotel.R.string.create))
            General_info.setText(resources.getString(com.nectarinfotel.R.string.general_info))
            incident_next_text.setText(resources.getString(com.nectarinfotel.R.string.next))
            incident_previous_text.setText(resources.getString(com.nectarinfotel.R.string.previous))
            incident_description_text.setText(resources.getString(com.nectarinfotel.R.string.description))

            network.setText(resources.getString(com.nectarinfotel.R.string.network))
            technologies.setText(resources.getString(com.nectarinfotel.R.string.technology))
            service_affacted.setText(resources.getString(com.nectarinfotel.R.string.service_affacted))
            internationcall_checkbox.setText(resources.getString(com.nectarinfotel.R.string.internation_calls))
            voicemail_checkbox.setText(resources.getString(com.nectarinfotel.R.string.voice_mails))
            dice_checkbox.setText(resources.getString(com.nectarinfotel.R.string.dice))
            face_checkbox.setText(resources.getString(com.nectarinfotel.R.string.fact))
            prepaid_checkbox.setText(resources.getString(com.nectarinfotel.R.string.prepaid))
            sms_checkbox.setText(resources.getString(com.nectarinfotel.R.string.sms))
            supervise_checkbox.setText(resources.getString(com.nectarinfotel.R.string.supervise))
            callcentre_checkbox.setText(resources.getString(com.nectarinfotel.R.string.support_callcentre))
            international_checkbox.setText(resources.getString(com.nectarinfotel.R.string.inter_operators))
            ussd_checkbox.setText(resources.getString(com.nectarinfotel.R.string.ussd))
            you_checkbox.setText(resources.getString(com.nectarinfotel.R.string.you))
            sales_checkbox.setText(resources.getString(com.nectarinfotel.R.string.sales))
            voice_checkbox.setText(resources.getString(com.nectarinfotel.R.string.voice))
            province_text.setText(resources.getString(com.nectarinfotel.R.string.province))
            reason_qualifications.setText(resources.getString(com.nectarinfotel.R.string.reason_qualifications))
            incident_reason_text.setText(resources.getString(com.nectarinfotel.R.string.reason))
            incident_subreason_text.setText(resources.getString(com.nectarinfotel.R.string.subreason))
            incident_event_text.setText(resources.getString(com.nectarinfotel.R.string.event))
            incident_categories_text.setText(resources.getString(com.nectarinfotel.R.string.categories))
            incident_qualifications_text.setText(resources.getString(com.nectarinfotel.R.string.qualification))
            affacted_site_text.setText(resources.getString(com.nectarinfotel.R.string.affacted_site))
        }
    }

    private fun GetALlServiceList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callServiceListAPI(1, areaid)
            } else {
                call = NectarApplication.mRetroClient?.callServiceListAPI(1, areaid)
            }
        }
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_service", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    // Log.d("jsonarray", ""+detailResponse.site_name)
                    //set data into adapter for area
                    servicelist = mutableListOf()

                    servicelist.addAll(detailResponse.info)
                    initServiceResources(servicelist)
                } else {
                    servicelist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = resources.getString(R.string.select_one)
                    servicelist.add(info)
                    initServiceResources(servicelist)
                    // Toast.makeText(applicationContext, ""+detailResponse.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail_sites", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlSubReasonstList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callSubReasonListAPI(1, reasonid)
            } else {
                call = NectarApplication.mRetroClient?.callSubReasonListAPI(1, reasonid)
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_subreason", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    // Log.d("jsonarray", ""+detailResponse.site_name)
                    //set data into adapter for area
                    subreasonlist = mutableListOf()
                    subreasonlist.addAll(detailResponse.info)
                    initsubReasonsSpinnerResources(subreasonlist)
                } else {
                    subreasonlist = mutableListOf()
                    info = DetailInfo()
                    info.sub_reason_id = "0"
                    info.sub_reason = resources.getString(R.string.select_one)
                    subreasonlist.add(info)
                    initsubReasonsSpinnerResources(subreasonlist)
                    // Toast.makeText(applicationContext, ""+detailResponse.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail_sites", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlAffactedSitestList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callAffactedSitesListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callAffactedSitesListAPI(1)
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_sites", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    // Log.d("jsonarray", ""+detailResponse.site_name)
                    //set data into adapter for area
                    sitelist.clear()
                    sitelist.addAll(detailResponse.info)
                    initAffactedSitesResources(sitelist)
                } else {
                    Toast.makeText(applicationContext, "" + detailResponse.msg, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail_sites", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlCallerList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callCallerListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callCallerListAPI(1)
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_caller", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    Log.d("jsonarray", "" + detailResponse.reason)
                    //set data into adapter for area
                    //   initCallerResources(detailResponse.info)
                    callerList.addAll(detailResponse.info)
                    initCallerResources(callerList)
                } else {
                    Toast.makeText(applicationContext, "" + detailResponse.msg, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlReasonstList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callReasonsListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callReasonsListAPI(1)
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    Log.d("jsonarray", "" + detailResponse.reason)
                    //set data into adapter for reason
                    info = DetailInfo()
                    info.reason_id = "0"
                    info.reason = "Select One"
                    reasonlist.add(info)
                    reasonlist.addAll(detailResponse.info)
                    initReasonsSpinnerResources(reasonlist)
                } else {
                    Toast.makeText(applicationContext, "" + detailResponse.msg, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlEventList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callEventListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callEventListAPI(1)
            }
        }
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_event", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    eventlist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = "Select One"
                    eventlist.add(info)
                    eventlist.addAll(detailResponse.info)
                    //set data into adapter for event
                    initEventEventResources(eventlist)
                } else {
                    eventlist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = resources.getString(R.string.select_one)
                    eventlist.add(info)
                    initEventEventResources(eventlist)
                    //Toast.makeText(applicationContext, ""+detailResponse.msg, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlCategoryList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callCategoriesListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callCategoriesListAPI(1)
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_category", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    categorylist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = resources.getString(R.string.select_one)
                    categorylist.add(info)
                    categorylist.addAll(detailResponse.info)
                    //set data into adapter for category
                    initCategorySpinnerResources(categorylist)
                } else {
                    categorylist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = resources.getString(R.string.select_one)
                    categorylist.add(info)
                    initCategorySpinnerResources(categorylist)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlAreaList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callAreaListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callAreaListAPI(1)
            }
        }
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_area", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)
                if (detailResponse.msg.equals("Data found")) {
                    // Log.d("jsonarray", ""+detailResponse.name)
                    //set data into adapter for area
                    arealist = mutableListOf()
                    info = DetailInfo()
                    info.orgnid = "0"
                    info.name = resources.getString(R.string.select_one)
                    arealist.add(info)
                    arealist.addAll(detailResponse.info)
                    initAreaSpinnerResources(arealist)
                } else {
                    arealist = mutableListOf()
                    info = DetailInfo()
                    info.orgnid = "0"
                    info.name = resources.getString(R.string.select_one)
                    categorylist.add(info)
                    initAreaSpinnerResources(arealist)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun GetALlProvinceList() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1?.callProvinceListAPI(1)
            } else {
                call = NectarApplication.mRetroClient?.callProvinceListAPI(1)
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("str_response_province", response.body().toString())
                val rsp: JsonObject? = response.body() ?: return
                var response = response.body().toString()
                val detailResponse =
                    Gson().fromJson<DetailResponse>(rsp, DetailResponse::class.java)
                Log.d("message", "" + detailResponse.msg)

                if (detailResponse.msg.equals("Data found")) {
                    provincelist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = resources.getString(R.string.select_one)
                    provincelist.add(info)
                    provincelist.addAll(detailResponse.info)
                    //set data into adapter for province
                    initProvinceSpinnerResources(provincelist)
                } else {
                    provincelist = mutableListOf()
                    info = DetailInfo()
                    info.id = "0"
                    info.name = resources.getString(R.string.select_one)
                    provincelist.add(info)
                    initProvinceSpinnerResources(provincelist)
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                Log.d("str_responsefail", "str_responsefail" + t)
                Toast.makeText(applicationContext, "please try again" + t, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun sendNotification(notification: JSONObject) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            com.android.volley.Response.Listener<JSONObject?> { response ->
                Log.i(TAG, "onResponse: " + response.toString())

            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(this@NewIncidentActivity, "Request error", Toast.LENGTH_LONG)
                    .show()
                Log.i(TAG, "onErrorResponse: Didn't work")
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String?, String?>? {
                Log.d("test", "Hereee")
                val params: HashMap<String?, String> = HashMap()
                var serverKey = "key=" + resources.getString(R.string.firebase_server_key2);
                Log.d("test", serverKey)
                params.put("Authorization", serverKey)
                params.put("Content-Type", contentType)
                return params
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(jsonObjectRequest)
    }

    private fun showallsites() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.affacted_site_popup_layout)

        dialog.site_recyclerView
        dialog.site_recyclerView.layoutManager = LinearLayoutManager(this)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)
        /* siteValueList.removeAt(0)
         siteValueList.removeAt(1)*/
        // Access the RecyclerView Adapter and load the data into it
        dialog.site_recyclerView.adapter =
            SiteAdapter(siteValueList, siteValueListid, this, extra_site)

        dialog.deletesitedialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun initAffactedSitesResources(sites: ArrayList<DetailInfo>) {
        Log.d("sitessites", "" + sites.size)
        //set adapter for affacted site
        val adapter = SiteAutoCompleteAdapter(this, sites)
        affacted_site.threshold = 1
        affacted_site.setAdapter(adapter)
    }

    private fun initCallerResources(callerlist: ArrayList<DetailInfo>) {

        //set adapter for affacted site
        val adapter = AutoCompleteAdapter(this, callerlist, callerList_filter)
        incident_reported_by.threshold = 1
        incident_reported_by.setAdapter(adapter)

    }

    private fun initReasonsSpinnerResources(reasons: MutableList<DetailInfo>) {
        incident_reason.onItemSelectedListener = this
        val adapter = ReasonAdapter(reasons, this)
        //Setting the ArrayAdapter data on the Spinner
        incident_reason.adapter = adapter
    }

    private fun initsubReasonsSpinnerResources(reasons: MutableList<DetailInfo>) {
        incident_subreason.onItemSelectedListener = this
        val adapter = SubReasonAdapter(reasons, this)
        //Setting the ArrayAdapter data on the Spinner
        incident_subreason.adapter = adapter
    }

    private fun initCategorySpinnerResources(categories: MutableList<DetailInfo>) {
        incident_categories.onItemSelectedListener = this

        val adapter = AreaAdapter(categories, this)
        //Setting the ArrayAdapter data on the Spinner
        incident_categories.adapter = adapter
    }

    private fun initAreaSpinnerResources(area: MutableList<DetailInfo>) {
        incident_area.onItemSelectedListener = this

        val adapter = AreaAdapter(area, this)
        //Setting the ArrayAdapter data on the Spinner
        incident_area.adapter = adapter
    }

    private fun initServiceResources(service: MutableList<DetailInfo>) {
        incident_services.onItemSelectedListener = this

        val adapter = AreaAdapter(service, this)
        //Setting the ArrayAdapter data on the Spinner
        incident_services.adapter = adapter
    }

    private fun initEventEventResources(event: MutableList<DetailInfo>) {
        incident_event.onItemSelectedListener = this

        incident_services.onItemSelectedListener = this

        val adapter = AreaAdapter(event, this)
        //Setting the ArrayAdapter data on the Spinner
        incident_event.adapter = adapter
    }

    private fun initProvinceSpinnerResources(provincearray: MutableList<DetailInfo>) {
        province.onItemSelectedListener = this

        incident_services.onItemSelectedListener = this

        val adapter = AreaAdapter(provincearray, this)
        //Setting the ArrayAdapter data on the Spinner
        province.adapter = adapter
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (parent == province) {
            provinceid = provincelist.get(position).id
            Log.d("province_value", provinceid)
        } else if (parent == incident_area) {
            area = arealist.get(position).name
            areaid = arealist.get(position).orgnid
            Log.d("area", area)
            Log.d("areaid", areaid)
            //call api for get service list
            GetALlServiceList()
        } else if (parent == incident_categories) {
            categoryid = categorylist.get(position).id
            Log.d("category", categoryid)
        } else if (parent == incident_event) {
            eventid = eventlist.get(position).id
            Log.d("event", eventid)
        } else if (parent == incident_reason) {
            reason = reasonlist.get(position).reason
            reasonid = reasonlist.get(position).reason_id
            Log.d("reasonid", reasonid)
            Log.d("reason", reason)
            //call api for sub reason list
            GetALlSubReasonstList()
        } else if (parent == incident_subreason) {
            subreason = subreasonlist.get(position).sub_reason
            sub_reasonid = subreasonlist.get(position).sub_reason_id
            Log.d("subreason", subreason)
            Log.d("sub_reasonid", sub_reasonid)

        } else if (parent == incident_services) {
            services = servicelist.get(position).name
            servicesid = servicelist.get(position).id
            Log.d("services", services)
            Log.d("servicesid", servicesid)

        } else if (parent == incident_qualifications) {
            if (incident_qualifications.selectedItem.toString().equals(resources.getString(R.string.low))) {
                urgency_value = "4"
            } else if (incident_qualifications.selectedItem.toString().equals(resources.getString(R.string.medium))) {
                urgency_value = "3"
            } else if (incident_qualifications.selectedItem.toString().equals(resources.getString(R.string.high))) {
                urgency_value = "2"
            } else if (incident_qualifications.selectedItem.toString().equals(resources.getString(R.string.critical))) {
                urgency_value = "1"
            }
            Log.d("urgency_value", urgency_value)

        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        if (p0 == g2_checkbox) {
            if (p1 == true) {
                technology1 = "2G"
            } else if (p1 == false) {
                technology1 = ""
            }
        } else if (p0 == g3_checkbox) {
            if (p1 == true) {
                technology2 = "3G"
            } else if (p1 == false) {
                technology2 = ""
            }
        } else if (p0 == g4_checkbox) {
            if (p1 == true) {
                technology3 = "4G"
            } else if (p1 == false) {
                technology3 = ""
            }
        } else if (p0 == internationcall_checkbox) {
            if (p1 == true) {
                serviceaffacted1 = "1"
            } else if (p1 == false) {
                serviceaffacted1 = ""
            }
        } else if (p0 == voicemail_checkbox) {
            if (p1 == true) {
                serviceaffacted2 = "2"
            } else if (p1 == false) {
                serviceaffacted2 = ""
            }
        } else if (p0 == dice_checkbox) {
            if (p1 == true) {
                serviceaffacted3 = "3"
            } else if (p1 == false) {
                serviceaffacted3 = ""
            }
        } else if (p0 == face_checkbox) {
            if (p1 == true) {
                serviceaffacted4 = "4"
            } else if (p1 == false) {
                serviceaffacted4 = ""
            }
        } else if (p0 == prepaid_checkbox) {
            if (p1 == true) {
                serviceaffacted5 = "5"
            } else if (p1 == false) {
                serviceaffacted5 = ""
            }
        } else if (p0 == sms_checkbox) {
            if (p1 == true) {
                serviceaffacted6 = "6"
            } else if (p1 == false) {
                serviceaffacted6 = ""
            }
        } else if (p0 == supervise_checkbox) {
            if (p1 == true) {
                serviceaffacted7 = "7"
            } else if (p1 == false) {
                serviceaffacted7 = ""
            }
        } else if (p0 == callcentre_checkbox) {
            if (p1 == true) {
                serviceaffacted8 = "8"
            } else if (p1 == false) {
                serviceaffacted8 = ""
            }
        } else if (p0 == international_checkbox) {
            if (p1 == true) {
                serviceaffacted9 = "9"
            } else if (p1 == false) {
                serviceaffacted9 = ""
            }
        } else if (p0 == ussd_checkbox) {
            if (p1 == true) {
                serviceaffacted10 = "10"
            } else if (p1 == false) {
                serviceaffacted10 = ""
            }
        } else if (p0 == you_checkbox) {
            if (p1 == true) {
                serviceaffacted11 = "11"
            } else if (p1 == false) {
                serviceaffacted11 = ""
            }
        } else if (p0 == sales_checkbox) {
            if (p1 == true) {
                serviceaffacted12 = "12"
            } else if (p1 == false) {
                serviceaffacted12 = ""
            }
        } else if (p0 == voice_checkbox) {
            if (p1 == true) {
                serviceaffacted13 = "13"
            } else if (p1 == false) {
                serviceaffacted13 = ""
            }
        }

        technology = "$technology1,$technology2,$technology3";

        serviceaffacted =
            "$serviceaffacted1,$serviceaffacted2,$serviceaffacted3,$serviceaffacted4,$serviceaffacted5,$serviceaffacted6,$serviceaffacted7,$serviceaffacted8,$serviceaffacted9,$serviceaffacted10,$serviceaffacted11,$serviceaffacted12,$serviceaffacted13"

        val splitted: List<String> = serviceaffacted!!.split(",")
        val sb = StringBuffer()
        var retrieveData = ""
        for (i in splitted.indices) {
            retrieveData = splitted[i]
            if (retrieveData.trim { it <= ' ' }.length > 0) {
                if (i != 0) {
                    sb.append(",")
                }
                sb.append(retrieveData)
            }
        }

        val splitted1: List<String> = technology!!.split(",")
        val sb1 = StringBuffer()
        retrieveData = ""
        for (i in splitted1.indices) {
            retrieveData = splitted1[i]
            if (retrieveData.trim { it <= ' ' }.length > 0) {
                if (i != 0) {
                    sb1.append(",")
                }
                sb1.append(retrieveData)
            }
        }

        serviceaffacted = sb.toString()
        technology = sb1.toString()

        if (technology!!.endsWith(",")) {
            technology = technology!!.substring(0, technology!!.length - 1);
        }

        if (serviceaffacted!!.endsWith(",")) {
            serviceaffacted = serviceaffacted!!.substring(0, serviceaffacted!!.length - 1);
        }

        Log.d("test", "Tech : $technology")
        Log.d("test", "Service Affected : $serviceaffacted")
    }
}

// areaid : 33
// callerid : 140
// mon 5 from mob
// Service check
// text
// provinceid : 1
//reasonid : 1
//sub_reasonid : 1
//eventid : 1
//categoryid : 4
//servicesid 19
//urgency_value : 3
//serviceaffacted 6
//tech 3G
//NectarApplication.userID: "1"
//siteid : 4, 204, 335, 638, 640