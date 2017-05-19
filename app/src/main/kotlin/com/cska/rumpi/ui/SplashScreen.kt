package com.cska.rumpi.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.cska.rumpi.R
import com.cska.rumpi.network.RestApi
import com.cska.rumpi.network.datahelpers.DictionaryManager
import com.cska.rumpi.ui.base.BaseActivity
import com.cska.rumpi.utils.Consts
import com.cska.rumpi.utils.isNullOrEmpty
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


/**
 * Created by rumpi on 26.01.2017.
 */

class SplashActivity : BaseActivity() {
    private var retry: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        getDictionary()
    }

    fun checkLanguage() {
        val sharedPref = applicationContext.getSharedPreferences(Consts.PREF_NAME, 0);
        val language = sharedPref.getString(Consts.PREF_LANGUAGE, "none")
        if (language == "none") {
            LanguageActivity.launch(this)
        }
        else {
            Consts.setLanguage(baseContext, language)
            HomeActivity.launch(this)
        }
        finish()
    }

    fun getDictionary() {
        RestApi().getDictionary()
                .doOnNext { result ->
                    getPdf()
                }.onErrorReturn { error ->
            if (retry < 3) {
                retry++
                getDictionary()
            }
            else {
                error.printStackTrace()
                if (DictionaryManager.getEvents("ru").isNullOrEmpty()) {
                    Toast.makeText(baseContext, R.string.label_connection_error, Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    checkLanguage()
                }
            }
        }.subscribe()
    }

    fun getPdf() {
        RestApi().saveMedals().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    writeResponseBodyToDisk(response.body())
                    checkLanguage()
                }else{
                    checkLanguage()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                checkLanguage()
            }
        })
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        try {
            // todo change the file location/name according to your needs
            val pdfFile = File(filesDir, "medals.pdf")

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                val fileReader = ByteArray(4096)

                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0

                inputStream = body.byteStream()
                outputStream = openFileOutput(pdfFile.name, Context.MODE_PRIVATE);

                while (true) {
                    val read = inputStream!!.read(fileReader)

                    if (read == -1) {
                        break
                    }

                    outputStream!!.write(fileReader, 0, read)

                    fileSizeDownloaded += read.toLong()
                }

                outputStream!!.flush()

                return true
            }
            catch (e: IOException) {
                return false
            }
            finally {
                if (inputStream != null) {
                    inputStream!!.close()
                }

                if (outputStream != null) {
                    outputStream!!.close()
                }
            }
        }
        catch (e: IOException) {
            return false
        }

    }
}
