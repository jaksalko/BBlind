package com.test.moon.bblind

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class ChatRoom : Fragment() {
    private lateinit var list: ListView
    private lateinit var layout : LinearLayout
    private lateinit var bu : Button
    var posi : Int?=null

    var al = ArrayList<ChatRoomListData>()
    var all = ArrayList<ChatRoomListData>()
    private var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref : DatabaseReference = mFirebaseDatabase.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()

       all = ArrayList<ChatRoomListData>()
        MainActivity.crdtemp =  ChatRoomData()
        al = all



        if(MainActivity.crd!!.ChatRoom.size>0)
        {
            list.visibility=View.VISIBLE
            layout.visibility = View.GONE
        }
        else
        {
            list.visibility=View.GONE
            layout.visibility = View.VISIBLE
        }


            for (i in 0..MainActivity.crd!!.ChatRoom.size - 1) {
                if (!MainActivity.crdtemp!!.ChatRoom.contains(MainActivity.crd!!.ChatRoom.get(i))) {
                    MainActivity.crdtemp!!.ChatRoom.add(MainActivity.crd!!.ChatRoom.get(i))
                }
            }




            ref.child("Chat").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    al.removeAll(al);

                    for (i in 0..MainActivity.crd!!.ChatRoom.size - 1) {
                        Log.d("zczc","사이즈"+MainActivity.crd!!.ChatRoom.size.toString())
                        Log.d("zczc",MainActivity.crd!!.ChatRoom[i])
                        var cd : ChatRoomListData = ChatRoomListData()
                        cd.ChatRoomNum= p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").child("chatRoomNum").getValue(true).toString()!!
                        cd.LastMsg= p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").child("lastMsg").getValue(true).toString()!!
                        cd.ManMsg= p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").child("manMsg").getValue(true).toString()!!
                        cd.MeetDate= p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").child("meetDate").getValue(true).toString()!!
                        cd.PersonNum= p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").child("personNum").getValue(true).toString()!!
                        cd.WomanMsg= p0.child(MainActivity.crd!!.ChatRoom[i]).child("Info").child("ChatRoomList").child("womanMsg").getValue(true).toString()!!


                        al.add(cd!!)

                        for (i in 0..al.size - 1) {
                            if (!all.contains(al.get(i))) {
                                all.add(al.get(i))
                            } else {
                                Log.d("aaaaz", "존재해서 안더함")
                            }
                        }
                    }
                    Log.d("aaaaz : al", all.size.toString())
                    Log.d("aaaaz : all", all.size.toString())
                    var adap = ChatRoomAdapter(all)

                    adap.notifyDataSetChanged()

                    list.adapter = adap
                    setListViewHeightBasedOnChildren(list)


                }


            })



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.chat, container, false) as View

        list = view.findViewById(R.id.list)
        layout = view.findViewById(R.id.check_chat)
        bu = view.findViewById(R.id.chat_apply)
        if(MainActivity.crd!!.ChatRoom.size>0)
        {
            list.visibility=View.VISIBLE
            layout.visibility = View.GONE
        }
        else
        {
            list.visibility=View.GONE
            layout.visibility = View.VISIBLE
        }

        bu.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                val it : Intent = Intent(activity,ApplyActivity::class.java)
                it.putExtra("Subway","")
                startActivity(it)

            }
        })

        list.setOnItemLongClickListener(object: AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {



                ref.child("Chat").addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot)
                    {

                        if(p0.child(al[position].ChatRoomNum!!).exists()) {


                            MainActivity.nowChatRoomNum = al[position].ChatRoomNum
                            val num = MainActivity.crd!!.ChatRoom.indexOf(MainActivity.nowChatRoomNum)
                            Log.d("aaaaz : num", num.toString() + MainActivity.crd!!.Token.size.toString())
                            MainActivity.nowToken = MainActivity.crd!!.Token[num]

                            val intent : Intent = Intent(context,DeclarationPopup::class.java)

                            startActivityForResult(intent,2)

                            posi= position
                        }
                        else
                        {
                            Toast.makeText(context, "이미 삭제된 채팅방입니다.",Toast.LENGTH_LONG).show()
                            al.removeAt(position)
                            var adap = ChatRoomAdapter(al)

                            adap.notifyDataSetChanged()

                            list.adapter = adap
                            setListViewHeightBasedOnChildren(list)
                        }

                    }
                })











                return true


            }
        })



        list.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                ref.child("Chat").addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot)
                    {

                        if(p0.child(al[position].ChatRoomNum!!).exists()) {


                            MainActivity.nowChatRoomNum = al[position].ChatRoomNum
                            val num = MainActivity.crd!!.ChatRoom.indexOf(MainActivity.nowChatRoomNum)
                            Log.d("aaaaz : num", num.toString() + MainActivity.crd!!.Token.size.toString())
                            MainActivity.nowToken = MainActivity.crd!!.Token[num]
                            Log.d("dedede","position = "+position)
                            Log.d("dedede","ChatRoomNum = "+MainActivity.nowChatRoomNum)
                            Log.d("dedede","Token = "+MainActivity.nowToken)

                            val i = Intent(activity, Chat::class.java)
                            //i.putExtra("token",)
                            startActivity(i)
                        }
                        else
                        {
                            Toast.makeText(context, "이미 삭제된 채팅방입니다.",Toast.LENGTH_LONG).show()
                            al.removeAt(position)
                            var adap = ChatRoomAdapter(al)

                            adap.notifyDataSetChanged()

                            list.adapter = adap
                            setListViewHeightBasedOnChildren(list)
                        }

                    }
                })



            }
        })




        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                //데이터 받기

        when(data!!.getStringExtra("select"))
        {

            "0"->
            {
                var intent : Intent = Intent(context,Declaration::class.java)
                intent.putExtra("id",MainActivity.nowChatRoomNum!!.replace(MainActivity.Myuid!!,""))
                startActivity(intent)


            }
            "1"-> {
                val alt_bld = context?.let { AlertDialog.Builder(it) }
                alt_bld!!.setMessage("매칭을 삭제하시겠습니까?").setCancelable(true).setPositiveButton("네",
                        object : DialogInterface.OnClickListener {

                            override fun onClick(p0: DialogInterface?, p1: Int) {


                                MainActivity.nowChatRoomNum = al[posi!!].ChatRoomNum



                                ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {


                                    }

                                    override fun onDataChange(p0: DataSnapshot) {


                                        val crld: ChatRoomListData = p0.child("Chat").child(MainActivity.nowChatRoomNum!!).child("Info").child("ChatRoomList").getValue(ChatRoomListData::class.java)!!

                                        crld.MeetDate = "0"

                                        ref.child("Chat").child(MainActivity.nowChatRoomNum!!).child("Info").child("ChatRoomList").setValue(crld)


                                        MainActivity.crd = p0.child("Account").child(MainActivity.Myuid!!).child("ChatNum").getValue(ChatRoomData::class.java)!!


                                        Log.d("zczc111", "1")

                                        var strr = MainActivity.nowChatRoomNum!!


                                        val today = Date()
                                        var strdate: String? = null

                                        var format1: SimpleDateFormat? = null
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            format1 = SimpleDateFormat("yyyy-MM-dd")

                                            strdate = format1.format(today)
                                            Log.d("zczc111", format1!!.format(today))

                                        }

                                        if (p0.child("Chat").child(MainActivity.nowChatRoomNum!!).exists()) {
                                            //이미 지난 채팅방이 존재할 경우 삭제

                                            Log.d("zczc111", "2")

                                            Log.d("zczc111", "3")
                                            ref.child("Chat").child(MainActivity.nowChatRoomNum!!).removeValue()

                                            strr = strr!!.replace(MainActivity.Myuid!!, "")
                                            Log.d("cancellll", "zz지워짐" + strr + "/" + MainActivity.Myuid)


                                            Log.d("zczc111", "4")

                                            MainActivity.crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!

                                            for (i in 0..MainActivity.crdd!!.Token.size - 1) {
                                                MainActivity.crdd!!.Token.remove(p0.child("Account").child(MainActivity.Myuid!!).child("fcmToken").getValue(true))
                                            }
                                            for (i in 0..MainActivity.crdd!!.ChatRoom.size - 1) {
                                                MainActivity.crdd!!.ChatRoom.remove(MainActivity.nowChatRoomNum!!)
                                            }

                                            for (i in 0..MainActivity.crd!!.Token.size - 1) {
                                                MainActivity.crd!!.Token.remove(p0.child("Account").child(strr!!).child("fcmToken").getValue(true))
                                            }
                                            for (i in 0..MainActivity.crd!!.ChatRoom.size - 1) {
                                                MainActivity.crd!!.ChatRoom.remove(MainActivity.nowChatRoomNum!!)
                                            }


                                            ref.child("Account").child(MainActivity.Myuid!!).child("ChatNum").setValue(MainActivity.crd)
                                            ref.child("Account").child(strr).child("ChatNum").setValue(MainActivity.crdd)
                                            MainActivity.ChatRoomNum = null


                                        }



                                        al.removeAt(posi!!)
                                        var adap = ChatRoomAdapter(al)

                                        adap.notifyDataSetChanged()

                                        list.adapter = adap
                                        setListViewHeightBasedOnChildren(list)
                                    }
                                })


                            }
                        }).setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.cancel()
                    }
                })
                alt_bld.create().show()
            }
        }
            }

        }

    }
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
                ?: // pre-condition
                return

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

}