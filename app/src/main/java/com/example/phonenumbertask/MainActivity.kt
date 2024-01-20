package com.example.phonenumbertask

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private lateinit var phoneNumberTextView: TextView
    private lateinit var operatorTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        operatorTextView = findViewById(R.id.operatorTextView)


        // Check for the READ_PHONE_STATE permission
        if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, get the phone number
            val phoneNumber = getPhoneNumber()
            val operatorName = getOperatorName()

            displayPhoneNumber(phoneNumber)
            displayOperator(operatorName)

        } else {
            // Request the READ_PHONE_STATE permission
            requestPermissions(arrayOf(android.Manifest.permission.READ_PHONE_STATE), 1)
        }
    }
    private fun displayOperator(operatorName: String) {
        operatorTextView.text = "Operator: $operatorName"
    }
    private fun getOperatorName(): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // Get the operator name
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where permission is not granted
            "Permissions not granted"
        } else {
            // Return the operator name
            telephonyManager.networkOperatorName ?: "Operator name not available"
        }
    }

    private fun getPhoneNumber(): String {
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        // Get the phone number
        return if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Handle the case where permissions are not granted
            "Permissions not granted"
        } else {
            // Return the phone number
            telephonyManager.line1Number ?: "Phone number not available"
        }
    }

    private fun displayPhoneNumber(phoneNumber: String) {
        phoneNumberTextView.text = "Phone Number: $phoneNumber"
    }


    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, get the phone number
                    val phoneNumber = getPhoneNumber()
                    val operatorName = getOperatorName()

                    displayPhoneNumber(phoneNumber)
                    displayOperator(operatorName)

                } else {
                    // Permission denied, handle accordingly
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
