package com.test.moon.bblind
import android.annotation.SuppressLint
import android.os.Bundle

import kotlinx.android.synthetic.main.asd.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.random.Random


class msg : AppCompatActivity() {

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.asd)
        val ff : FirebaseDatabase = FirebaseDatabase.getInstance()
        val dd : DatabaseReference = ff.getReference()
       var userName = "user" + Random.nextInt(10000)


        var adapter = ArrayAdapter<Any>(this, android.R.layout.simple_list_item_1, android.R.id.text1)
        listv.setAdapter(adapter)


       dd.child("ApplyM").child("SubwayStation").child("동대문역사문화공원").child("2명").child("0").addChildEventListener(object : ChildEventListener {  // message는 child의 이벤트를 수신합니다.
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val chatData = dataSnapshot.getValue<IdData>(IdData::class.java!!)  // chatData를 가져오고

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

}