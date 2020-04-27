package com.test.moon.bblind

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.test.moon.bblind.MainActivity.Companion.activity
import com.test.moon.bblind.MainActivity.Companion.checkapplylist
import kotlinx.android.synthetic.main.activity_apply.*

class CheckApplyActivity : AppCompatActivity()
{
    var arr1 :String= String()
    var arr2 :String= String()
    var arr3 :String= String()
    var arr4 :String= String()

    var stringdata :ArrayList<CheckString>?= ArrayList<CheckString>()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    var s1 : ArrayList<String> = ArrayList<String>()
    var s2 : ArrayList<String> = ArrayList<String>()
    var s3 : ArrayList<String> = ArrayList<String>()
    var v0 : String? = null
    var v1 : String? = null
    var v2 : String? = null
    var v3 : String? = null
    var v4 : String? = null
    var v5 : String? = null
    var v6 : String? = null
    var v7 : String? = null

    val ref : DatabaseReference = database.reference
    var lv : ListView? = null
    var iv : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_check_apply)

        val bu : Button = findViewById(R.id.check_apply)

        MainActivity.nowAc="CheckApplyActivity"

        lv  = findViewById(R.id.check_lv)
        iv = findViewById(R.id.check_iv)

        if(MainActivity.checkapplylist!!.checklist!!.size>1)
        {
            lv!!.visibility = View.VISIBLE
            iv!!.visibility = View.GONE
        }
        else
        {
            lv!!.visibility = View.GONE
            iv!!.visibility = View.VISIBLE
        }

        bu.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                val it : Intent = Intent(activity,ApplyActivity::class.java)
                it.putExtra("Subway","")
                startActivity(it)

            }
        })


        s1.add("check")
        s2.add("check")
        s3.add("check")

        for ( i in 1..MainActivity.checkapplylist!!.checklist!!.size-1)
        //1부터인 이유 = 0번째는 "초기화"
        {



            s1.add(MainActivity.checkapplylist!!.checklist!![i].split("/")[0])
            s2.add(MainActivity.checkapplylist!!.checklist!![i].split("/")[1])

            Log.d("wlgusdnzzz",s1.toString()+s2.toString())


            ref.child("Apply").child(s1[i]).child(MainActivity.Mysex!!).orderByChild("name").equalTo(MainActivity.Myuid!!).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {


                }

                override fun onDataChange(p0: DataSnapshot) {




                    Log.d("wlgusdnzzz",p0.toString())
                    v0 = p0.getValue(true).toString().split("{")[2]


                    Log.d("wlgusdnzzz",v0.toString())
                    v1 = v0!!.split(",")[0]
                    v2 = v0!!.split(",")[1]
                    v3 = v0!!.split(",")[2]
                    v4 = v0!!.split(",")[4]
                    v5 = v0!!.split(",")[5]
                    v6 = v0!!.split(",")[6]
                    v7 = v0!!.split(",")[7]


                    v1 = v1!!.split("=")[1]
                    v2 = v2!!.split("=")[1]
                    v3 = v3!!.split("=")[1]
                    v4 = v4!!.split("=")[1]
                    v5 = v5!!.split("=")[1]
                    v6 = v6!!.split("=")[1]
                    v7 = v7!!.split("=")[1]


                    var cc: CheckString = CheckString(s2[i]!!, v4!!+":00 ~ "+v5!!+":00", s1[i])

                    stringdata!!.add(cc)


                    var adap: CheckApplyListAdapter = CheckApplyListAdapter(stringdata!!)


                    adap.notifyDataSetChanged()

                    lv!!.adapter = adap

                    setListViewHeightBasedOnChildren(lv!!)


                }
            })
            setListViewHeightBasedOnChildren(lv!!)

        }

        lv!!.setOnItemLongClickListener(object: AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {

                val alt_bld = AlertDialog.Builder(this@CheckApplyActivity)
                alt_bld!!.setMessage("매칭을 삭제하시겠습니까?").setCancelable(true).setPositiveButton("네",
                        object: DialogInterface.OnClickListener {

                            override fun onClick(p0: DialogInterface?, p1: Int) {

                                val delquery: Query = ref.child("Apply").child(s1[position+1]).child(MainActivity.Mysex!!).orderByChild("name").equalTo(MainActivity.Myuid!!)


                                delquery.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {

                                        for (appleSnapshot in p0.getChildren()) {

                                            if(appleSnapshot.child("date").getValue(true).toString().equals(s2[position+1])) {

                                                appleSnapshot.getRef().removeValue()
                                                val count = MainActivity.checkapplylist!!.checklist!!.indexOf(s1[position+1]+"/"+s2[position+1])

                                                MainActivity.checkapplylist!!.checklist!!.removeAt(count)

                                                ref.child("Account").child(MainActivity.Myuid!!).child("Myapply").setValue(MainActivity.checkapplylist)


                                                MainActivity.nowAc="Main"
                                                finish()

                                            }
                                        }

                                    }
                                })


                            }
                        }).setNegativeButton("아니요", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.cancel()
                    }})
                alt_bld.create().show()
                return true

            }
        })



    }

    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.nowAc="Main"
        finish()
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