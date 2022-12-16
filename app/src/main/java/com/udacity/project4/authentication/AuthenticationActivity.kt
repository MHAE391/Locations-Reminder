package com.udacity.project4.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import kotlinx.android.synthetic.main.activity_authentication.*

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    companion object {
        const val TAG = "AuthenticationActivity"
        const val SIGN_IN_RESULT_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        if(auth.currentUser != null) {
            Toast.makeText(this ,
                "Welcome Back ${auth.currentUser?.displayName}",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, RemindersActivity::class.java))
            finish()
        }
        else {
            launchSignInFlow()
        }
    }


    private fun launchSignInFlow() {
        val providers = listOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        login_button.setOnClickListener {
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setLogo(R.drawable.map).setAvailableProviders(
                    providers
                ).build(),SIGN_IN_RESULT_CODE
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this ,
                    "Welcome ${auth.currentUser?.displayName}",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RemindersActivity::class.java))
                finish()
                Log.i(TAG, "Successfully signed in user " +
                        "${auth.currentUser?.displayName}!"
                )
            } else {
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

}