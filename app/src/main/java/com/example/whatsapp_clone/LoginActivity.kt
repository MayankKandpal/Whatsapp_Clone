package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var countrycode :String
    private lateinit var phoneno : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //HintRequest to be added *don't know it at time of this code
        editTextPhone.addTextChangedListener{
            nextBtn.isEnabled=!(it.isNullOrEmpty() || it.length<10)
        }
        nextBtn.setOnClickListener{
            checknumber()
        }
    }

    private fun checknumber() {
        countrycode = ccp.selectedCountryCodeWithPlus
        phoneno = countrycode+editTextPhone.text.toString()
        notifyuser()
    }

    private fun notifyuser() {
        MaterialAlertDialogBuilder(this).apply {
            setMessage("We are going to verify the phone no:$phoneno\n"+"Is this ok or would you like to change the number")
           setPositiveButton("ok") { _, _ ->
           showOtpActivity()
           }
           setNegativeButton("Edit"){dialog, which ->
               dialog.dismiss()
           }
            setCancelable(false)
            create()
            show()

        }
 }

    private fun showOtpActivity() {
        startActivity(Intent(this,OtpActivity::class.java).putExtra(PHONE_NUMBER,phoneno))
        finish()
    }
}