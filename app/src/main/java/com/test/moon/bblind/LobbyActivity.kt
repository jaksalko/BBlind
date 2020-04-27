package com.test.moon.bblind
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.message.template.FeedTemplate.newBuilder
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.util.helper.log.Logger
import com.test.moon.bblind.MainActivity.Companion.activity

class LobbyActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val backpress : BackPress = BackPress(this)
    private lateinit var adapter : ViewPagerAdapter
    internal val tabIcons = intArrayOf(R.drawable.homed,R.drawable.chatd,R.drawable.gamed)
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    val database : FirebaseDatabase? = FirebaseDatabase.getInstance()
    val myRef : DatabaseReference = database!!.reference
    var heartNum : TextView? = null
    var firebaseanalytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    private lateinit var navbtn : Button

    override fun onResume() {
        super.onResume()

        if(MainActivity.nowAc.equals("apply")) {
            //Toast.makeText(this@LobbyActivity,"Apply종료",Toast.LENGTH_LONG).show()
            MainActivity.applyactivity!!.finish()
        }
        if(MainActivity.Accountactivity!=null)
        {
            MainActivity.Accountactivity!!.finish()
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby2)
        navbtn = findViewById(R.id.navbtn)
        heartNum = findViewById(R.id.Lobby_Macaron)

       // Toast.makeText(this@LobbyActivity,"LobbyActivityCreate",Toast.LENGTH_LONG).show()
        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)


            navbtn.setOnClickListener {
                val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
                if(!drawer.isDrawerOpen(GravityCompat.START)){drawer.openDrawer(GravityCompat.START)}
                else drawer.closeDrawer(GravityCompat.END)
            }
        // val fab : FloatingActionButton = findViewById(R.id.fab)
        // fab.setOnClickListener { val intent = Intent(this@LobbyActivity, roulette::class.java) ; startActivity(intent) }

        viewPager = findViewById(R.id.viewpager)
        setupViewPager(viewPager)
        tabLayout = findViewById(R.id.tabs)
        tabLayout!!.setupWithViewPager(viewPager)
        setupTabIcons()
       // Toast.makeText(this,"Hi",Toast.LENGTH_LONG).show()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
          //  Toast.makeText(this,currentUser.uid,Toast.LENGTH_LONG).show()
            //var intent = Intent(this, Loading::class.java)
            //startActivity(intent)

        }else{//Toast.makeText(this,"null",Toast.LENGTH_LONG).show()
             }

        myRef.child("Account").child(MainActivity.Myuid!!).child("heart").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

               heartNum!!.text = p0.getValue(true).toString()

            }
        })

        myRef.child("Account").child(MainActivity.Myuid!!).child("ChatNum").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()) {
                    Log.d("crdcheck", "Change바뀌기전 crd 사이즈 : " + MainActivity.crd!!.ChatRoom.size)
                    MainActivity.crd = p0.getValue(ChatRoomData::class.java)!!
                    Log.d("crdcheck", "Change바뀐후 crd 사이즈 : " + MainActivity.crd!!.ChatRoom.size)
                }

            }
        })



    }




    override fun onBackPressed()
    {
        val activity = activity as MainActivity
        activity.finish()


        backpress.onBackPressed()
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        val eid = p0.itemId


        if (eid == R.id.nav_AppInformation) {
            //Toast.makeText(this,"앱정보",Toast.LENGTH_LONG).show()
            // Handle the camera action
        } else if (eid == R.id.nav_Chat) {
            //Toast.makeText(this,"문의하기",Toast.LENGTH_LONG).show()

           val email = Intent(Intent.ACTION_SEND);
            email.setType("plain/Text");
            val address = arrayOf("dotdopdeep@gmail.com")


            email.putExtra(Intent.EXTRA_EMAIL, address);

            email.putExtra(Intent.EXTRA_TEXT, "");
            email.setType("message/rfc822");
            startActivity(email);



        } else if (eid == R.id.nav_Information) {
            val intent = Intent(this@LobbyActivity, license1::class.java)
            startActivity(intent)

        } else if (eid == R.id.Account_Destroy) {
            //Toast.makeText(this,"회원탈퇴",Toast.LENGTH_LONG).show()

            if(MainActivity.checkapplylist!!.checklist!!.size>1||MainActivity.crd!!.ChatRoom.size>0||MainActivity.crd!!.Token.size>0)
            {
                Toast.makeText(LobbyActivity@this,"내 매칭신청 글과 매칭채팅방을 삭제한 뒤에 시도해주세요.",Toast.LENGTH_LONG).show()
            }
            else
            {

                val alt_bld = AlertDialog.Builder(this@LobbyActivity)
                alt_bld!!.setMessage("회원정보를 삭제하시겠습니까?").setCancelable(true).setPositiveButton("네",
                        object: DialogInterface.OnClickListener {

                            override fun onClick(p0: DialogInterface?, p1: Int) {

                                Log.d("deldeldel","1")

                                val delquery: Query = myRef.child("Account").child(MainActivity.Myuid!!)



                                delquery.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {

                                        for (appleSnapshot in p0.getChildren()) {


                                                appleSnapshot.getRef().removeValue()


                                            }
                                        }

                                    })



                            }
                        }).setNegativeButton("아니요", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.cancel()
                    }})
                alt_bld.create().show()

            }



        }else if (eid == R.id.Nav_Alram_Setting) {

            val intent = Intent(this@LobbyActivity, AlarmSetting::class.java)
            startActivity(intent)
            //Toast.makeText(this,"알림설정",Toast.LENGTH_LONG).show()

        } else if (eid == R.id.nav_QnA) {
            //
            // Toast.makeText(this,"자주묻는질문",Toast.LENGTH_LONG).show()
            val intent = Intent(this@LobbyActivity, QnA::class.java)
            startActivity(intent)

        } else if (eid == R.id.Nav_Recommend) {
            var params = TextTemplate.newBuilder("두근두근! 콩닥콩닥! \n얼굴도 모르는 사람과의 하루의 인연\n추천인 아이디는 "+MainActivity.Myuid!!+" 입니다.",
                    LinkObject.newBuilder().setAndroidExecutionParams("https://play.google.com/store/apps/details?id=com.test.moon.bblind").build()).setButtonTitle("수상한 친구 만들러가기").build()

            var serverCallbackArgs  = HashMap<String, String>();
            var aa : Map<Any,Any> = HashMap<Any,Any>()

            serverCallbackArgs.put("user_id", MainActivity.Myuid!!);

            var aaa  = object : ResponseCallback<KakaoLinkResponse>(){
                override fun onSuccess(result: KakaoLinkResponse?) {

                    Log.d("ststst","성공")

                }

                override fun onFailure(errorResult: ErrorResult?) {

                }

            }

            KakaoLinkService.getInstance().sendDefault(this, params, serverCallbackArgs,aaa)




            /* UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                 override fun onCompleteLogout() {
                     FirebaseAuth.getInstance().signOut()

                     //val handler = Handler(Looper.getMainLooper())
                     //handler.post { updateUI() }
                 }
             })*/
            //Toast.makeText(this,"추천인코드적기",Toast.LENGTH_LONG).show()
        }


        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupTabIcons() {
        for(i in 0..2) {
            val view1 = layoutInflater.inflate(R.layout.customtab, null) as View
            view1.findViewById<ImageView>(R.id.icon).setBackgroundResource(tabIcons[i])
            tabLayout!!.getTabAt(i)!!.setCustomView(view1)
        }

    }

    private fun setupViewPager(viewPager: ViewPager?) {
        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFrag(Home(), "HOME")
        adapter.addFrag(ChatRoom(), "CHAT")
        adapter.addFrag(Game(),"GAME")
        //adapter.addFrag(heart(), "HEART")
        viewPager!!.adapter = adapter
        viewPager.offscreenPageLimit= 7         // 한번에 5개의 ViewPager를 띄우겠다 -> 성능향상
    }    //ADAPT FRAGMENT

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("Activity","InActivity")
        super.onActivityResult(requestCode, resultCode, data)
        val fragmentManager = supportFragmentManager
        val fragment = adapter.getItem(3)
        if (fragment != null) {
            Log.d("Activity","toFragment")
            (fragment as Store).onActivityResult(requestCode, resultCode, data)
        }else{Log.d("Activity","NULL")}





    }



    ///////////////////////////////////// Adapter ///////////////////////////////////////////////////////////////
    internal inner class ViewPagerAdapter(manager: FragmentManager) : FragmentStatePagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()   //MAKE FRAGMENT LIST
        private val mFragmentTitleList = ArrayList<String>()  //MAKE FRAGMENT TITLE LIST

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]       // RETURN THE ITEM OBJECT(mFragmentList)
        }

        override fun getCount(): Int {
            return mFragmentList.size       //FRAGMENT SIZE
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }  //ADD FRAGMENT

        override fun getPageTitle(position: Int): CharSequence? {
            // return null to display only the icon
            return null
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
}