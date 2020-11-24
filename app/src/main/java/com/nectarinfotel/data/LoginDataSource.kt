package com.nectarinfotel.data

import com.google.gson.JsonObject
import com.nectarinfotel.utils.NectarApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private var rsp: Result.Success<Call<JsonObject>>? = null

    fun login(username: String, password: String) {
        try {
            // TODO: handle loggedInUser authentication
            var call  = NectarApplication.mRetroClient!!.callLoginAPI(username,password,"aaaaa")

            call.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    var rsp: JsonObject? = response.body() ?: return

                    if(rsp!!.isJsonObject && rsp["info"].isJsonObject){
                        var infoObj = rsp["info"]

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

            })
          /*  val fakeUser = LoggedInUser(java.util.UUID.randomUUID().toString(), "Jane Doe")
            return Result.Success(fakeUser)*/
        } catch (e: Throwable) {
           // return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}

