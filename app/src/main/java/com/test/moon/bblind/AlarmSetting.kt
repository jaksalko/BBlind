package com.test.moon.bblind

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Window
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AlarmSetting : AppCompatActivity()
{
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef : DatabaseReference = database.getReference("Account")
    var tb1: ToggleButton? = null
    var tb2: ToggleButton? = null
    var firebaseanalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    var sf: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    var Matching: String? = null
    var App: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.alram_setting)


        sf = getSharedPreferences("Alarm", 0)
        editor =  sf!!.edit();

        Matching = sf!!.getString("Matching", "")
        App = sf!!.getString("App", "")

        tb1 = findViewById(R.id.toggleButton2)//Matching
        tb2 = findViewById(R.id.toggleButton1)//APp


        if (Matching.equals("true")) {
            tb1!!.setChecked(true)
        } else {
            tb1!!.setChecked(false)
        }

        if (App.equals("true")) {
            tb2!!.setChecked(true)
        } else {
            tb2!!.setChecked(false)
        }





        tb1!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked == true) {

                    editor!!.putString("Matching", "true")

                   myRef.child(MainActivity.Myuid!!).child("Alram").setValue("true")

                    firebaseanalytics!!.setUserProperty("MatchingAlarm", "true")

                } else {

                    editor!!.putString("Matching", "false")

                    myRef.child(MainActivity.Myuid!!).child("Alram").setValue("false")

                    firebaseanalytics!!.setUserProperty("MatchingAlarm", "false")

                }

                editor!!.commit()


            }
        });

        tb2!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked == true) {

                    editor!!.putString("App", "true")

                    firebaseanalytics!!.setUserProperty("AppAlarm", "true")

                } else {

                    editor!!.putString("App", "false")

                    firebaseanalytics!!.setUserProperty("AppAlarm", "false")

                }


                editor!!.commit()
            }
        });



        fun onBackPressed() {
            super.onBackPressed();
            finish();

        }
    }
}