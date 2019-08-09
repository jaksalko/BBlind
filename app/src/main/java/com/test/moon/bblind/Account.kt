package com.test.moon.bblind

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.telephony.TelephonyManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_account.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.spinner_item.*
import kotlinx.android.synthetic.main.spinner_dropdown_item.*
import kotlinx.android.synthetic.main.activity_main.*
import android.telephony.PhoneNumberUtils
import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.SharedPreferences
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.util.TypedValue
import android.view.Window
import android.widget.ToggleButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId


class Account : AppCompatActivity()
{
    var firebaseanalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    var sf : SharedPreferences?  = null
    var editor : SharedPreferences.Editor?  = null

    var Matching : String? = null
    var App : String? = null






    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        MainActivity.Accountactivity = this

        sf = getSharedPreferences("Alarm", 0)
        editor = sf!!.edit();
        editor!!.putString("Matching","true")
        editor!!.putString("App","true")
        editor!!.commit()


        val id : Array<String> = arrayOf("id1","id2","id3")
        // val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database.getReference("Account")
        val keyboard : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var spinadapter = ArrayAdapter.createFromResource(this,R.array.year,R.layout.spinner_item)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            var permissionResult = checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)

            if(permissionResult == PackageManager.PERMISSION_DENIED)
            {


                if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_STATE))
                {
                    var dialog =  AlertDialog.Builder(this);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", DialogInterface.OnClickListener()
                            { dialogInterface: DialogInterface, i: Int ->
                                fun onClick(dialog:DialogInterface, which:Integer)
                            {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                                { // CALL_PHONE 권한을 Android OS에 요청한다.

                                    requestPermissions(arrayOf(android.Manifest.permission.READ_PHONE_STATE), 1000)

                                }
                            }
                            })
                            .setNegativeButton("아니오",DialogInterface.OnClickListener()
                            {
                                dialogInterface: DialogInterface, i: Int ->

                                fun onClick(dialog:DialogInterface , which:Integer) {
                                    Toast.makeText(this, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }


                            }).create().show()
                }
                else
                {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_PHONE_STATE), 1000)

                }






            }
            else
            {
                Toast.makeText(this,"됐냐",Toast.LENGTH_SHORT).show()
            }

        }




        spinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        Account_Spinner_Year.adapter = spinadapter

        Account_Toggle_Boy.setChecked(true)
        Account_Toggle_Girl.setChecked(false)
        Account_Radio_Student.setChecked(true)

        Account_Toggle_Boy.setText("남자")
        Account_Toggle_Boy.setTextOff("남자")
        Account_Toggle_Boy.setTextOn("남자")
        Account_Toggle_Girl.setText("여자")
        Account_Toggle_Girl.setTextOff("여자")
        Account_Toggle_Girl.setTextOn("여자")


        Account_Toggle_Boy.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
        Account_Toggle_Girl.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
        Account_Toggle_Boy.setOnClickListener(View.OnClickListener {

            if(Account_Toggle_Boy.isChecked())
            {
                Account_Toggle_Girl.setChecked(false)
                Account_Toggle_Boy.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
                Account_Toggle_Girl.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
            }
            else
            {
                Account_Toggle_Girl.setChecked(true)
                Account_Toggle_Boy.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
                Account_Toggle_Girl.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
            }


        })
        Account_Toggle_Girl.setOnClickListener(View.OnClickListener{

            if(Account_Toggle_Girl.isChecked())
            {
                Account_Toggle_Boy.setChecked(false)
                Account_Toggle_Boy.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
                Account_Toggle_Girl.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
            }
            else
            {
                Account_Toggle_Boy.setChecked(true)
                Account_Toggle_Boy.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
                Account_Toggle_Girl.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
            }
        })

        var i = 0
        var sex : String?=null
        var isStudent : Boolean = true
        Account_Button_Register.setOnClickListener(View.OnClickListener {

            if((Account_Edit_Nickname.text.toString().length)>8||(Account_Edit_Nickname.text.toString().length)<4)
            {
                //팝업 띄우기

                Toast.makeText(this,"닉네임은 4~8글자 이내",Toast.LENGTH_SHORT).show()

                keyboard.showSoftInput(Account_Edit_Nickname,0)
            }
            else
            {


                if (Account_Toggle_Boy.isChecked())
                    sex = "Man"
                else
                    sex = "Woman"

                val crd  = ChatRoomData()

                MainActivity.checkapplylist!!.checklist!!.add("초기화")




                if(Account_Edit_Inviter.text.toString().length==0)
                {
                    myRef.child(currentUser!!.uid).child("Nickname").setValue(Account_Edit_Nickname.text.toString())
                    myRef.child(currentUser!!.uid).child("Year").setValue(Account_Spinner_Year.selectedItem.toString())
                    myRef.child(currentUser!!.uid).child("Sex").setValue(sex)
                    myRef.child(currentUser!!.uid).child("Phone").setValue(getPhoneNumber())
                    myRef.child(currentUser!!.uid).child("ChatNum").setValue(crd)
                    myRef.child(currentUser!!.uid).child("fcmToken").setValue(FirebaseInstanceId.getInstance().token)
                    myRef.child(currentUser!!.uid).child("Myapply").setValue(MainActivity.checkapplylist)
                    myRef.child(currentUser!!.uid).child("heart").setValue(20)
                    if (Account_Radio_Student.isChecked())
                        myRef.child(currentUser!!.uid).child("isStudent").setValue("Y")
                    else
                        myRef.child(currentUser!!.uid).child("isStudent").setValue("N")


                    firebaseanalytics!!.setUserProperty("MatchingAlarm", "true")
                    firebaseanalytics!!.setUserProperty("AppAlarm", "true")


                    i++
                    MainActivity.Myuid = currentUser!!.uid
                    DirectLobby()

                }
                else
                {
                    myRef.addListenerForSingleValueEvent(object : ValueEventListener
                    {
                        override fun onCancelled(p0: DatabaseError)
                        {



                        }

                        override fun onDataChange(p0: DataSnapshot)
                        {

                            if (p0.child(Account_Edit_Inviter.text.toString()).exists())
                            {


                                var Inviterheart: Int = Integer.parseInt(p0.child(Account_Edit_Inviter.text.toString()).child("heart").getValue(true).toString())

                                Inviterheart += 5
                                myRef.child(currentUser!!.uid).child("Nickname").setValue(Account_Edit_Nickname.text.toString())
                                myRef.child(currentUser!!.uid).child("Year").setValue(Account_Spinner_Year.selectedItem.toString())
                                myRef.child(currentUser!!.uid).child("Sex").setValue(sex)
                                myRef.child(currentUser!!.uid).child("Phone").setValue(getPhoneNumber())
                                myRef.child(currentUser!!.uid).child("ChatNum").setValue(crd)
                                myRef.child(currentUser!!.uid).child("fcmToken").setValue(FirebaseInstanceId.getInstance().token)
                                myRef.child(currentUser!!.uid).child("Myapply").setValue(MainActivity.checkapplylist)

                                myRef.child(currentUser!!.uid).child("heart").setValue(30)
                                myRef.child(Account_Edit_Inviter.text.toString()).child("heart").setValue(Inviterheart)
                                if (Account_Radio_Student.isChecked())
                                    myRef.child(currentUser!!.uid).child("isStudent").setValue("Y")
                                else
                                    myRef.child(currentUser!!.uid).child("isStudent").setValue("N")


                                firebaseanalytics!!.setUserProperty("MatchingAlarm", "true")
                                firebaseanalytics!!.setUserProperty("AppAlarm", "true")


                                i++
                                MainActivity.Myuid = currentUser!!.uid
                                DirectLobby()

                            }
                            else
                            {

                                Toast.makeText(this@Account,"존재하지 않는 추천인코드입니다.",Toast.LENGTH_LONG).show()

                            }


                        }

                    })

                }



            }
        })

    }
    private fun DirectLobby()
    {
        var intent = Intent(this, LobbyActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        startActivity(intent)
    }
    @SuppressLint("MissingPermission")
    fun getPhoneNumber(): String {
        val telephony = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var phoneNumber = ""

        try {
            if (telephony.line1Number != null) {
                phoneNumber = telephony.line1Number
            } else {
                if (telephony.simSerialNumber != null) {
                    phoneNumber = telephony.simSerialNumber
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (phoneNumber.startsWith("+82")) {
            phoneNumber = phoneNumber.replace("+82", "0") // +8210xxxxyyyy 로 시작되는 번호
        }
        phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber)

        return phoneNumber
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1000)
        {
            if(grantResults.count()>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

                if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
                {



                }

            }
            else
            {
                Toast.makeText(this,"거부됨",Toast.LENGTH_SHORT).show()
            }
        }
    }




}