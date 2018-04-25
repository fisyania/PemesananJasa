package com.fish.isyania.aplikasipemesananreparasi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        class RegisterActivity : AppCompatActivity() {
            val mAuth = FirebaseAuth.getInstance()
            lateinit var mDatabase : DatabaseReference
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_register)

                mDatabase = FirebaseDatabase.getInstance().getReference("Users")


                regBtn.setOnClickListener(View.OnClickListener {
                    registerUser()
                })

            }
            private fun registerUser () {


                var email = regemailTxt.text.toString()
                var password = regpasswordTxt.text.toString()
                var name = nameTxt.text.toString()

                if (!email.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = mAuth.currentUser
                            val uid = user!!.uid
                            mDatabase.child(uid).child("Name").setValue(name)
                            startActivity(Intent(this, MainActivity::class.java))
                            Toast.makeText(this, "Successfully registered :)", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(this, "Error registering, try again later :(", Toast.LENGTH_LONG).show()
                        }
                    })
                }else {
                    Toast.makeText(this,"Please fill up the Credentials :|", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}
