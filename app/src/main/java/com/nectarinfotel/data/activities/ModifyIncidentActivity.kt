package com.nectarinfotel.data.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.nectarinfotel.R
import com.nectarinfotel.data.jsonmodel.DetailInfo
import com.nectarinfotel.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_detail_page.headerTextView
import kotlinx.android.synthetic.main.activity_modify_ticket.*
import java.util.*

class ModifyIncidentActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var text: String = ""
    var title: String = ""
    var caller: String = ""
    var agent: String = ""
    var startDate: String = ""
    var priority: String = ""
    var status: String = ""
    var critical: String = ""
    var high: String = ""
    var medium: String = ""
    var low: String = ""
    var selectedStatus: String = ""
    private var langTranslate: FirebaseTranslator? = null
    private var detailInfo: DetailInfo? = null
    val statuses = arrayOf(
        "assigned","closed","escalated_tto","escalated_ttr","new","pending","resolved"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_ticket)

        text = resources.getString(R.string.ticket_no)
        title = resources.getString(R.string.title)
        caller = resources.getString(R.string.Caller)
        agent = resources.getString(R.string.Agent)
        startDate = resources.getString(R.string.Start_Date)
        priority = resources.getString(R.string.Priority)
        status = resources.getString(R.string.status)
        critical = resources.getString(R.string.Critical)
        high = resources.getString(R.string.High)
        medium = resources.getString(R.string.Medium)
        low = resources.getString(R.string.Low)

        if (intent.hasExtra("info")) {

            detailInfo = intent.getParcelableExtra("info")
        }

        //Add validation for change language
        if (LoginActivity.language.equals("Portuguese")) {
            val config = resources.configuration
            val locale = Locale("pt")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            headerTextView.setText(text.plus(detailInfo!!.ticketid))
        } else {
            val config = resources.configuration
            val locale = Locale("en")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            headerTextView.setText(text.plus(detailInfo!!.ticketid))
        }

        text = resources.getString(R.string.ticket_no)
        title = resources.getString(R.string.title)
        caller = resources.getString(R.string.Caller)
        agent = resources.getString(R.string.Agent)
        startDate = resources.getString(R.string.Start_Date)
        priority = resources.getString(R.string.Priority)
        status = resources.getString(R.string.status)
        critical = resources.getString(R.string.Critical)
        high = resources.getString(R.string.High)
        medium = resources.getString(R.string.Medium)
        low = resources.getString(R.string.Low)

        if (LoginActivity.language.equals("Portuguese")) {

            langTranslate!!.translate(title)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    titleContainerTextView.text =
                        translatedText
                }
            langTranslate!!.translate(caller)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    callerContainerTextView.text =
                        translatedText
                }
            langTranslate!!.translate(agent)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    agentContainerTextView.text =
                        translatedText
                }
            langTranslate!!.translate(startDate)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    dateContainerTextView.text =
                        translatedText
                }
            langTranslate!!.translate(priority)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    priorityContainerTextView.text =
                        translatedText
                }
            langTranslate!!.translate(status)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    statusContainerTextView.text =
                        translatedText
                }
            langTranslate!!.translate(detailInfo!!.ticketid)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    ticketNumberTextView.text =
                        text.plus(translatedText)
                }
            langTranslate!!.translate(detailInfo!!.department)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    departmentTextView.text =
                        translatedText
                }
            langTranslate!!.translate(detailInfo!!.title)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    titleValueTextView.text =
                        translatedText
                }
            langTranslate!!.translate(detailInfo!!.caller)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    callerValueTextView.text =
                        translatedText
                }
            langTranslate!!.translate(detailInfo!!.agent)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    agentValueTextView.text =
                        translatedText
                }

            if (detailInfo!!.startDate != null) {
                dateContainerTextView.visibility =
                    View.VISIBLE
                dateValueTextView.visibility =
                    View.VISIBLE
                langTranslate!!.translate(detailInfo!!.startDate)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        dateValueTextView.text =
                            translatedText
                    }

            } else {
                dateContainerTextView.visibility =
                    View.GONE
                dateValueTextView.visibility =
                    View.GONE
            }
            // priorityValueTextView.text = detailInfo!!.priority
            if (detailInfo!!.priority != null) {
                priorityContainerTextView.visibility =
                    View.VISIBLE
                priorityValueTextView.visibility =
                    View.VISIBLE
                Log.d("department", detailInfo!!.agent)
                if (detailInfo!!.priority.equals("1")) {
                    langTranslate!!.translate(critical)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            priorityValueTextView.text =
                                translatedText
                        }
                } else if (detailInfo!!.priority.equals("2")) {
                    langTranslate!!.translate(high)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            priorityValueTextView.text =
                                translatedText
                        }
                } else if (detailInfo!!.priority.equals("3")) {
                    langTranslate!!.translate(medium)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            priorityValueTextView.text =
                                translatedText
                        }
                } else if (detailInfo!!.priority.equals("4")) {
                    langTranslate!!.translate(low)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            priorityValueTextView.text =
                                translatedText
                        }
                }
            } else {
                priorityValueTextView.visibility =
                    View.GONE
                priorityContainerTextView.visibility =
                    View.GONE
            }
            langTranslate!!.translate(detailInfo!!.status)
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)
                    statusValueTextView.text =
                        translatedText
                }

        } else {
            titleContainerTextView.text = title
            callerContainerTextView.text = caller
            agentContainerTextView.text = agent
            dateContainerTextView.text = startDate
            priorityContainerTextView.text =
                priority
            statusContainerTextView.text = status

            ticketNumberTextView.text =
                text.plus(detailInfo!!.ticketid)
            departmentTextView.text =
                detailInfo!!.department
            titleValueTextView.text =
                detailInfo!!.title
            callerValueTextView.text =
                detailInfo!!.caller
            agentValueTextView.text =
                detailInfo!!.agent
            if (detailInfo!!.startDate != null) {
                dateContainerTextView.visibility =
                    View.VISIBLE
                dateValueTextView.visibility =
                    View.VISIBLE
                dateValueTextView.text =
                    detailInfo!!.startDate
            } else {
                dateContainerTextView.visibility =
                    View.GONE
                dateValueTextView.visibility =
                    View.GONE
            }
            // priorityValueTextView.text = detailInfo!!.priority
            if (detailInfo!!.priority != null) {
                priorityContainerTextView.visibility =
                    View.VISIBLE
                priorityValueTextView.visibility =
                    View.VISIBLE
                Log.d("department", detailInfo!!.agent)
                if (detailInfo!!.priority.equals("1")) {
                    priorityValueTextView.text =
                        critical
                } else if (detailInfo!!.priority.equals("2")) {
                    priorityValueTextView.text = high
                } else if (detailInfo!!.priority.equals("3")) {
                    priorityValueTextView.text =
                        medium
                } else if (detailInfo!!.priority.equals("4")) {
                    priorityValueTextView.text = low
                }
            } else {
                priorityValueTextView.visibility =
                    View.GONE
                priorityContainerTextView.visibility =
                    View.GONE
            }
            statusValueTextView.text =
                detailInfo!!.status
        }

        setStatus()
    }

    private fun setStatus() {
        incident_status.onItemSelectedListener = this

        //Creating the ArrayAdapter instance having the country list
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        incident_status.adapter = aa
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedStatus =  incident_status.selectedItem.toString()
    }
}