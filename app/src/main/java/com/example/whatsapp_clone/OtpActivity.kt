package com.example.whatsapp_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_otp.*
import java.util.concurrent.TimeUnit

const val PHONE_NUMBER = "phoneNumber"
class OtpActivity : AppCompatActivity() {
    var phonenumber:String?=null
    var mverificationid :String?=null
    var mresendcode: PhoneAuthProvider.ForceResendingToken?=null
    lateinit var callbacks:PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        initviews()
        startVerify()
        showTimer(60000)
    }

    private fun startVerify() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenumber!!, // Phone number to verify
            60, // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this, // Activity (for callback binding)
            callbacks) // OnVerificationStateChangedCallbacks
    }

    private fun showTimer(milliSecInFuture: Long) {
        resendBtn.isEnabled=false
        object :CountDownTimer(milliSecInFuture,1000){
            override fun onFinish() {
                resendBtn.isEnabled=true
                counterTv.isVisible=false
            }
            override fun onTick(millisUntilFinished: Long) {
                counterTv.isVisible=true
                counterTv.text=getString(R.string.second_remaining,millisUntilFinished/1000)
            }
        }.start()
    }

    private fun initviews() {
        phonenumber = intent.getStringExtra(PHONE_NUMBER)
        verifyTv.text = getString(R.string.verify_number,phonenumber)
        setSpannableString()
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
               val smscode = credential.smsCode
                if(!smscode.isNullOrBlank())
                    sentcodeEt.setText(smscode)
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {

                } else if (e is FirebaseTooManyRequestsException) {

                }


            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                mverificationid = verificationId
              mresendcode = token
            }
        }
    }

    private fun setSpannableString() {
        val span = SpannableString(getString(R.string.waiting_text,phonenumber))
        val clickableSpan = object : ClickableSpan(){
            override fun onClick(widget: View) {
                //send to previous activity
                showLoginActivity()
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

    private fun showLoginActivity() {
       startActivity(Intent(this,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
       //flag helps us in removing/clearing the back stack
    }

    override fun onBackPressed() {
        //removing the super function helps us in disabling the action of pressing of back button
    }
}