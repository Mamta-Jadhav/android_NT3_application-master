package com.nectarinfotel.ui.login

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.google.gson.JsonObject
import com.nectarinfotel.R
import com.nectarinfotel.data.activities.DashboardActivity
import com.nectarinfotel.utils.*
import kotlinx.android.synthetic.main.activity_login_new.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    public var mRegistrationBroadcastReceiver: BroadcastReceiver? = null
    public var userid: String? = null
    var regId = null
    var spinner: Spinner? = null
    private lateinit var loginViewModel: LoginViewModel
    private val sharedPrefFile = "kotlinsharedpreference"

    companion object {

        lateinit var langTranslater: FirebaseTranslator
        lateinit var language: String
        // public var BaseUrl: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login_new)

        translateTextToEnglish("String")

        Log.d("test", "ORG : ${PrefUtils.getORG(this)}")

        spinner = findViewById<Spinner>(R.id.spinner)
        var list_of_items = arrayOf("English", "Portuguese")
        spinner!!.setOnItemSelectedListener(this)

        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_items)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner!!.setAdapter(aa)

        websitelink.setText(Html.fromHtml(resources.getString(R.string.powerdedby) + "<a href='http://nectarinfotel.com/'> <font color='#FFFFFF'>Nectar Infotel Solution Pvt.Ltd.</font></a> "));
        websitelink.setMovementMethod(LinkMovementMethod.getInstance());// make it active
        registerfcmID()
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

        })
        setvalueforrememberme()
        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()

                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE

                Log.d("test", " Id : " + PrefUtils.getKey(this@LoginActivity, AppConstants.TokenID))

                var call: Call<JsonObject>? = null

                if (PrefUtils.getORG(this@LoginActivity) != null) {
                    if (PrefUtils.getORG(this@LoginActivity) == "movicel") {
                        call = NectarApplication.mRetroClient1!!.callLoginAPI(
                            username.text.toString(),
                            password.text.toString(),
                            PrefUtils.getKey(this@LoginActivity, AppConstants.TokenID)
                        )
                    } else {
                        call = NectarApplication.mRetroClient!!.callLoginAPI(
                            username.text.toString(),
                            password.text.toString(),
                            PrefUtils.getKey(this@LoginActivity, AppConstants.TokenID)
                        )
                    }
                }
                call?.enqueue(object : Callback<JsonObject> {
                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        loading.visibility = View.GONE
                        var rsp: JsonObject? = response.body() ?: return
                        Log.d("test", "Login response : " + response.body())
                        if (rsp!!.isJsonObject && rsp["info"] != null && rsp["info"].isJsonObject) {
                            var infoObj = rsp["info"]

                            var name = (rsp["info"] as JsonObject).get("name")
                            userid = (rsp["info"] as JsonObject).get("userid").toString()


                            // call next activity
                            NectarApplication.userID = userid.toString()

                            var loggedInUserView = LoggedInUserView("$name")
                            updateUiWithUser(loggedInUserView)
                            //call method for remember me
                            rememberme()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                resources.getString(R.string.Invalid_credentials),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Log.d("test", "${t.message}")
                        Log.d("test", "${t.printStackTrace()}")
                        loading.visibility = View.GONE
                        Toast.makeText(
                            applicationContext,
                            resources.getString(R.string.login_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }

        }
    }

    private fun registerfcmID() {
        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // checking for type intent filter
                if (intent.action == Config.REGISTRATION_COMPLETE) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
                    displayFirebaseRegId()

                } else if (intent.action == Config.PUSH_NOTIFICATION) {
                    // new push notification is received

                    val message = intent.getStringExtra("message")

                }
            }
        }

        displayFirebaseRegId()
    }

    override fun onResume() {
        super.onResume()
        // register GCM registration complete receiver
        mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it,
                IntentFilter(Config.REGISTRATION_COMPLETE)
            )
        };

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(
                it,
                IntentFilter(Config.PUSH_NOTIFICATION)
            )
        };

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    override fun onPause() {
        mRegistrationBroadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        };
        super.onPause()
    }

    private fun displayFirebaseRegId() {
        val pref = applicationContext.getSharedPreferences(Config.SHARED_PREF, 0)
        val regId = pref.getString("regId", null)
        PrefUtils.storeKey(this@LoginActivity, AppConstants.TokenID, regId)
    }

    private fun setvalueforrememberme() {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val sharedNameValue = sharedPreferences.getString(AppConstants.Username, null)
        val sharedPasswordValue = sharedPreferences.getString(AppConstants.Password, null)

        if (sharedNameValue != null && sharedPasswordValue != null) {
            username.setText(sharedNameValue).toString()
            password.setText(sharedPasswordValue).toString()
            checkbox.isChecked = true
        } else {
            checkbox.isChecked = false
        }
    }

    private fun rememberme() {
        val sharedPreferences: SharedPreferences =
            this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        if (checkbox.isChecked) {
            editor.putString(AppConstants.Username, username.text.toString())
            editor.putString(AppConstants.Password, password.text.toString())
        } else {
            editor.putString(AppConstants.Username, null)
            editor.putString(AppConstants.Password, null)
        }

        editor.apply()
        editor.commit()
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("ID", userid);
        startActivity(intent)
        finish();
        // startActivity(DashboardActivity.startIntent(this as LoginActivity))
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var change = ""
        language = spinner?.selectedItem.toString()

        if (language.equals("Portuguese")) {
            val config = resources.configuration
            val locale = Locale("pt")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            username.setHint(resources.getText(R.string.prompt_username))
            password.setHint(resources.getText(R.string.prompt_password))
            remember_me.setText(R.string.remember_me)
            welcometext.setText(R.string.welcome)
            login.setText(R.string.login)
        } else {
            val config = resources.configuration
            val locale = Locale("en")
            Locale.setDefault(locale)
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
            username.setHint(resources.getText(R.string.prompt_username))
            password.setHint(resources.getText(R.string.prompt_password))
            remember_me.setText(R.string.remember_me)
            welcometext.setText(R.string.welcome)
            login.setText(R.string.login)
        }
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

        langTranslater =
            FirebaseNaturalLanguage.getInstance().getTranslator(options)

        //download language models if needed
        val conditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        langTranslater.downloadModelIfNeeded()
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

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}