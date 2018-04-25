package com.fish.isyania.aplikasipemesananreparasi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.fish.isyania.aplikasipemesananreparasi.R.id.chkSave
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    private lateinit var sharedPreferences : SharedPreferences
    lateinit var mGoogleSignInClient: GoogleSignInClient

    //    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    val RC_SIGN_IN: Int = 999
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    if(mAuth.currentUser?.email != null){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    //

    //
    //untuk save username dan password
    //--------------------------------------------------------------------------
    sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
    emailTxt.setText(sharedPreferences.getString("email", ""))
    passwordTxt.setText(sharedPreferences.getString("password", ""))
    chkSave.isChecked = sharedPreferences.getBoolean("check", false)

    //--------------------------------------------------------------------------

    //untuk  request idtoken dan email dari Gmail
    //------------------------------------------------------------------------
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    //------------------------------------------------------------------------


//
//        untuk login google (button)
    loginGoogle.setOnClickListener {
        signInGoogle()
    }

    //untuk Login Email (button)
    loginBtn.setOnClickListener{
        login()
    }

    //untuk Register (button)
    regTxt.setOnClickListener{
        register()
    }

}



//fungsi untuk masuk google
private fun signInGoogle(){
    val signInIntent: Intent = mGoogleSignInClient.signInIntent
    startActivityForResult(signInIntent, RC_SIGN_IN)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == RC_SIGN_IN) {
        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (result.isSuccess){

            val account = result.signInAccount
            firebaseAuthWithGoogle(account!!)

        }else {

        }
    }
}




private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
    mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(LoginActivity@this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()


                }


            }
}
private fun login () {
    val emailTxt = findViewById<View>(R.id.emailTxt) as EditText
    var email = emailTxt.text.toString()
    val passwordTxt = findViewById<View>(R.id.passwordTxt) as EditText
    var password = passwordTxt.text.toString()

    if (!email.isEmpty() && !password.isEmpty()) {
        this.mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener ( this, { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
                if(chkSave.isChecked){
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("email",email)
                    editor.putString("password", password)
                    editor.putBoolean("check", true)
                    editor.commit()
                }else{
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.remove("email")
                    editor.remove("password")
                    editor.remove("check")
                    editor.commit()
                }

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Error Logging in :(", Toast.LENGTH_SHORT).show()
            }
        })

    }else {
        Toast.makeText(this, "Tolong isi email dan password yang ada dengan bener", Toast.LENGTH_SHORT).show()
    }
}

private fun register () {
    startActivity(Intent(this, RegisterActivity::class.java))
}


}
