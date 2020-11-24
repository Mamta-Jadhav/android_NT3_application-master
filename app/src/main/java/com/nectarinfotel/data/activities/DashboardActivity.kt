package com.nectarinfotel.data.activities

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nectarinfotel.R
import com.nectarinfotel.data.adapter.AgentAdapter
import com.nectarinfotel.data.adapter.DepartmentAdapter
import com.nectarinfotel.data.adapter.StatusDashboardAdapter
import com.nectarinfotel.data.jsonmodel.*
import com.nectarinfotel.data.model.OnItemClickInterface
import com.nectarinfotel.ui.login.LoginActivity
import com.nectarinfotel.utils.NectarApplication
import com.nectarinfotel.utils.PrefUtils
import kotlinx.android.synthetic.main.activity_dashboard.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    OnItemClickInterface, OnChartValueSelectedListener {

    override fun onNothingSelected() {
    }

    private var bitmap: Bitmap? = null
    private var isexception: Boolean? = false

    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {
        Log.i("Entry selected", e.toString())

        val x: Int = e!!.xIndex
        if (rightSpinnerValue.equals("status")) {
            var id = statusValueList.get(x)
            var title = statusValueList.get(x)
            DetailActivity.getStartIntent(
                this,
                rightSpinnerValue,
                leftSpinnerValue,
                id,
                title,
                langTranslator
            )
        } else if (rightSpinnerValue.equals("agent")) {

            var id = agentValueList1.get(x).agentId
            var title = agentValueList1.get(x).agent
            DetailActivity.getStartIntent(
                this,
                rightSpinnerValue,
                leftSpinnerValue,
                id,
                title,
                langTranslator
            )
        } else if (rightSpinnerValue.equals("department")) {

            var id = departmentValueList1.get(x).orgId
            var title = departmentValueList1.get(x).department
            DetailActivity.getStartIntent(
                this,
                rightSpinnerValue,
                leftSpinnerValue,
                id,
                title,
                langTranslator
            )
        }
        barChart.highlightValue(h)
    }

    override fun OnClick(status: String, title: String) {

        DetailActivity.getStartIntent(
            this,
            rightSpinnerValue,
            leftSpinnerValue,
            status,
            title,
            langTranslator
        )
    }

    private var id: String? = null;
    private var transss: String = "";
    private var TAG: String = DashboardActivity::class.java.simpleName

    private var statusValueList: MutableList<String> = mutableListOf()
    private var statusTotalList: MutableList<BarEntry> = mutableListOf()
    private var agentValueList: MutableList<String> = mutableListOf()
    private var agentValueList1: MutableList<AgenInfo> = mutableListOf()
    private var agentTotalList: MutableList<BarEntry> = mutableListOf()
    private var departmentValueList: MutableList<String> = mutableListOf()
    private var departmentValueList1: MutableList<DepartmentInfo> = mutableListOf()
    private var departmentTotalList: MutableList<BarEntry> = mutableListOf()
    private var leftSpinnerValue: Int = 1
    private var changeSpinnerValue: Int = 1
    var langTranslator: FirebaseTranslator? = null

    //    private var spinnerItemSelect: Int = 0
    private var changeSpinnerValue1: String = "ffff"
    private var rightSpinnerValue: String = "status"

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//        if (spinnerItemSelect == 0) {
//            spinnerItemSelect = 1
//        } else {
        if (parent == leftSpinner) {
            if (position == 0) {
                changeSpinner.visibility = View.GONE
                leftSpinnerValue = 1
                initializeRightSpinnerAdapter(resources.getStringArray(R.array.statusAgentDepartmentSpinner))
            } else if (position == 1) {
                changeSpinner.visibility = View.GONE
                leftSpinnerValue = 2
                initializeRightSpinnerAdapter(resources.getStringArray(R.array.statusAgentSpinner))
            } else {
                leftSpinnerValue = 3
                initializeRightSpinnerAdapter(resources.getStringArray(R.array.statusAgentDepartmentSpinner))
            }

        } else if (parent == changeSpinner) {
            if (position == 0) {
                changeSpinnerValue = 1
            } else if (position == 1) {
                changeSpinnerValue = 2
            } else if (position == 2) {
                changeSpinnerValue = 3
            }

        } else {

            if (leftSpinnerValue == 1) {
                rightSpinnerValue =
                    resources.getStringArray(R.array.statusAgentDepartmentSpinner)[position]
            } else if (leftSpinnerValue == 3) {
                rightSpinnerValue =
                    resources.getStringArray(R.array.statusAgentDepartmentSpinner)[position]
            } else {
                rightSpinnerValue =
                    resources.getStringArray(R.array.statusAgentSpinner)[position]
            }

        }

        //call api for fetch data according to categories
        Log.d("test", "On item selected")
        apiCall()
//        }
    }

    companion object {
        fun startIntent(context: Context): Intent {
            return Intent(context, DashboardActivity::class.java)
        }

        fun loadBitmapFromView(v: View, width: Int, height: Int): Bitmap {
            val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.draw(c)
            return b
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nectarinfotel.R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        translateTextToEnglish("String")

        Log.d("test", " ${LoginActivity.language}")

        //change language validation
        if (LoginActivity.language.equals("Portuguese")) {
            val config = resources.configuration
            val locale = Locale("pt")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            newTextView.setText(R.string.new_incident)
        } else {
            val config = resources.configuration
            val locale = Locale("en")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            newTextView.setText(R.string.new_incident)
        }

        FirebaseApp.initializeApp(this)

        addincident_layout.setOnClickListener {
            val intent = Intent(this, NewIncidentActivity::class.java)
            intent.putExtra("category", leftSpinnerValue)
            startActivity(intent)
        }
        downloadReport.setOnClickListener {
            //we need to handle runtime permission for devices with marshmallow and above
            isexception = false
            requestReadPermissions()
            Log.d("size", " " + pdfview!!.width + "  " + pdfview!!.width)
            bitmap = loadBitmapFromView(pdfview!!, pdfview!!.width, pdfview!!.height)
            //export pdf using this method
            createPdf()
        }

        val bundle: Bundle? = intent.extras;
        id = bundle!!.getString("ID");
        // add swipe refresh functionality
        swipeRefresh.setOnRefreshListener {
            Log.d("test", "Swipe refresh")
            apiCall()
            swipeRefresh.isRefreshing = false
        }

        logout.setOnClickListener {
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //set data in recylerview accroding to status
        initializeRecyclerView()

        //set data into adapter for categories
        initLeftSpinnerResources()

        //set data into adapter for subcategories
        initRightSpinnerResources()

        //set data into adapter for change managment
        initChangeSpinnerResources()

    }

    private fun initializeRecyclerView() {
        recyclerView.hasFixedSize()
        recyclerView.layoutManager =
            LinearLayoutManager(this)
    }

    private fun initLeftSpinnerResources() {
        leftSpinner.onItemSelectedListener = this

        //Creating the ArrayAdapter instance having the country list

        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.openIncidentsSpinner)
        )
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        leftSpinner.adapter = aa

    }


    private fun initRightSpinnerResources() {
        rightSpinner.onItemSelectedListener = this
        initializeRightSpinnerAdapter(resources.getStringArray(R.array.statusAgentDepartmentSpinner))
    }

    private fun initChangeSpinnerResources() {
        changeSpinner.onItemSelectedListener = this
        initializeChangeSpinnerAdapter(resources.getStringArray(R.array.ChangesSpinner))
    }

    private fun initializeRightSpinnerAdapter(statusAgentDepartmentSpinner: Array<String>) {
        //Creating the ArrayAdapter instance having the country list
        val aa =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, statusAgentDepartmentSpinner)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        rightSpinner.adapter = aa

    }

    private fun initializeChangeSpinnerAdapter(ChangesSpinner: Array<String>) {
        //Creating the ArrayAdapter instance having the country list
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, ChangesSpinner)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        changeSpinner.adapter = aa
    }

    fun apiCall() {
        if (rightSpinnerValue.equals("Area")) {
            rightSpinnerValue = "department"
        } else if (rightSpinnerValue.equals("estado")) {
            rightSpinnerValue = "status"
        } else if (rightSpinnerValue.equals("Agente")) {
            rightSpinnerValue = "agent"
        } else if (rightSpinnerValue.equals("Área")) {
            rightSpinnerValue = "department"
        }
        Log.d(
            "test",
            "Call Dashboard API : " + leftSpinnerValue.toString() + " " + rightSpinnerValue + " " + id
        )
        var call: Call<JsonObject>? = null

        if (PrefUtils.getORG(this) != null) {
            if (PrefUtils.getORG(this) == "movicel") {
                call = NectarApplication.mRetroClient1!!.callDashboardAPI(
                    leftSpinnerValue.toString(),
                    rightSpinnerValue,
                    id
                )
            } else {
                call = NectarApplication.mRetroClient!!.callDashboardAPI(
                    leftSpinnerValue.toString(),
                    rightSpinnerValue,
                    id
                )
            }
        }
        call?.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                val rsp: JsonObject? = response.body() ?: return
                Log.d("responbse", "" + response.body().toString())
                if (rsp!!.has("flag") && rsp?.get("flag").toString() == "true") {

                    if (rightSpinnerValue.equals("agent", ignoreCase = true)) {
                        val agentResponse =
                            Gson().fromJson<AgentResponse>(rsp, AgentResponse::class.java)

                        agentResponse.info.reverse()
                        initializeAgentAdapter(agentResponse.info)
                        setBarValuesForAgents(agentResponse.info)
                        Log.d("test", "agent")
                        if (LoginActivity.language.equals("Portuguese")) {
                            Handler().postDelayed({
                                setBarChart(
                                    agentTotalList as ArrayList<BarEntry>,
                                    agentValueList as ArrayList<String>
                                )
                            }, 500)
                        } else {
                            setBarChart(
                                agentTotalList as ArrayList<BarEntry>,
                                agentValueList as ArrayList<String>
                            )
                        }

                    } else if (rightSpinnerValue.equals("agente", ignoreCase = true)) {
                        val agentResponse =
                            Gson().fromJson<AgentResponse>(rsp, AgentResponse::class.java)

                        agentResponse.info.reverse()
                        initializeAgentAdapter(agentResponse.info)
                        setBarValuesForAgents(agentResponse.info)
                        Log.d("test", "agente")
                        if (LoginActivity.language.equals("Portuguese")) {
                            Handler().postDelayed({
                                setBarChart(
                                    agentTotalList as ArrayList<BarEntry>,
                                    agentValueList as ArrayList<String>
                                )
                            }, 500)
                        } else {
                            setBarChart(
                                agentTotalList as ArrayList<BarEntry>,
                                agentValueList as ArrayList<String>
                            )
                        }
                    } else if (rightSpinnerValue.equals("status", ignoreCase = true)) {
                        val statusResponse =
                            Gson().fromJson<StatusResponse>(rsp, StatusResponse::class.java)

                        if (statusResponse.info != null) {
                            initializeStatusAdapter(statusResponse.info)
                            //set value in barchart for status
                            Log.d("test", "status")
                            setBarValuesForStatus(statusResponse.info)
                            if (LoginActivity.language.equals("Portuguese")) {
                                Handler().postDelayed({
                                    setBarChart(
                                        statusTotalList as ArrayList<BarEntry>,
                                        statusValueList as ArrayList<String>
                                    )
                                }, 500)
                            } else {
                                setBarChart(
                                    statusTotalList as ArrayList<BarEntry>,
                                    statusValueList as ArrayList<String>
                                )
                            }
                        }

                    } else if (rightSpinnerValue.equals("department", ignoreCase = true)) {
                        val departmentResponse =
                            Gson().fromJson<DepartmentResponse>(rsp, DepartmentResponse::class.java)

                        departmentResponse.info.reverse()
                        initializeDepartmentAdapter(departmentResponse.info)
                        //set value in barchart for department
                        setBarValuesForDepartment(departmentResponse.info)
                        Log.d("test", "department")
                        if (LoginActivity.language.equals("Portuguese")) {
                            Handler().postDelayed({
                                setBarChart(
                                    departmentTotalList as ArrayList<BarEntry>,
                                    departmentValueList as ArrayList<String>
                                )
                            }, 500)
                        } else {
                            setBarChart(
                                departmentTotalList as ArrayList<BarEntry>,
                                departmentValueList as ArrayList<String>
                            )
                        }
                    } else if (rightSpinnerValue.equals("departamento", ignoreCase = true)) {
                        val departmentResponse =
                            Gson().fromJson<DepartmentResponse>(rsp, DepartmentResponse::class.java)

                        departmentResponse.info.reverse()
                        initializeDepartmentAdapter(departmentResponse.info)
                        //set value in barchart for department
                        setBarValuesForDepartment(departmentResponse.info)
                        Log.d("test", "departmento")
                        if (LoginActivity.language.equals("Portuguese")) {
                            Handler().postDelayed({
                                setBarChart(
                                    departmentTotalList as ArrayList<BarEntry>,
                                    departmentValueList as ArrayList<String>
                                )
                            }, 500)
                        } else {
                            setBarChart(
                                departmentTotalList as ArrayList<BarEntry>,
                                departmentValueList as ArrayList<String>
                            )
                        }
                    }
                } else {
                    val dialog = Dialog(this@DashboardActivity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setCancelable(false)
                    dialog.setContentView(R.layout.layout_no_records)
                    val yesBtn = dialog.findViewById(R.id.ok) as TextView
                    yesBtn.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.no_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setBarValuesForStatus(info: List<StatusInfo>) {
        if (info.isEmpty()) return
        var totalAgentTicket: Int = 0
        statusValueList.clear()
        statusTotalList.clear()
        for ((start, statusObj) in info.withIndex()) {

            if (statusObj.status != null) {
                if (LoginActivity.language.equals("Portuguese")) {

                    langTranslator!!.translate(statusObj.status)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            statusValueList.add(translatedText)
                        }
                } else {
                    statusValueList.add(statusObj.status)
                }

                Log.d("test", "Heeeeeeeeeeeeeeeeeerrrrrrrrrrreeeeeeee ${statusValueList.size}")
                val total = statusObj.total.toFloat()
                statusTotalList.add(BarEntry(total, start))
                totalAgentTicket += total.toInt()

                TotalTextView.setText(resources.getString(R.string.total_count) + totalAgentTicket)
            }
        }
    }

    private fun setBarValuesForDepartment(info: List<DepartmentInfo>) {
        if (info.isEmpty()) return
        var totalAgentTicket: Int = 0
        departmentTotalList.clear()
        departmentValueList.clear()
        for ((start, departmentObj) in info.withIndex()) {
            if (departmentObj.department != null) {
                if (LoginActivity.language.equals("Portuguese")) {
                    langTranslator!!.translate(departmentObj.department)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            departmentValueList.add(translatedText)
                        }
                } else {
                    departmentValueList.add(departmentObj.department)
                }
                val total = departmentObj.tickets.toFloat()
                departmentTotalList.add(BarEntry(total, start))
                totalAgentTicket += total.toInt()
                TotalTextView.setText(resources.getString(R.string.total_count) + totalAgentTicket)
            }
        }
    }

    private fun setBarValuesForAgents(info: List<AgenInfo>) {
        if (info.isEmpty()) return
        var totalAgentTicket: Int = 0
        agentTotalList.clear()
        agentValueList.clear()

        for ((start, agentObj) in info.withIndex()) {

            if (agentObj.agent != null) {
                if (LoginActivity.language.equals("Portuguese")) {
                    langTranslator!!.translate(agentObj.agent)
                        .addOnSuccessListener { translatedText ->
                            Log.d("test", translatedText)
                            agentValueList.add(translatedText)
                        }
                } else {
                    agentValueList.add(agentObj.agent)
                }
                val total = agentObj.tickets.toFloat()
                agentTotalList.add(BarEntry(total, start))
                totalAgentTicket += total.toInt()

                TotalTextView.setText(resources.getString(R.string.total_count) + totalAgentTicket)
            }
        }
    }

    private fun initializeStatusAdapter(info: MutableList<StatusInfo>) {
        recyclerView.adapter = StatusDashboardAdapter(info, this, langTranslator)
        (recyclerView.adapter as StatusDashboardAdapter).notifyDataSetChanged()
    }

    private fun initializeDepartmentAdapter(info: MutableList<DepartmentInfo>) {
        departmentValueList1.addAll(info.filterNotNull())
        recyclerView.adapter = DepartmentAdapter(info, this, langTranslator)
        (recyclerView.adapter as DepartmentAdapter).notifyDataSetChanged()
    }

    private fun initializeAgentAdapter(info: MutableList<AgenInfo>) {
        agentValueList1.addAll(info.filterNotNull())
        recyclerView.adapter = AgentAdapter(info, this, langTranslator)
        (recyclerView.adapter as AgentAdapter).notifyDataSetChanged()
    }

    private fun setBarChart(entries: ArrayList<BarEntry>, labels: ArrayList<String>) {

        Log.d("test", "Entries : ${entries.size}")
        Log.d("test", "Labels : ${labels.size}")
        val listWithoutDuplicates: Set<String> = LinkedHashSet<String>(labels)
        labels.clear()
        labels.addAll(listWithoutDuplicates)
        val barDataSet = BarDataSet(entries, "")
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart

        barChart.setDescription("")  // set the description

        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        // barDataSet.color = Color.BLUE

        barChart.animateY(5000)
        var xaxis = XAxis()
        xaxis = barChart.xAxis
        xaxis.setLabelRotationAngle(90F);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // barChart.data.setValueFormatter( PercentFormatter());
        xaxis.spaceBetweenLabels = 1
        barChart.getLegend().setEnabled(false);
        barChart.setOnChartValueSelectedListener(this)
        // barChart.setPinchZoom(true)

        downloadReport.visibility = View.VISIBLE
    }

    private fun requestReadPermissions() {

        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {

                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // show alert dialog navigating to Settings

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError) {
                    Toast.makeText(applicationContext, "Some Error! ", Toast.LENGTH_SHORT).show()
                }
            })
            .onSameThread()
            .check()
    }

    private fun createPdf() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displaymetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displaymetrics)
        val hight = displaymetrics.heightPixels.toFloat()
        val width = displaymetrics.widthPixels.toFloat()

        val convertHighet = hight.toInt()
        val convertWidth = width.toInt()

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint)

        bitmap = Bitmap.createScaledBitmap(bitmap!!, convertWidth, convertHighet, true)

        paint.color = Color.WHITE
        canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        document.finishPage(page)

        // write the document content
        val sdf = SimpleDateFormat("dd_M_yyyy_hh:mm:ss")
        val currentDate = sdf.format(Date())
        System.out.println(" C DATE is  " + currentDate)
        // write the document content

        var targetPdf: String = "/storage/emulated/0//" + currentDate + "nt3.pdf"
        Log.d("target", targetPdf)
        val filePath: File
        filePath = File(targetPdf)
        try {
            document.writeTo(FileOutputStream(filePath))

        } catch (e: IOException) {
            e.printStackTrace()
            isexception = true
            Toast.makeText(this, resources.getText(R.string.permission), Toast.LENGTH_LONG).show()
        }

        // close the document
        document.close()
        if (isexception == false) {
            val snack =
                Snackbar.make(
                    snackbar_view,
                    resources.getText(R.string.download),
                    Snackbar.LENGTH_INDEFINITE
                )
            snack.setActionTextColor(Color.YELLOW)
            snack.setAction(resources.getText(R.string.Share), View.OnClickListener {
                // executed when DISMISS is clicked
                System.out.println("Snackbar Set Action - OnClick.")
                sharefile(targetPdf)
            })

            snack.setDuration(8000)
            snack.show()

        }
    }

    fun sharefile(targetPdf: String) {
        var pdf = File(targetPdf);
        val intent = Intent();
        intent.setAction(Intent.ACTION_SEND);
        val contentUri = FileProvider.getUriForFile(this, "com.nectarinfotel" + ".provider", pdf)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri);
        intent.setType("application/pdf");
        startActivity(intent);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.english -> {
                LoginActivity.language = "english"
                val config = resources.configuration
                val locale = Locale("en")
                Locale.setDefault(locale)
                config.locale = locale
                resources.updateConfiguration(config, resources.displayMetrics)
                newTextView.setText(R.string.new_incident)
                statusValueList.clear();
                statusTotalList.clear();
                departmentValueList.clear();
                departmentTotalList.clear();
                agentValueList.clear();
                agentTotalList.clear();
                initializeRecyclerView()
                initLeftSpinnerResources()
                initRightSpinnerResources()
                initChangeSpinnerResources()
                return true
            }
            R.id.pourtuguese -> {
                LoginActivity.language = "Portuguese"
                val config = resources.configuration
                val locale = Locale("pt")
                Locale.setDefault(locale)
                config.locale = locale
                resources.updateConfiguration(config, resources.displayMetrics)
                newTextView.setText(R.string.new_incident)
                statusValueList.clear();
                statusTotalList.clear();
                departmentValueList.clear();
                departmentTotalList.clear();
                agentValueList.clear();
                agentTotalList.clear();
                initializeRecyclerView()
                initLeftSpinnerResources()
                initRightSpinnerResources()
                initChangeSpinnerResources()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun translate(str: String): String {
        return MyAsyncTask(this, langTranslator).execute(str).get();
//        Handler().postDelayed({
//            MyAsyncTask(this, langTranslator).execute(str).get();
//        }, 2000)
//        return transss;
    }

    fun translateText(langTranslator: FirebaseTranslator, str: String): String {

        var translated: String = ""
        //translate source text to english
        langTranslator.translate(str)
            .addOnSuccessListener { translatedText ->
                Log.d("test", translatedText)
                translated = translatedText
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Problem in translating the text entered",
                    Toast.LENGTH_LONG
                ).show()
            }

        return translated
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
                        this@DashboardActivity,
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

    fun translateTextToEnglish1(str: String): String {
        //First identify the language of the entered text

        val languageIdentifier: FirebaseLanguageIdentification =
            FirebaseNaturalLanguage.getInstance().languageIdentification
        Log.d("languageIdentifier", "languageIdentifier  $languageIdentifier")
        languageIdentifier.identifyLanguage(str)
            .addOnSuccessListener { languageCode ->
                Log.d("test", languageCode)
                if (languageCode == "en") {
                    Log.d("translator", "lang $languageCode")
                    //download translator for the identified language
                    // and translate the entered text into english

                    val sourceLanguage = FirebaseTranslateLanguage
                        .languageForLanguageCode(languageCode)!!

                    //create translator for source and target languages
                    val options = FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguage)
                        .setTargetLanguage(FirebaseTranslateLanguage.PT)
                        .build()

                    val langTranslator: FirebaseTranslator =
                        FirebaseNaturalLanguage.getInstance().getTranslator(options)

                    //download language models if needed
                    val conditions = FirebaseModelDownloadConditions.Builder()
                        .requireWifi()
                        .build()
                    langTranslator.downloadModelIfNeeded()
                        .addOnSuccessListener {
                            Log.d("translator", "downloaded lang  model")
                            //after making sure language models are available
                            //perform translation
                            langTranslator.translate(str)
                                .addOnSuccessListener { translatedText ->
                                    Log.d("test", translatedText)

                                    transss = translatedText
                                }

                                .addOnFailureListener { exception ->
                                    Toast.makeText(
                                        this,
                                        "Problem in translating the text entered",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }

                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this,
                                "Problem in translating the text entered",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                } else {
                    Toast.makeText(
                        this@DashboardActivity,
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

        return transss;
    }

    class MyAsyncTask internal constructor(
        context: DashboardActivity,
        langTranslator: FirebaseTranslator?
    ) :
        AsyncTask<String, String, String>() {

        private var transss: String? = null
        private val activityReference: WeakReference<DashboardActivity> = WeakReference(context)
        private val langTranslator = langTranslator

        override fun onPreExecute() {
        }

        override fun doInBackground(vararg params: String): String? {

//            val languageIdentifier: FirebaseLanguageIdentification =
//                FirebaseNaturalLanguage.getInstance().languageIdentification
//            Log.d("languageIdentifier", "languageIdentifier  $languageIdentifier")
//            Log.d("languageIdentifier", "Params:  ${params[0]}")
//            languageIdentifier.identifyLanguage(params[0])
//                .addOnSuccessListener { languageCode ->
//                    Log.d("test", languageCode)
//                    if (languageCode == "en") {
//                        Log.d("translator", "lang $languageCode")
//                        //download translator for the identified language
//                        // and translate the entered text into english
//
//                        val sourceLanguage = FirebaseTranslateLanguage
//                            .languageForLanguageCode(languageCode)!!
//
//                        //create translator for source and target languages
//                        val options = FirebaseTranslatorOptions.Builder()
//                            .setSourceLanguage(sourceLanguage)
//                            .setTargetLanguage(FirebaseTranslateLanguage.PT)
//                            .build()
//
//                        val langTranslator: FirebaseTranslator =
//                            FirebaseNaturalLanguage.getInstance().getTranslator(options)
//
//                        //download language models if needed
//                        val conditions = FirebaseModelDownloadConditions.Builder()
//                            .requireWifi()
//                            .build()
//            langTranslator!!.downloadModelIfNeeded()
//                .addOnSuccessListener {
            Log.d("translator", "downloaded lang  model")
            //after making sure language models are available
            //perform translation
            langTranslator!!.translate(params[0])
                .addOnSuccessListener { translatedText ->
                    Log.d("test", translatedText)

                    transss = translatedText
                }
//
//                        .addOnFailureListener { exception ->
//                            Log.d("test", "Problem in translating the text entered")
//                        }
//                }
//
//                .addOnFailureListener { exception ->
//                    Log.d("test", "Problem in translating the text entered")
//                }
//                    } else {
//                        Log.d("test", "Could not identify language of the text entered")
//                    }
//                }
//
//                .addOnFailureListener { exception ->
//                    Log.d("test", "Problem in identifying language of the text entered")
//                }

            return transss
        }

        override fun onPostExecute(result: String?) {

        }

        override fun onProgressUpdate(vararg text: String?) {

        }
    }
}