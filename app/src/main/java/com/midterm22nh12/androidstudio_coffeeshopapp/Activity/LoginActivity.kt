package com.midterm22nh12.androidstudio_coffeeshopapp.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.developer.gbuttons.GoogleSignInButton
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

import com.midterm22nh12.androidstudio_coffeeshopapp.R

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginButton: Button
    private lateinit var signupRedirectText: TextView
    private lateinit var forgotPassword: TextView
    private lateinit var googleBtn: GoogleSignInButton
    private lateinit var auth: FirebaseAuth
    private lateinit var gOptions: GoogleSignInOptions
    private lateinit var gClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signUpRedirectText)
        forgotPassword = findViewById(R.id.forgot_password)
        googleBtn = findViewById(R.id.googleBtn)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val pass = loginPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login Fail", Toast.LENGTH_SHORT).show()
                    }
                }
        }


        signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }

        gOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gClient = GoogleSignIn.getClient(this, gOptions)

        val gAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (gAccount != null) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        val activityResultLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val task: Task<GoogleSignInAccount> =
                        GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        task.getResult(ApiException::class.java)
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    } catch (e: ApiException) {
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        googleBtn.setOnClickListener {
            val signInIntent: Intent = gClient.signInIntent
            activityResultLauncher.launch(signInIntent)
        }
    }
}
