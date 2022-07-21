package com.example.vepop.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.vepop.R
import com.example.vepop.firestore.FirestoreClass
import com.example.vepop.models.Users
import com.example.vepop.utils.Constants
import com.example.vepop.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException
import java.util.jar.Manifest

class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: Users
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            //get the user details from the intent as a ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        etProfileFirstName.isEnabled = false
        etProfileFirstName.setText(mUserDetails.firstName)

        etProfileLastName.isEnabled = false
        etProfileLastName.setText(mUserDetails.lastName)

        etProfileEmail.isEnabled = false
        etProfileEmail.setText(mUserDetails.email)

        CardView.setOnClickListener(this)

        mbSave.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.CardView -> {
                    //Here we will check if the permission for photos access is allowed or not
                    //First we wil check the READ_EXTERNAL_STORAGE is allowed or not
                    if (ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
//                        showErrorSnackBar("You already have the storage permission", false)
                        Constants.showImageChooser(this@UserProfileActivity)
                    } else {
                        /*Request Permissions to be granted to this application, these permissions should be requested in your manifest,
                        they should not be granted to your app, and they should have protection level*/
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.mbSave -> {

                    if (validateUserProfileDetails()) {

                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null)
                            FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileUri)
                        else {
                            updateUserProfileDetails()

                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()

        val mobileNumber = etProfileMobileNo.text.toString().trim { it <= ' ' }

        val gender = if (rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        //key: gender  value:male (example)
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.PROFILE_COMPLETED] = 1

//        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this, userHashMap)
//      showErrorSnackBar("Your details are valid, you can update them" , false)

    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()

        Toast.makeText(this, "You have updated your profile successfully", Toast.LENGTH_SHORT)
            .show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            // If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showErrorSnackBar("The storage permission is granted" , false)
                Constants.showImageChooser(this)
            } else {
                // Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }


        }
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onActivityResult(requestCode, resultCode, data)")
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage .
                        mSelectedImageFileUri = data.data!!
//                        UserAvatar.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, UserAvatar)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //A log is printed when user cancels or closes the image selection
            Log.e("Request Cancelled", "Image Selection Cancelled")

        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(etProfileMobileNo.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar("Please Enter Mobile Number", false)
                false
            }
            else -> {
                true
            }
        }

    }

    fun imageUploadSuccess(imageURL: String) {
//        hideProgressDialog()

        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}

