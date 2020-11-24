package com.nectarinfotel.data.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.JsonObject
import com.nectarinfotel.R
import com.nectarinfotel.utils.NectarApplication
import com.nectarinfotel.utils.PrefUtils
import kotlinx.android.synthetic.main.ticket_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketDetailsActivity : AppCompatActivity() {

    private var category: Int = 0
    var ticketid = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ticket_details)
        loading_ticketdetails.visibility = View.VISIBLE

        //handle notification
        val extras = getIntent().getExtras()
        if (null != extras) {
            val value = extras.getString("pushnotification")
            //The key argument here must match that used in the other activity
            if (value != null) {
                title = extras.getString("title")
                ticketid = extras.getString("ticket_id")
                ticketid = ticketid!!.drop(3)
                ticketid = ticketid.replace("^0*".toRegex(), "") // -> 123
                category = extras.getString("category").toInt()
                getticketdetails()
            }
        }


    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        loading_ticketdetails.visibility = View.VISIBLE

        val extras = getIntent().getExtras()
        if (null != extras) {
            val value = extras.getString("pushnotification")
            //The key argument here must match that used in the other activity
            if (value != null) {
                title = extras.getString("title")
                ticketid = extras.getString("ticket_id")
                ticketid = ticketid!!.drop(3)
                ticketid = ticketid.replace("^0*".toRegex(), "") // -> 123
                category = extras.getString("category").toInt()
                Log.e("ticketid", ticketid)
                getticketdetails()
            }
        }

    }

    private fun getticketdetails() {
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1!!.callTicketDetailAPI(
                    category, ticketid,
                    NectarApplication.userID
                )
            } else {
                call = NectarApplication.mRetroClient!!.callTicketDetailAPI(
                    category, ticketid,
                    NectarApplication.userID
                )
            }
        }

        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                loading_ticketdetails.visibility = View.GONE
                var rsp: JsonObject? = response.body() ?: return

                if (rsp!!.isJsonObject && rsp["info"] != null && rsp["info"].isJsonObject) {
                    var infoObj = rsp["info"]
                    var id = (rsp["info"] as JsonObject).get("id").toString().replace("\"", "");
                    var title =
                        (rsp["info"] as JsonObject).get("title").toString().replace("\"", "");
                    var caller =
                        (rsp["info"] as JsonObject).get("caller").toString().replace("\"", "");
                    var status =
                        (rsp["info"] as JsonObject).get("status").toString().replace("\"", "");
                    var priority =
                        (rsp["info"] as JsonObject).get("priority").toString().replace("\"", "");
                    var departmentvalue =
                        (rsp["info"] as JsonObject).get("department").toString().replace("\"", "");
                    var start_date =
                        (rsp["info"] as JsonObject).get("start_date").toString().replace("\"", "");
                    var agent_id =
                        (rsp["info"] as JsonObject).get("agent_id").toString().replace("\"", "");
                    var agent =
                        (rsp["info"] as JsonObject).get("agent").toString().replace("\"", "");

                    ticketNumber.text = "Ticket no : TT-000".plus(id)
                    titleValue.text = title
                    callerValue.text = caller
                    department.text = departmentvalue
                    dateValue.text = start_date
                    priorityValue.text = priority
                    statusValue.text = status

                    if (agent != null || !agent.equals("")) {
                        agent_lay.visibility = View.VISIBLE
                        agentValue.text = agent
                    } else {
                        agent_lay.visibility = View.GONE
                    }


                } else {
                    Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                loading_ticketdetails.visibility = View.GONE
                Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
