package com.test.moon.bblind

import android.app.Activity
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.usermgmt.UserManagement
import com.kakao.util.exception.KakaoException
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.database.*
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth?  = FirebaseAuth.getInstance()
    private var user : FirebaseUser?= mAuth!!.currentUser
    val database : FirebaseDatabase? = FirebaseDatabase.getInstance()
    val myRef : DatabaseReference = database!!.reference
    var auth : FirebaseAuth?=FirebaseAuth.getInstance()

    var mCallbackManager : CallbackManager?=null
    var myuid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        //어플의 모든 푸시알림 삭제
        val  notifiyMgr : NotificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notifiyMgr.cancelAll()




        if(auth!!.currentUser==null)
        {
            Log.d("crdcheck","1111"+auth!!.currentUser)
            myuid = null
            login_button.visibility=View.VISIBLE

        }
        else
        {

            DirectLobby(auth!!.currentUser?.uid!!)
            login_button.visibility=View.GONE

            myuid = auth!!.currentUser!!.uid
        }
        updateUI()


        mCallbackManager = CallbackManager.Factory.create()

        login_button!!.setReadPermissions("email")

        login_button!!.registerCallback(mCallbackManager,object : FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                //페이스북 로그인 성공

                handleFacebookAccessToken(result!!.accessToken)

                Toast.makeText(this@MainActivity,"로그인 성공",Toast.LENGTH_LONG).show()
            }
            override fun onCancel() {
                //페이스북 로그인 취소
                Toast.makeText(this@MainActivity,"로그인이 취소되었습니다.",Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                //페이스북 로그인 실패
                //Log.d("페북 로그인 에러",error.toString())
                Toast.makeText(this@MainActivity,"로그인에 실패하었습니다.",Toast.LENGTH_SHORT).show()
            }
        })




    }



    fun handleFacebookAccessToken(token : AccessToken)
    {
        //페이스북 로그인

            val credential = FacebookAuthProvider.getCredential(token.token)
            auth!!.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(this@MainActivity,"Authentication sucesseded.",Toast.LENGTH_LONG).show()
                            user = auth!!.currentUser
                            DirectSignUp()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this@MainActivity,"Authentication failed.",Toast.LENGTH_LONG).show()

                        }
                    }


    }
    /**
     * Update UI based on Firebase's current user. Show Login Button if not logged in.
     */
    private fun updateUI() {
        Log.d("crdcheck","updateUI"+myuid)
        if (!myuid.equals(null)) {


            /**
             * SHOW LOGO FOR 2SEC
             */


            DirectLobby(myuid!!)
            /*if (currentUser.photoUrl != null) {
                Glide.with(this)
                        .load(currentUser.photoUrl)
                        .into(imageView)
            }
            loginButton.visibility = View.INVISIBLE
            loggedInView.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE*/
        } else {
            /**
             * SHOW LOGO FOR 2SEC
             */


        }
    }

    /**
     * OnActivityResult() should be overridden for Kakao Login because Kakao Login uses startActivityForResult().
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)




    }

    private fun getFirebaseJwt(kakaoAccessToken: String): Task<String> {
        val source = TaskCompletionSource<String>()

        val queue = Volley.newRequestQueue(this)
        val url = resources.getString(R.string.validation_server_domain) + "/verifyToken"
        val validationObject = HashMap<String, String>()
        validationObject["token"] = kakaoAccessToken

        val request = object : JsonObjectRequest(Request.Method.POST, url, JSONObject(validationObject), Response.Listener { response ->
            try {


                val firebaseToken = response.getString("firebase_token")
                source.setResult(firebaseToken)
            } catch (e: Exception) {
                source.setException(e)
            }
        }, Response.ErrorListener { error ->
            source.setException(error)
        }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["token"] = kakaoAccessToken
                return params
            }
        }
        // request.setRetryPolicy(DefaultRetryPolicy( 20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(request)
        return source.task
    }
    private fun DirectLobby( str : String)
    {

        myRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                Mysex = p0.child("Account").child(str).child("Sex").getValue(true).toString()

                if(p0.child("Account").child(str).child("Myapply").exists()) {
                    MainActivity.checkapplylist = p0.child("Account").child(str).child("Myapply").getValue(CheckApplyListData::class.java)!!
                }
                else
                {
                    for(i in 1..MainActivity.checkapplylist!!.checklist!!.size)
                        MainActivity.checkapplylist!!.checklist!!.removeAt(0)
                }

                if(MainActivity.checkapplylist!!.checklist!!.size>1)
                {
                    for ( i in 1..MainActivity.checkapplylist!!.checklist!!.size-1)
                    //1부터인 이유 = 0번째는 "초기화"
                    {
                        //이미지난 매칭정보가 존재할 경우


                        var s1 = MainActivity.checkapplylist!!.checklist!![i].split("/")[0]
                        var s2 = MainActivity.checkapplylist!!.checklist!![i].split("/")[1]


                        val today = Date()
                        var strdate: String? = null

                        var format1: SimpleDateFormat? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            format1 = SimpleDateFormat("yyyy-MM-dd")

                            strdate = format1.format(today)

                        }


                        if(s2.compareTo(strdate!!)<0)
                        {


                            val delquery: Query = myRef.child("Apply").child("SubwayStation").child(s1)
                                    .child(s2).child(Mysex!!).orderByChild("name").equalTo(str)


                            delquery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    for (appleSnapshot in p0.getChildren()) {
                                        appleSnapshot.getRef().removeValue()

                                        MainActivity.checkapplylist!!.checklist!!.removeAt(i)
                                        myRef.child("Account").child(str).child("Myapply").setValue(MainActivity.checkapplylist)


                                    }

                                }
                            })
                        }





                    }
                }


                if(str==null)
                {

                }
                else if(!p0.child("Account").child(str).exists())
                {
                    DirectSignUp()
                }

                else if (p0.child("Account").child(str).exists() && p0.child("Account").child(str).child("ChatNum").exists())
                {

                    //아이디가 존재할 경우 && 매칭상대가 존재할 경우


                    Log.d("crdcheck","오잖아")
                    crd = p0.child("Account").child(str).child("ChatNum").getValue(ChatRoomData::class.java)!!
                    if(crd!!.ChatRoom.size>0) {
                        for (i in 0..crd!!.ChatRoom.size - 1) {

                            MainActivity.ChatRoomNum = crd!!.ChatRoom[i]
                            var strr = MainActivity.ChatRoomNum


                            val today = Date()
                            var strdate: String? = null

                            var format1: SimpleDateFormat? = null
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                format1 = SimpleDateFormat("yyyy-MM-dd")

                                strdate = format1.format(today)

                            }

                            if (p0.child("Chat").child(ChatRoomNum!!).exists()) {
                                //이미 지난 채팅방이 존재할 경우 삭제

                                if (p0.child("Chat").child(ChatRoomNum!!).child("Info").child("ChatRoomList").child("meetDate").getValue(true).toString().compareTo(strdate!!) < 0) {
                                    myRef.child("Chat").child(ChatRoomNum!!).removeValue()

                                    strr = strr!!.replace(str, "")
                                    Myuid = str




                                    crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!
                                    for (i in 0..crdd!!.Token.size - 1) {
                                        crdd!!.Token.remove(p0.child("Account").child(Myuid!!).child("fcmToken").getValue(true))
                                    }
                                    for (i in 0..crdd!!.ChatRoom.size - 1) {
                                        crdd!!.ChatRoom.remove(ChatRoomNum!!)
                                    }

                                    for (i in 0..crd!!.Token.size - 1) {
                                        crd!!.Token.remove(p0.child("Account").child(strr!!).child("fcmToken").getValue(true))
                                    }
                                    for (i in 0..crd!!.ChatRoom.size - 1) {
                                        crd!!.ChatRoom.remove(ChatRoomNum!!)
                                    }


                                    myRef.child("Account").child(Myuid!!).child("ChatNum").setValue(crd)
                                    myRef.child("Account").child(strr).child("ChatNum").setValue(crdd)
                                    ChatRoomNum = null

                                } else {
                                    strr = strr!!.replace(str, "")
                                    Myuid = str


                                }


                            } else {

                                strr = strr!!.replace(str, "")
                                Myuid = str


                                crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!

                                crdd!!.Token.remove(p0.child("Account").child(Myuid!!).child("fcmToken").getValue(true))
                                crdd!!.ChatRoom.remove(ChatRoomNum!!)

                                crd!!.Token.remove(p0.child("Account").child(strr!!).child("fcmToken").getValue(true))
                                crd!!.ChatRoom.remove(ChatRoomNum!!)


                                myRef.child("Account").child(Myuid!!).child("ChatNum").setValue(crd)
                                myRef.child("Account").child(strr).child("ChatNum").setValue(crdd)
                                ChatRoomNum = null


                            }

                        }
                    }


                    val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    startActivity(intent)
                }

                else {
                    //매칭 상대가 없을 경우



                    //  crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!


                    Myuid =FirebaseAuth.getInstance().uid





                    val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    startActivity(intent)

                }

            }
        })



    }



    private fun DirectSignUp()
    {
        var intent = Intent(this, Account::class.java)

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

    }

    /**
     * Session callback class for Kakao Login. OnSessionOpened() is called after successful login.
     */


    inner class SessionCallback(application : Application) : ISessionCallback {

        val TAG = "SessionCallback"
        val app  = application
        var sf = app.getSharedPreferences("login",0)
        var edit = sf.edit()
        val database : FirebaseDatabase? = FirebaseDatabase.getInstance()
        val myRef : DatabaseReference = database!!.reference


        override fun onSessionOpenFailed(exception: KakaoException?) {

        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {


                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e(TAG,"Fail")
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Log.d(TAG,"closed")
                }

                override fun onSuccess(result: MeV2Response?) {

                    myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if(!p0.child("Account").child(result?.id.toString()).exists()) {
                                //회원가입을 하지 않았으면
                                //Account에 내 id가 없으면
                                Log.d("wlgusdnzzz","Account에 없다"+result?.id.toString())
                                var aauth = FirebaseAuth.getInstance()



                                aauth!!.signInAnonymously()
                                        .addOnCompleteListener(this@MainActivity,object : OnCompleteListener<AuthResult?> {
                                            override fun onComplete(p0: Task<AuthResult?>) {

                                                if(p0.isSuccessful)
                                                {
                                                    edit.putInt("id", result!!.id.toInt())
                                                    Log.d("wlgusdnzzz", "익명가입성공")
                                                    edit.commit()
                                                    //DirectSignUp()

                                                }
                                                else
                                                {
                                                    Log.d("wlgusdnzzz", "익명가입실패")
                                                }



                                            }
                                        })


                            }
                            else
                            {
                                DirectLobby(result!!.id.toString())
                            }



                        }
                    })



                    }
            })
        }






    }

    private inner class KakaoSessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            //Toast.makeText(applicationContext, "Successfully logged in to Kakao. Now creating or updating a Firebase User.", Toast.LENGTH_LONG).show()
            val accessToken = Session.getCurrentSession().accessToken
            getFirebaseJwt(accessToken!!).continueWithTask { task ->
                val firebaseToken = task.result
                auth = FirebaseAuth.getInstance()
                auth!!.signInWithCustomToken(firebaseToken!!)
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.child("Account").child(auth!!.currentUser!!.uid).exists()) {
                                DirectLobby(auth!!.currentUser!!.uid)
                            } else {
                                DirectSignUp()

                            }
                        }
                    })



                    //updateUI()
                } else {
                    //Toast.makeText(applicationContext, "Failed to create a Firebase user.", Toast.LENGTH_LONG).show()
                    if (task.exception != null) {
                    }
                }
            }
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e(TAG,"2" + exception.toString())
            }
        }
    }


    companion object {

        var activity: Activity? = null
        var Accountactivity : Activity? = null
        private val TAG = MainActivity::class.java.name
        var applyactivity : Activity? = null
        var ChatRoomNum : String? = null
        var Token : String? = null
        var Myuid : String? = null
        var Mysex : String? = null
        var crd  :ChatRoomData? = ChatRoomData()
        var crdtemp  :ChatRoomData? = ChatRoomData()
        var checkapplylist : CheckApplyListData? = CheckApplyListData()
        var checkapplylistt : CheckApplyListData? = CheckApplyListData()

        var crdd  :ChatRoomData? = ChatRoomData()
        var nowChatRoomNum : String? = null
        var nowToken : String? = null
        var nowAc : String? = null

    }



}