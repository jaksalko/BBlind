package com.test.moon.bblind

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Debug
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_apply.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account.*
import java.lang.ArithmeticException
import java.lang.Exception
import kotlin.random.Random
import kotlin.random.nextInt
import com.google.firebase.database.DataSnapshot
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import java.util.Date;


class ApplyActivity : AppCompatActivity()
{
    private val FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send"
    private val SERVER_KEY = "AAAAghhryJU:APA91bE5FeyIHILMSGcWRgWY4hp43aQv9a5haGPMw2A5ZM0G6-102amS9gh-6YKLRRs4qAAKBE-dCBE7A1fnUjoEi3A6mZgrjVIGz-Y34x_yuOYk4fHSM_wT969p36N5oYgYobr3tyCq"

    var Match = false
    private var mAuth: FirebaseAuth? = null
    private var user : FirebaseUser? = null

    var datee : String?=null
    var selectyear : Int? = null
    var selectmonth : Int? = null
    var selectday : Int? = null
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    var OppositeToken : String? = null


    val ref : DatabaseReference = database.reference
    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply)


        MainActivity.nowAc="apply"
        MainActivity.applyactivity=this


        var i =0
        mAuth = FirebaseAuth.getInstance()
        user = mAuth!!.currentUser


        var sex : String? = null
        var oppositesex : String? = null
        var id : String? = null
        var findtemp : ArrayList<IdData> = ArrayList<IdData>()
        var find : IdData? = null
        var delme : IdData? = null
        var iddata : IdData? = null

        val Chatref : DatabaseReference = database.getReference("Chat")
        val Applyref : DatabaseReference = database.getReference("Apply")
        val Hotref : DatabaseReference = database.getReference("Hot")



        val keyboard : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var personspinadapter = ArrayAdapter.createFromResource(this,R.array.person,R.layout.spinner_item)
        var agespinadapter = ArrayAdapter.createFromResource(this,R.array.age,R.layout.spinner_item)
        var timespinadapter = ArrayAdapter.createFromResource(this,R.array.time,R.layout.spinner_item)
        var drinkspinadapter = ArrayAdapter.createFromResource(this,R.array.drink,R.layout.spinner_item)

        personspinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        agespinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        timespinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        drinkspinadapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        Apply_Spinner_PersonNum.adapter = personspinadapter
        Apply_Spinner_AverageAge.adapter = agespinadapter
        Apply_Spinner_After_Time.adapter = timespinadapter
        Apply_Spinner_Before_Time.adapter = timespinadapter
        Apply_Spinner_Average_Drink.adapter = drinkspinadapter

        sex = MainActivity.Mysex
        if(sex.equals("Man"))
        {
            oppositesex = "Woman"
        }
        else
        {
            oppositesex = "Man"
        }

        //  Apply_Toggle_Sex.

        Apply_button_Cancel.setOnClickListener(object : View.OnClickListener
        {
            override fun onClick(v: View?) {

                finish()

            }

        })


        Apply_button_Notice.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                val alt_bld = AlertDialog.Builder(this@ApplyActivity)
                alt_bld!!.setMessage("매칭이 될 시 하트 20개가 소모됩니다. \n신청을 하시겠습니까?").setCancelable(true).setPositiveButton("네",
                        object: DialogInterface.OnClickListener {

                            override fun onClick(p0: DialogInterface?, p1: Int) {

                                if (Match == false) {
                                    Log.d("aaaaz", "Click Notice")


                                    val today = Date()
                                    var strdate: String? = null

                                    var format1: SimpleDateFormat? = null
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        format1 = SimpleDateFormat("yyyy-MM-dd")

                                        strdate = format1.format(today)


                                    }

                                    var selectday: Date? = null
                                    var selectdatstr: String? = null
                                    var format2: SimpleDateFormat? = null
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        format2 = SimpleDateFormat("yyyy-MM-dd")
                                        if (datee == null) {
                                            Toast.makeText(this@ApplyActivity, "날짜를 다시 입력해주세요.", Toast.LENGTH_SHORT).show()

                                        } else {

                                            selectday = format2.parse(datee)
                                            selectdatstr = format2.format(selectday)
                                        }

                                    }

                                    Log.d("aaaaz", "Try 전")
                                    try {

                                        var s4: Boolean? = null

                                        if (MainActivity.checkapplylist!!.checklist!!.size > 1) {

                                            Log.d("aaaaz", "1111")
                                            for (i in 1..MainActivity.checkapplylist!!.checklist!!.size - 1)
                                            //1부터인 이유 = 0번째는 "초기화"
                                            {
                                                //이미지난 매칭정보가 존재할 경우


                                                Log.d("aaaaz", "2222")
                                                var s1 = MainActivity.checkapplylist!!.checklist!![i].split("/")[0]
                                                var s2 = MainActivity.checkapplylist!!.checklist!![i].split("/")[1]

                                                if (s2.equals(selectdatstr)) {

                                                    Log.d("aaaaz", "3333")
                                                    s4 = true
                                                }

                                                Log.d("aaaaz", "4444")

                                            }
                                        }


                                        if (s4 == true) {
                                            throw NoSuchMethodException("Already Exist")
                                        }else if (Apply_Edittext_Introduction.text.toString().length != 5) {
                                            throw ArithmeticException("Five Introduction")
                                        } else if (Apply_Spinner_Before_Time.selectedItem.toString().toInt() + 1 > Apply_Spinner_After_Time.selectedItem.toString().toInt()) {

                                            throw ArrayStoreException("Can't use Time")

                                        } else if (selectdatstr!!.compareTo(strdate!!) <= 0 || selectdatstr.length < 7) {
                                            throw NoSuchElementException("OverDate")
                                        }else {

                                            Log.d("aaaaz", "5555")

                                            id = user!!.uid





                                            iddata = IdData(id!!, sex!!,
                                                    Apply_Spinner_Before_Time.selectedItem.toString(),
                                                    Apply_Spinner_After_Time.selectedItem.toString(),
                                                    selectdatstr,
                                                    Apply_Spinner_AverageAge.selectedItem.toString(),
                                                    Apply_Spinner_Average_Drink.selectedItem.toString(),
                                                    Apply_Edittext_Introduction.text.toString())
                                            Log.d("zczc", iddata!!.date)


                                            //*********************************************여기서 나와 같은 조건의 상대성별 확인
                                            MainActivity.checkapplylist!!.checklist!!.add(Apply_Spinner_PersonNum.selectedItem.toString() + "/" + selectdatstr)

                                            ref.child("Account").child(id!!).child("Myapply").setValue(MainActivity.checkapplylist)



                                            Applyref.child(Apply_Spinner_PersonNum.selectedItem.toString())
                                                    .child(sex!!)
                                                    .push().setValue(iddata)

                                            Hotref.child("Count").addListenerForSingleValueEvent(object: ValueEventListener {
                                                override fun onCancelled(p0: DatabaseError) {


                                                }

                                                override fun onDataChange(p0: DataSnapshot) {
                                                    var cnt: Int? = 0
                                                    if(p0.exists()) {
                                                        cnt = Integer.parseInt(p0.getValue(true).toString())
                                                        cnt++

                                                    }
                                                    else
                                                    {
                                                        cnt = 1
                                                    }
                                                    Hotref.child("Count").setValue(cnt)


                                                }
                                            })





                                            Applyref.child(Apply_Spinner_PersonNum.selectedItem.toString()).child(oppositesex!!).orderByChild("date").equalTo(selectdatstr).limitToFirst(1).addChildEventListener(object : ChildEventListener {
                                                override fun onCancelled(p0: DatabaseError) {
                                                }

                                                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                                                }

                                                override fun onChildChanged(p0: DataSnapshot, p1: String?) {

                                                }

                                                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                                                    //메소드가 find의 개수만큼 실행된다.
                                                    //ex) 3개 있으면 메소드가 3번 실행됨

                                                    if (Match == false) {
                                                        var finddate: String? = null
                                                        var findsex: String? = null
                                                        var findfrom: String? = null
                                                        var findto: String? = null
                                                        var findname: String? = null




                                                        find = p0.getValue(IdData::class.java)
                                                        finddate = find!!.date!!
                                                        findsex = find!!.sex!!
                                                        findfrom = find!!.from!!
                                                        findto = find!!.to!!
                                                        findname = find!!.name!!


                                                        if (selectdatstr.equals(finddate)) {


                                                            if (!findsex.equals(sex))//성이 다르면 add
                                                            {


                                                                if (findfrom!! <= Apply_Spinner_Before_Time.selectedItem.toString() && findto!! >= Apply_Spinner_Before_Time.selectedItem.toString()
                                                                        || findfrom!! <= Apply_Spinner_After_Time.selectedItem.toString() && findto!! >= Apply_Spinner_After_Time.selectedItem.toString())
                                                                {



                                                                    //시간 조건이 겹침

                                                                    if (sex.equals("Man")) {


                                                                        MainActivity.ChatRoomNum = id + findname

                                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ManId").setValue(id)
                                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("WomanId").setValue(findname)

                                                                    } else {

                                                                        MainActivity.ChatRoomNum = find!!.name + id

                                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ManId").setValue(findname)
                                                                        Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("WomanId").setValue(id)

                                                                    }

                                                                    ref.child("Account").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                        override fun onCancelled(p0: DatabaseError) {

                                                                        }

                                                                        override fun onDataChange(p0: DataSnapshot) {
                                                                            if (p0.child(id!!).child("ChatNum").getValue(ChatRoomData::class.java) == null) {
                                                                                MainActivity.crd = ChatRoomData()
                                                                                Log.d("zczc", "나의chatnum이 null")
                                                                            } else {
                                                                                MainActivity.crd = p0.child(id!!).child("ChatNum").getValue(ChatRoomData::class.java)
                                                                                Log.d("zczc", "나의chatnum이 null이 아님")

                                                                            }
                                                                            if (p0.child(findname!!).child("ChatNum").getValue(ChatRoomData::class.java) == null) {
                                                                                MainActivity.crdd = ChatRoomData()
                                                                                Log.d("zczc", "상대의chatnum이 null")
                                                                            } else {
                                                                                MainActivity.crdd = p0.child(findname!!).child("ChatNum").getValue(ChatRoomData::class.java)
                                                                                Log.d("zczc", "상대의chatnum이 null이 아님")
                                                                            }

                                                                            if (!MainActivity.crd!!.ChatRoom.contains(MainActivity.ChatRoomNum!!)) {
                                                                                Log.d("zczc", "crd의 size : " + MainActivity.crd!!.ChatRoom.size.toString())
                                                                                MainActivity.crd!!.ChatRoom.add(MainActivity.ChatRoomNum!!)
                                                                                MainActivity.crd!!.Token.add(p0.child(findname!!).child("fcmToken").getValue(true).toString())
                                                                                OppositeToken = p0.child(findname!!).child("fcmToken").getValue(true).toString();
                                                                                //내꺼에 상대방꺼 저장
                                                                                ref.child("Account").child(id!!).child("ChatNum").setValue(MainActivity.crd)

                                                                                if (!MainActivity.crdd!!.ChatRoom.contains(MainActivity.ChatRoomNum!!)) {

                                                                                    MainActivity.crdd!!.ChatRoom.add(MainActivity.ChatRoomNum!!)
                                                                                    MainActivity.crdd!!.Token.add(p0.child(id!!).child("fcmToken").getValue(true).toString())
                                                                                }
                                                                                ref.child("Account").child(findname).child("ChatNum").setValue(MainActivity.crdd)

                                                                                MainActivity.checkapplylistt = p0.child(findname).child("Myapply").getValue(CheckApplyListData::class.java)!!


                                                                                MainActivity.checkapplylistt!!.checklist!!.remove(Apply_Spinner_PersonNum.selectedItem.toString() + "/" + selectdatstr)

                                                                                ref.child("Account").child(findname).child("Myapply").setValue(MainActivity.checkapplylistt)





                                                                                MainActivity.checkapplylist = p0.child(id!!).child("Myapply").getValue(CheckApplyListData::class.java)!!

                                                                                MainActivity.checkapplylist!!.checklist!!.remove(Apply_Spinner_PersonNum.selectedItem.toString() + "/" + selectdatstr)

                                                                                ref.child("Account").child(id!!).child("Myapply").setValue(MainActivity.checkapplylist)

                                                                                var cd: ChatData? = ChatData()

                                                                                cd!!.message = "언행이 바른자가 미인을 얻는다."
                                                                                cd!!.userName = "Notice"
                                                                                cd!!.time = System.currentTimeMillis()




                                                                                if (selectdatstr!!.length > 7) {

                                                                                    val cr = ChatRoomListData(Apply_Spinner_PersonNum.selectedItem.toString(), selectdatstr!!, MainActivity.ChatRoomNum.toString(), "언행이 바른자가 미인을 얻는다.")



                                                                                    Chatref.child(MainActivity.ChatRoomNum.toString()).child("Info").child("ChatRoomList").setValue(cr)
                                                                                }


                                                                                Chatref.child(MainActivity.ChatRoomNum.toString()).child("message").push().setValue(cd)


                                                                                Log.d("zczc", "fcm보내기 전")

                                                                                sendPostToFCM()


                                                                                Log.d("zczc", "fcm보낸 후")


                                                                                Log.d("zczc", Apply_Spinner_PersonNum.selectedItem.toString() + "/" + id)
                                                                                Log.d("zczc", Apply_Spinner_PersonNum.selectedItem.toString() + "/" + find!!.name)
                                                                                Log.d("zczc", id + findname)
                                                                                Log.d("zczc", "delete")



                                                                                ref.child("Apply")
                                                                                        .child(Apply_Spinner_PersonNum.selectedItem.toString()).child(sex!!).orderByChild("date").equalTo(selectdatstr).addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                            override fun onCancelled(p0: DatabaseError) {


                                                                                            }

                                                                                            override fun onDataChange(p0: DataSnapshot) {


                                                                                                Log.d("zczc", "p0 : " + p0.toString())



                                                                                                for (appleSnapshot in p0.getChildren()) {

                                                                                                    if (appleSnapshot.child("name").getValue(true).toString().equals(id)) {
                                                                                                        Log.d("zczc", "내꺼 삭제")
                                                                                                        appleSnapshot.getRef().removeValue()


                                                                                                    }

                                                                                                }


                                                                                            }

                                                                                        })
                                                                                ref.child("Apply")
                                                                                        .child(Apply_Spinner_PersonNum.selectedItem.toString()).child(oppositesex!!).orderByChild("date").equalTo(selectdatstr).addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                            override fun onCancelled(p0: DatabaseError) {


                                                                                            }

                                                                                            override fun onDataChange(p0: DataSnapshot) {


                                                                                                Log.d("zczc", "p0 : " + p0.toString())



                                                                                                for (appleSnapshot in p0.getChildren()) {

                                                                                                    if (appleSnapshot.child("name").getValue(true).toString().equals(findname)) {
                                                                                                        Log.d("zczc", "상대꺼 삭제")
                                                                                                        appleSnapshot.getRef().removeValue()


                                                                                                    }

                                                                                                }


                                                                                            }

                                                                                        })


                                                                                var findheart: Int = Integer.parseInt(p0.child(findname!!).child("heart").getValue(true).toString())
                                                                                var myheart: Int = Integer.parseInt(p0.child(id!!).child("heart").getValue(true).toString())

                                                                                findheart -= 0 // for test code
                                                                                myheart -= 0

                                                                                database.getReference("Account").child(findname).child("heart").setValue(findheart)

                                                                                database.getReference("Account").child(id!!).child("heart").setValue(myheart)

                                                                                Hotref.child(Apply_Textview_Subway.text.toString()).child("Count").addListenerForSingleValueEvent(object: ValueEventListener {
                                                                                    override fun onCancelled(p0: DatabaseError) {


                                                                                    }

                                                                                    override fun onDataChange(p0: DataSnapshot) {

                                                                                        var cnt : Int = Integer.parseInt(p0.getValue(true).toString())

                                                                                        cnt-=2


                                                                                        Hotref.child("Count").setValue(cnt)

                                                                                    }
                                                                                })


                                                                                iddata = null
                                                                                find = null
                                                                                Match = true
                                                                                finish()
                                                                            } else {
                                                                            }


                                                                        }
                                                                    })




                                                                    Log.d("zczc", "finish 1번")


                                                                } else {

                                                                }


                                                            }


                                                        }
                                                    }
                                                }
                                                override fun onChildRemoved(p0: DataSnapshot) {

                                                }


                                            })

                                            finish()


                                        }

                                    } catch (e: ArithmeticException) {

                                        Toast.makeText(this@ApplyActivity, "5글자를 맞춰주세요.", Toast.LENGTH_SHORT).show()
                                        Apply_Edittext_Introduction.setText("")
                                        keyboard.showSoftInput(Apply_Edittext_Introduction, 0)


                                    } catch (e: ArrayStoreException) {
                                        Toast.makeText(this@ApplyActivity, "시간을 설정해주세요.", Toast.LENGTH_SHORT).show()


                                    } catch (e: NoSuchElementException) {
                                        Toast.makeText(this@ApplyActivity, "이미 지난 날짜입니다.", Toast.LENGTH_SHORT).show()

                                    } catch (e: NoSuchMethodException) {
                                        Toast.makeText(this@ApplyActivity, "이미 선택된 날짜로 매칭정보가 이미 존재합니다.", Toast.LENGTH_LONG).show()
                                    } catch (e: ReflectiveOperationException) {
                                        Toast.makeText(this@ApplyActivity, "하트 개수가 부족합니다.", Toast.LENGTH_SHORT).show()

                                    } catch (e: Exception) {
                                        Log.d("aaaaz", e.toString())
                                        Toast.makeText(this@ApplyActivity, "정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()

                                    }


                                }




                                }




                        }).setNegativeButton("아니요", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.cancel()
                    }})
                alt_bld.create().show()




            }

        })









    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun NoticeDate(view: View) {


        val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            datee = year.toString() +"-"+ (month + 1).toString() +"-"+ dayOfMonth.toString()
            selectyear = Integer.parseInt(year.toString())
            selectmonth = Integer.parseInt((month + 1).toString())
            selectday = Integer.parseInt(dayOfMonth.toString())

            SampleBidYearText.setText(year.toString() + "."+(month + 1).toString() + "."+dayOfMonth.toString())
        }, 2019, 0, 1)


        dialog.show()
    }


    private fun sendPostToFCM() {
        ref.child("Account")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        Log.d("checkkk", MainActivity.crd!!.Token.toString())

                        Thread(object : Runnable {

                            override fun run() {
                                try
                                {

                                    // FMC 메시지 생성 start
                                    val root = JSONObject()
                                    val notification = JSONObject()
                                    notification.put("body", "등록한 매칭이 성사되었습니다.\n상대방의 한마디 : "+Apply_Edittext_Introduction.text.toString())
                                    notification.put("title", getString(R.string.app_name))
                                    root.put("notification", notification)
                                    root.put("to", OppositeToken)   // FMC 메시지 생성 end

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

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
    }

    fun  OpenSubwayPopup(view: View) {

        var temp : String?  = null

        when(view.getId())
        {

            R.id.Apply_Button_Subway1 -> temp = "1"
            R.id.Apply_Button_Subway2 -> temp = "2"
            R.id.Apply_Button_Subway3 -> temp = "3"
            R.id.Apply_Button_Subway4 -> temp = "4"
            R.id.Apply_Button_Subway5 -> temp = "5"
            R.id.Apply_Button_Subway6 -> temp = "6"
            R.id.Apply_Button_Subway7 -> temp = "7"
            R.id.Apply_Button_Subway8 -> temp = "8"
            R.id.Apply_Button_Subway9 -> temp = "9"
            R.id.Apply_Button_Subwaygyungjung -> temp = "gyungjung"
            R.id.Apply_Button_Subwayincheon1 -> temp = "incheon1"
            R.id.Apply_Button_Subwayincheon2 -> temp = "incheon2"

        }


        val intent = Intent(this@ApplyActivity, SubwayPopup::class.java)

        intent.putExtra("Subway", temp)
        startActivityForResult(intent,0)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0)
        {

            if (resultCode == RESULT_OK)
            {


                Apply_Textview_Subway.setText(data?.getStringExtra("result"))


            }

        }

    }



}