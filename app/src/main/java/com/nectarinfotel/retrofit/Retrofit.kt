package com.nectarinfotel.retrofit

import android.content.Context
import android.util.Log
import com.nectarinfotel.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

class Retrofit {

    companion object {
        @kotlin.jvm.JvmField
        var BaseUrl: String = ""

        @JvmStatic
        val TAG = com.nectarinfotel.retrofit.Retrofit::class.java.simpleName
        private val DEFAULT_TIMEOUT_SEC = 60
        var client: OkHttpClient? = null

        // public var BaseUrl: String? = null
        fun getClient(context: Context): Retrofit? {
            val clientBuilder = getOkHttpBuilder()
            val client = clientBuilder.build()

                Log.d("Base_URL", AppConstants.BASE_URL)
                return AppConstants.BASE_URL?.let { buildRetrofitInstance(it, client) }
        }

        fun getClient1(context: Context): Retrofit? {
            val clientBuilder = getOkHttpBuilder()
            val client = clientBuilder.build()

            Log.d("Base_URL1", AppConstants.BASE_URL1)
            return AppConstants.BASE_URL1?.let { buildRetrofitInstance(it, client) }
        }

        private fun buildRetrofitInstance(baseUrl: String, client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getUnsafeOkHttpClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }


        private fun getOkHttpBuilder(): OkHttpClient.Builder {
            return OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
        }

        fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<java.security.cert.X509Certificate>,
                        authType: String
                    ) {
                    }
                })

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.getSocketFactory()

                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier(object : HostnameVerifier {
                    override fun verify(hostname: String, session: SSLSession): Boolean {
                        return true
                    }
                })
                return builder
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }


    }


}