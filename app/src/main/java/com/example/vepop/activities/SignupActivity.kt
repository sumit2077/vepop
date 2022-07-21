package com.example.vepop.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.vepop.R
import com.example.vepop.firestore.FirestoreClass
import com.example.vepop.models.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        tvCanLogin.setOnClickListener {
            onBackPressed()

        }

        mbSignup.setOnClickListener {
            registerUser()
        }
    }

    private fun validateSignupDetails(): Boolean {
        return when {
            TextUtils.isEmpty(etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false


            }
            TextUtils.isEmpty(etEnterEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false

            }
            TextUtils.isEmpty(etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }
            etEnterPassword.text.toString().trim { it <= ' ' } != etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_passwords_dont_match),
                    true
                )
                false

            }
            !checkBox.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_conditions),
                    true
                )
                false

            }
            else -> {
//                showErrorSnackBar(resources.getString(R.string.msg_success), false)
                true

            }
        }
    }

    private fun registerUser() {
        //check with validate function if the entries are valid or not
        if (validateSignupDetails()) {

            showProgressDialog("Please Wait")

            val email: String = etEnterEmail.text.toString().trim { it <= ' ' }
            val password: String = etEnterPassword.text.toString().trim { it <= ' ' }

            //create an instance of FirebaseAuth class and create register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        //if the registration is successfully done
                        if (task.isSuccessful) {

                            //Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = Users(
                                firebaseUser.uid,
                                etFirstName.text.toString().trim { it <= ' ' },
                                etLastName.text.toString().trim { it <= ' ' },
                                etEnterEmail.text.toString().trim { it <= ' ' },

                                )

                            FirestoreClass().registerUser(this@SignupActivity, user)

//                            FirebaseAuth.getInstance().signOut()
//                            finish()
                        }
                        //if the registration is not successful
                        else {

                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()
    }
}

