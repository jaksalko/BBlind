package com.test.moon.bblind


import android.content.Context
import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import kotlinx.android.synthetic.main.activity_chattingroom.*
import org.json.JSONObject
import kotlinx.android.synthetic.main.activity_chattingroom.view.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.Random

class Chat : AppCompatActivity(), View.OnClickListener {

    private val RC_SIGN_IN = 1001
    private val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    private val SERVER_KEY = "AAAAghhryJU:APA91bE5FeyIHILMSGcWRgWY4hp43aQv9a5haGPMw2A5ZM0G6-102amS9gh-6YKLRRs4qAAKBE-dCBE7A1fnUjoEi3A6mZgrjVIGz-Y34x_yuOYk4fHSM_wT969p36N5oYgYobr3tyCq"

    // Firebase - Realtime Database
    private var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var mDatabaseReference: DatabaseReference? = null
    private var mChildEventListener: ChildEventListener? = null

    // Firebase - Authentication
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    // Views
    private lateinit var mListView: ListView
    private lateinit var mEdtMessage: EditText
    private lateinit var list: ListView
    private lateinit var  btn_send : Button
    private var user : FirebaseUser? = null
    // Values
    private var mAdapter: ChatAdapter? = null
    private var userName: String? = null
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    private var ChatRoomNum : String? = null
    private var chatdata : ChatData? = null

    private val fcmtoken : String? = null
    var NowChatRoomList : ChatRoomListData = ChatRoomListData()
    val ref : DatabaseReference = database.reference
    var temp : String? = null
    var OppositeId : String? = null
    var MyNick : String? = null
    var OppositeNick : String? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chattingroom)

        mListView = findViewById(R.id.list_message)
        mEdtMessage = findViewById(R.id.edit_message)
        btn_send = findViewById(R.id.btn_send)
        initViews()
        initFirebaseDatabase()
        initFirebaseAuth()
        initValues()
        user = mAuth!!.currentUser
        MainActivity.nowAc="Chat"

    }

    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.nowAc="Lobby"
        finish()
    }


    private fun initViews() {



        mAdapter = ChatAdapter(this, 0)



        mListView!!.adapter = mAdapter
        mListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val chatData = mAdapter!!.getItem(position)
            if (!TextUtils.isEmpty(chatData.userName)) {

            }
        }

        // mEdtMessage = edit_message as EditText
        btn_send.setOnClickListener(this)

        //  mBtnGoogleSignIn = btn_google_signin as SignInButton
        //  mBtnGoogleSignOut = btn_google_signout as Button
//        mBtnGoogleSignIn!!.setOnClickListener(this)
        //   mBtnGoogleSignOut!!.setOnClickListener(this)

        //   mTxtProfileInfo = txt_profile_info as TextView
        //   mImgProfile = img_profile as ImageView
    }

    private fun initFirebaseDatabase() {




        mFirebaseDatabase!!.getReference("Chat").child(MainActivity.nowChatRoomNum!!).child("Info").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if(MainActivity.Myuid!!.equals(p0.child("ManId")))
                {
                    OppositeId = p0.child("WomanId").getValue(true).toString()
                }
                else
                {
                    OppositeId = p0.child("ManId").getValue(true).toString()
                }

            if(MainActivity.nowAc.equals("Chat")) {
                NowChatRoomList = p0.child("ChatRoomList").getValue(ChatRoomListData::class.java)!!

                if (MainActivity.Mysex == "Man") {
                    temp = NowChatRoomList!!.ManMsg

                } else {
                    temp = NowChatRoomList!!.WomanMsg
                }

                if (MainActivity.Mysex == "Man") {
                    NowChatRoomList!!.WomanMsg = "0"

                } else {
                    NowChatRoomList!!.ManMsg = "0"
                }

                mFirebaseDatabase!!.getReference("Chat").child(MainActivity.nowChatRoomNum!!).child("Info").child("ChatRoomList").setValue(NowChatRoomList);
            }
            }
        })

        mFirebaseDatabase!!.getReference("Account").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                MyNick = p0.child(MainActivity.Myuid!!).child("Nickname").getValue(true).toString()



            }
        })

        if(MainActivity.nowChatRoomNum!=null) {
            ref.child("Chat").child(MainActivity.nowChatRoomNum!!).child("message")
                    .addChildEventListener(object : ChildEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                        }

                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                        }

                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                            chatdata = p0.getValue(ChatData::class.java)
                            chatdata!!.firebaseKey = p0.key
                            mAdapter!!.add(chatdata)
                            mListView!!.smoothScrollToPosition(mAdapter!!.count)
                        }

                        override fun onChildRemoved(p0: DataSnapshot) {
                            val firebaseKey = p0.key
                            val count = mAdapter!!.count
                            for (i in 0 until count) {
                                if (mAdapter!!.getItem(i).firebaseKey.equals(firebaseKey)) {
                                    mAdapter!!.remove(mAdapter!!.getItem(i))
                                    break
                                }
                            }
                        }
                    })
        }
        else
        {
            //채팅방이 존재하지 않습니다.


        }



    }

    private fun initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance()
        /*val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this) { }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()*/
        mAuthListener = FirebaseAuth.AuthStateListener { updateProfile() }
    }

    private fun initValues()
    {


        if (user == null) {
            userName = "Guest" + Random().nextInt(5000)
        } else {
            userName = user!!.displayName
        }
    }

    private fun updateProfile() {

        if (user == null) {
            // 비 로그인 상태 (메시지를 전송할 수 없다.)
            // mBtnGoogleSignIn!!.visibility = View.VISIBLE
            //  mBtnGoogleSignOut!!.visibility = View.GONE
            // mTxtProfileInfo!!.visibility = View.GONE
            // mImgProfile!!.visibility = View.GONE
            btn_send.setVisibility(View.GONE)
            mAdapter!!.setEmail(null)
            mAdapter!!.notifyDataSetChanged()
        } else {
            // 로그인 상태
            // mBtnGoogleSignIn!!.visibility = View.GONE
            // mBtnGoogleSignOut!!.visibility = View.VISIBLE
            //mTxtProfileInfo!!.visibility = View.VISIBLE
            // mImgProfile!!.visibility = View.VISIBLE
            btn_send.setVisibility(View.VISIBLE)

            userName = user!!.displayName // 채팅에 사용 될 닉네임 설정
            val email = user!!.uid
            val profile = StringBuilder()
            profile.append(userName).append("\n").append(user!!.uid)
            //  mTxtProfileInfo!!.text = profile
            mAdapter!!.setEmail(MyNick)
            mAdapter!!.notifyDataSetChanged()


            val userData = UserData()
            userData.userEmailID = user!!.uid
            ref.child(user!!.uid).child("fcmToken").addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    userData.fcmToken = p0.getValue(true).toString()
                }
            })


        }
    }



    private fun signOut() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                FirebaseAuth.getInstance().signOut()

                //val handler = Handler(Looper.getMainLooper())
                //handler.post { updateUI() }
            }
        })
    }

    private fun sendPostToFCM(message: String) {
        mFirebaseDatabase!!.getReference("Account")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val check : String = dataSnapshot.child(OppositeId!!).child("Alram").getValue(true).toString()
                        if(check.equals("true")) {


                            Thread(object : Runnable {

                                override fun run() {
                                    try {

                                        // FMC 메시지 생성 start
                                        val root = JSONObject()
                                        val notification = JSONObject()
                                        notification.put("body", message)
                                        notification.put("title", getString(R.string.app_name))

                                        root.put("notification", notification)
                                        root.put("collapse_key", "Chat")
                                        root.put("to", MainActivity.nowToken)   // FMC 메시지 생성 end


                                        val Url = URL(FCM_MESSAGE_URL)
                                        val conn = Url.openConnection() as HttpURLConnection

                                        conn.requestMethod = "POST"
                                        conn.doOutput = true
                                        conn.doInput = true

                                        conn.addRequestProperty("Authorization", "key=$SERVER_KEY")
                                        conn.setRequestProperty("Accept", "application/json")
                                        conn.setRequestProperty("Content-type", "application/json")
                                        val os = conn.outputStream
                                        os.write(root.toString().toByteArray(charset("utf-8")))
                                        os.flush()
                                        conn.responseCode


                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }

                                }
                            }).start()
                        }

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
    }

    public override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    public override fun onStop() {
        super.onStop()
        mAuth!!.removeAuthStateListener(mAuthListener!!)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()

        //한번더 누를시에 어플 종료

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            /*if (result.isSuccess) {
                val account = result.signInAccount
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                mAuth!!.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (!task.isSuccessful) {
                                Toast.makeText(con, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
            }else {
                updateProfile()
            }*/
        }
    }

    override fun onClick(v: View) {

        when (v) {
            btn_send -> {
                val message = edit_message.text.toString()
                if (!TextUtils.isEmpty(message)) {
                    edit_message!!.setText("")
                    val chatData = ChatData()
                    chatData.firebaseKey = null
                    chatData.message = message
                    chatData.time = System.currentTimeMillis()
                    chatData.userName = MyNick
                    ref.child("Chat").child(MainActivity.nowChatRoomNum!!).child("message").push().setValue(chatData)

                    sendPostToFCM(message)






                    if(MainActivity.Mysex=="Man") {

                        NowChatRoomList!!.ManMsg = (Integer.parseInt(temp)+1).toString()

                    }
                    else {
                        NowChatRoomList!!.WomanMsg = (Integer.parseInt(temp)+1).toString()

                    }

                    NowChatRoomList!!.LastMsg = message


                    ref.child("Chat").child(MainActivity.nowChatRoomNum!!).child("Info").child("ChatRoomList").setValue(NowChatRoomList)




                }
            }


        }
    }
    companion object {

        var con : Context? = null

    }

}