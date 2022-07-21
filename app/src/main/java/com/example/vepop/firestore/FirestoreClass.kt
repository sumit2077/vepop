package com.example.vepop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.vepop.activities.LoginActivity
import com.example.vepop.activities.SignupActivity
import com.example.vepop.activities.UserProfileActivity
import com.example.vepop.models.Users
import com.example.vepop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignupActivity, userInfo: Users) {

        //The "users" is a collection name. If the collection is already created then it will not create the same
        mFireStore.collection(Constants.USERS)
            //Document ID for the users fields, here Document ID is the uid
            .document(userInfo.id)
            //Here setOption is set to merge so that we can merge later instead of replacing it
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error Signing up the user", e)

            }


    }

    private fun getCurrentUserID(): String {

        //An instance of current User using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assign currentUserID if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID

    }

    fun getUserDetails(activity: Activity) {

        //Here we pass the collection name from which we want data
        mFireStore.collection(Constants.USERS)
            //The document id to get the Fields of user
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                //Here we received the document snapshot which is converted into the User Data model object
                val user = document.toObject(Users::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(Constants.VEPOP_PREFERENCES, Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //Key: LOGGED_IN_USERNAME - Value: first name , last name
                editor.putString(Constants.LOGGED_IN_USERNAME, "${user.firstName} ${user.lastName}")
                editor.apply()

                //TODO Step 6 : pass the result to the login activity
                //START
                when (activity) {
                    is LoginActivity -> {
                        //Call a function of base activity for transforming the result to it
                        activity.userLoggedInSuccess(user)
                    }
                }
                //END


            }

    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity) {
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity, imageFileURI

            )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            // The image upload is success
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            // Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when(activity){
                        is UserProfileActivity ->{
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }


                }

        }
            .addOnFailureListener { exception ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )


            }

    }
}

