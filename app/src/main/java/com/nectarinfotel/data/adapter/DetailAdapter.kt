package com.nectarinfotel.data.adapter

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.nectarinfotel.R
import com.nectarinfotel.data.jsonmodel.DetailInfo
import com.nectarinfotel.ui.login.LoginActivity
import kotlinx.android.extensions.LayoutContainer

class DetailAdapter(langTranslate: FirebaseTranslator?, itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<DetailAdapter.ItemViewHolder>() {
    var text: String = ""
    var title: String = ""
    var Caller: String = ""
    var Agent: String = ""
    var Start_Date: String = ""
    var Priority: String = ""
    var Status: String = ""
    var Critical: String = ""
    var High: String = ""
    var Medium: String = ""
    var Low: String = ""
    private var langTranslate: FirebaseTranslator? = langTranslate
    var itemClickListener: OnItemClickListener = itemClickListener

    private var detailList: MutableList<DetailInfo> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.detail_recylcerview_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return detailList.size
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, pos: Int) {
        //get data from string class
        text = itemViewHolder.itemView.getContext().getString(R.string.ticket_no)
        title = itemViewHolder.itemView.getContext().getString(R.string.title)
        Caller = itemViewHolder.itemView.getContext().getString(R.string.Caller)
        Agent = itemViewHolder.itemView.getContext().getString(R.string.Agent)
        Start_Date = itemViewHolder.itemView.getContext().getString(R.string.Start_Date)
        Priority = itemViewHolder.itemView.getContext().getString(R.string.Priority)
        Status = itemViewHolder.itemView.getContext().getString(R.string.status)
        Critical = itemViewHolder.itemView.getContext().getString(R.string.Critical)
        High = itemViewHolder.itemView.getContext().getString(R.string.High)
        Medium = itemViewHolder.itemView.getContext().getString(R.string.Medium)
        Low = itemViewHolder.itemView.getContext().getString(R.string.Low)
        itemViewHolder.bind(
            detailList[pos],
            text,
            title,
            Caller,
            Agent,
            Start_Date,
            Priority,
            Status,
            Critical,
            High,
            Medium,
            Low,
            langTranslate, itemClickListener
        )
    }

    fun updateList(list: MutableList<DetailInfo>) {
        this.detailList = list
        notifyDataSetChanged()
    }

    class ItemViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView!!),
        LayoutContainer {

        fun bind(
            detailInfo: DetailInfo,
            text: String,
            title: String,
            caller: String,
            agent: String,
            startDate: String,
            priority: String,
            status: String,
            critical: String,
            high: String,
            medium: String,
            low: String, langTranslate: FirebaseTranslator?, clickListener: OnItemClickListener
        ) {

            itemView.setOnClickListener {
                clickListener.onItemClicked(detailInfo)
            }

            if (LoginActivity.language.equals("Portuguese")) {

                langTranslate!!.translate(title)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView?.findViewById<TextView>(R.id.titleContainerTextView)!!.text =
                            translatedText
                    }
                langTranslate!!.translate(caller)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView?.findViewById<TextView>(R.id.callerContainerTextView)!!.text =
                            translatedText
                    }
                langTranslate!!.translate(agent)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView?.findViewById<TextView>(R.id.agentContainerTextView)!!.text =
                            translatedText
                    }
                langTranslate!!.translate(startDate)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView?.findViewById<TextView>(R.id.dateContainerTextView)!!.text =
                            translatedText
                    }
                langTranslate!!.translate(priority)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView?.findViewById<TextView>(R.id.priorityContainerTextView)!!.text =
                            translatedText
                    }
                langTranslate!!.translate(status)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView?.findViewById<TextView>(R.id.statusContainerTextView)!!.text =
                            translatedText
                    }
                langTranslate!!.translate(detailInfo.ticketid)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView!!.findViewById<TextView>(R.id.ticketNumberTextView).text =
                            text.plus(translatedText)
                    }
                langTranslate!!.translate(detailInfo.department)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView!!.findViewById<TextView>(R.id.departmentTextView).text =
                            translatedText
                    }
                langTranslate!!.translate(detailInfo.title)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView!!.findViewById<TextView>(R.id.titleValueTextView).text =
                            translatedText
                    }
                langTranslate!!.translate(detailInfo.caller)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView!!.findViewById<TextView>(R.id.callerValueTextView).text =
                            translatedText
                    }
                langTranslate!!.translate(detailInfo.agent)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView!!.findViewById<TextView>(R.id.agentValueTextView).text =
                            translatedText
                    }

                if (detailInfo.startDate != null) {
                    containerView!!.findViewById<TextView>(R.id.dateContainerTextView).visibility =
                        View.VISIBLE
                    containerView.findViewById<TextView>(R.id.dateValueTextView).visibility =
                        View.VISIBLE
                    langTranslate!!.translate(detailInfo.startDate)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            containerView!!.findViewById<TextView>(R.id.dateValueTextView).text =
                                translatedText
                        }

                } else {
                    containerView!!.findViewById<TextView>(R.id.dateContainerTextView).visibility =
                        View.GONE
                    containerView.findViewById<TextView>(R.id.dateValueTextView).visibility =
                        View.GONE
                }
                // containerView.findViewById<TextView>(R.id.priorityValueTextView).text = detailInfo.priority
                if (detailInfo.priority != null) {
                    containerView.findViewById<TextView>(R.id.priorityContainerTextView).visibility =
                        View.VISIBLE
                    containerView.findViewById<TextView>(R.id.priorityValueTextView).visibility =
                        View.VISIBLE
                    Log.d("department", detailInfo.agent)
                    if (detailInfo.priority.equals("1")) {
                        langTranslate!!.translate(critical)
                            .addOnSuccessListener { translatedText ->
                                Log.d("test", translatedText)
                                containerView!!.findViewById<TextView>(R.id.priorityValueTextView).text =
                                    translatedText
                            }
                    } else if (detailInfo.priority.equals("2")) {
                        langTranslate!!.translate(high)
                            .addOnSuccessListener { translatedText ->
                                Log.d("test", translatedText)
                                containerView!!.findViewById<TextView>(R.id.priorityValueTextView).text =
                                    translatedText
                            }
                    } else if (detailInfo.priority.equals("3")) {
                        langTranslate!!.translate(medium)
                            .addOnSuccessListener { translatedText ->
                                Log.d("test", translatedText)
                                containerView!!.findViewById<TextView>(R.id.priorityValueTextView).text =
                                    translatedText
                            }
                    } else if (detailInfo.priority.equals("4")) {
                        langTranslate!!.translate(low)
                            .addOnSuccessListener { translatedText ->
                                Log.d("test", translatedText)
                                containerView!!.findViewById<TextView>(R.id.priorityValueTextView).text =
                                    translatedText
                            }
                    }
                } else {
                    containerView.findViewById<TextView>(R.id.priorityValueTextView).visibility =
                        View.GONE
                    containerView.findViewById<TextView>(R.id.priorityContainerTextView).visibility =
                        View.GONE
                }
                langTranslate!!.translate(detailInfo.status)
                    .addOnSuccessListener { translatedText ->
                        Log.d("test", translatedText)
                        containerView!!.findViewById<TextView>(R.id.statusValueTextView).text =
                            translatedText
                    }

            } else {
                containerView?.findViewById<TextView>(R.id.titleContainerTextView)!!.text = title
                containerView?.findViewById<TextView>(R.id.callerContainerTextView).text = caller
                containerView?.findViewById<TextView>(R.id.agentContainerTextView).text = agent
                containerView?.findViewById<TextView>(R.id.dateContainerTextView).text = startDate
                containerView?.findViewById<TextView>(R.id.priorityContainerTextView).text =
                    priority
                containerView?.findViewById<TextView>(R.id.statusContainerTextView).text = status

                containerView!!.findViewById<TextView>(R.id.ticketNumberTextView).text =
                    text.plus(detailInfo.ticketid)
                containerView.findViewById<TextView>(R.id.departmentTextView).text =
                    detailInfo.department
                containerView.findViewById<TextView>(R.id.titleValueTextView).text =
                    detailInfo.title
                containerView.findViewById<TextView>(R.id.callerValueTextView).text =
                    detailInfo.caller
                containerView.findViewById<TextView>(R.id.agentValueTextView).text =
                    detailInfo.agent
                if (detailInfo.startDate != null) {
                    containerView.findViewById<TextView>(R.id.dateContainerTextView).visibility =
                        View.VISIBLE
                    containerView.findViewById<TextView>(R.id.dateValueTextView).visibility =
                        View.VISIBLE
                    containerView.findViewById<TextView>(R.id.dateValueTextView).text =
                        detailInfo.startDate
                } else {
                    containerView.findViewById<TextView>(R.id.dateContainerTextView).visibility =
                        View.GONE
                    containerView.findViewById<TextView>(R.id.dateValueTextView).visibility =
                        View.GONE
                }
                // containerView.findViewById<TextView>(R.id.priorityValueTextView).text = detailInfo.priority
                if (detailInfo.priority != null) {
                    containerView.findViewById<TextView>(R.id.priorityContainerTextView).visibility =
                        View.VISIBLE
                    containerView.findViewById<TextView>(R.id.priorityValueTextView).visibility =
                        View.VISIBLE
                    Log.d("department", detailInfo.agent)
                    if (detailInfo.priority.equals("1")) {
                        containerView.findViewById<TextView>(R.id.priorityValueTextView).text =
                            critical
                    } else if (detailInfo.priority.equals("2")) {
                        containerView.findViewById<TextView>(R.id.priorityValueTextView).text = high
                    } else if (detailInfo.priority.equals("3")) {
                        containerView.findViewById<TextView>(R.id.priorityValueTextView).text =
                            medium
                    } else if (detailInfo.priority.equals("4")) {
                        containerView.findViewById<TextView>(R.id.priorityValueTextView).text = low
                    }
                } else {
                    containerView.findViewById<TextView>(R.id.priorityValueTextView).visibility =
                        View.GONE
                    containerView.findViewById<TextView>(R.id.priorityContainerTextView).visibility =
                        View.GONE
                }
                containerView.findViewById<TextView>(R.id.statusValueTextView).text =
                    detailInfo.status
            }
        }
    }
}

interface OnItemClickListener{
    fun onItemClicked(detailInfo: DetailInfo)
}