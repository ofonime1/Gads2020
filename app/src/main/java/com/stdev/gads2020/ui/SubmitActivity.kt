package com.stdev.gads2020.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import cn.pedant.SweetAlert.SweetAlertDialog
import com.stdev.gads2020.GadsApi
import com.stdev.gads2020.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        val toolbar = findViewById<Toolbar>(R.id.submit_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val actionBar : ActionBar? = supportActionBar
        actionBar?.setDisplayShowCustomEnabled(true)
        val inflater : LayoutInflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val actionBarView = inflater.inflate(R.layout.custom_submit_toolbar,null)
        actionBar?.customView = actionBarView

        val firstName = findViewById<EditText>(R.id.submit_first_name)
        val lastName = findViewById<EditText>(R.id.submit_last_name)
        val emailAddress = findViewById<EditText>(R.id.submit_email_address)
        val githubProjectLink = findViewById<EditText>(R.id.submit_github_link)


        val buttonSubmit = findViewById<Button>(R.id.button_submit)
        buttonSubmit.setOnClickListener {
            if (firstName.text.isNotEmpty() || lastName.text.isNotEmpty() || emailAddress.text.isNotEmpty() || githubProjectLink.text.isNotEmpty()) {

                val sweetAlertDialog = SweetAlertDialog(this@SubmitActivity, SweetAlertDialog.WARNING_TYPE)
                sweetAlertDialog.titleText = "Are you sure ?"
                sweetAlertDialog.confirmText = "Yes"
                sweetAlertDialog.setConfirmClickListener {
                    it.cancel()
                    val name = firstName.text.toString()
                    val lastname = lastName.text.toString()
                    val emailaddress = emailAddress.text.toString()
                    val githublink = githubProjectLink.text.toString()
                    val submitService =
                        GadsApi.retrofitSubmitService
                    val detailsCall = submitService.submitProject(emailaddress, name, lastname, githublink)
                    detailsCall?.enqueue(object : Callback<Void?> {
                        override fun onFailure(call: Call<Void?>, t: Throwable) {

                            val sweetAlertDialog1 = SweetAlertDialog(this@SubmitActivity,SweetAlertDialog.ERROR_TYPE)
                            sweetAlertDialog1.titleText = "Oops..."
                            sweetAlertDialog1.contentText = "Submission not Successful : ${t.message.toString()}"
                            sweetAlertDialog1.show()
                        }

                        override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                            if (response.isSuccessful) {
                                Log.i("SubmitActivity", "onResponse: $response : $call")
                                val sweetAlertDialog2 = SweetAlertDialog(
                                    this@SubmitActivity,
                                    SweetAlertDialog.SUCCESS_TYPE
                                )
                                sweetAlertDialog2.titleText = "Good Job !"
                                sweetAlertDialog2.contentText = "Submission Successful"
                                sweetAlertDialog2.show()

                            } else {
                                val sweetAlertDialog3 = SweetAlertDialog(
                                    this@SubmitActivity,
                                    SweetAlertDialog.ERROR_TYPE
                                )
                                sweetAlertDialog3.titleText = "Oops...."
                                sweetAlertDialog3.contentText =
                                    "Submission not Successful, Try again "
                                sweetAlertDialog3.show()
                            }
                        }

                    })
                }
                sweetAlertDialog.show()
            }

            else{
                val sweetAlert = SweetAlertDialog(this@SubmitActivity)
                sweetAlert.titleText = "Input All Fields ! "
                sweetAlert.show()
            }
        }

    }

    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}