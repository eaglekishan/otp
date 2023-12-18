package com.example.otp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var send:Button
    private lateinit var number:EditText
    private lateinit var verify:Button
    private lateinit var otp:EditText
    var verificationId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        send=findViewById(R.id.send)
        number=findViewById(R.id.number)
        verify=findViewById(R.id.verify)
        otp=findViewById(R.id.enter)

        send.setOnClickListener {
            val phoneNumber="+91${number.text}"
            sendVerification(phoneNumber)


        }

        verify.setOnClickListener {
            val code=otp.text.toString()
            verifyCode(code)
        }

    }

    private fun verifyCode(code: String) {
        val credential=PhoneAuthProvider.getCredential(verificationId,code)

        signIn(credential)

    }

    private fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task->
                if(task.isSuccessful){
                    Toast.makeText(this,"sussessful",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendVerification(number:String){
        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number)
            .setTimeout(30L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(verificationCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val verificationCallback:OnVerificationStateChangedCallbacks=
        object :OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(p0: FirebaseException) {

            }

            override fun onCodeSent(s: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, p1)
                verificationId=s
            }

        }
}