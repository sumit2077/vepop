package com.example.vepop.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.vepop.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.progress_dialog.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }

    fun showErrorSnackBar(message: String , errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),message ,Snackbar.LENGTH_LONG)

        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.black)
            )
        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.orange)
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)

        //set the screen content from a layout file
        //the layout will be inflated, adding all top level views to the screen
        mProgressDialog.setContentView(R.layout.progress_dialog)
        mProgressDialog.tvPleaseWait.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //start the progress dialog and show it on screen
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
}