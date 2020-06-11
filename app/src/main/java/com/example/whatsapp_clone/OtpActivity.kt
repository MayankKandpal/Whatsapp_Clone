package com.example.whatsapp_clone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import kotlinx.android.synthetic.main.activity_otp.*

const val PHONE_NUMBER = "phoneNumber"
class OtpActivity : AppCompatActivity() {
    var phonenumber:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        initviews()
    }

    private fun initviews() {
        phonenumber = intent.getStringExtra(PHONE_NUMBER)
        verifyTv.text = getString(R.string.verify_number,phonenumber)
        setSpannableString()
    }

    private fun setSpannableString() {
        val span = SpannableString(getString(R.string.waiting_text,phonenumber))
        val clickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                //send to previous activity
            }
            override fun updateDrawState(ds: TextPaint) {
                ds.color = ds.linkColor // you can use custom color
                ds.isUnderlineText = false // this remove the underline
            }
        }
        span.setSpan(clickableSpan,span.length-13,span.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        waitingTv.movementMethod = LinkMovementMethod.getInstance()
        waitingTv.text=span
    }
}