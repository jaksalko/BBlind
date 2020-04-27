package com.test.moon.bblind
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_apply.*
import kotlinx.android.synthetic.main.home.*

class Home : Fragment(), View.OnClickListener
{

    private lateinit var text: TextView
    private lateinit var bu: Button

    private lateinit var Hot1: Button
    private lateinit var Hot2: Button
    private lateinit var Hot3: Button
    private lateinit var checkbu : Button

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()



    val ref : DatabaseReference = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.home, container, false) as View
        bu = view.findViewById(R.id.Home_Apply)
        Hot1 = view.findViewById(R.id.Home_Hot1)
        Hot2 = view.findViewById(R.id.Home_Hot2)
        Hot3 = view.findViewById(R.id.Home_Hot3)
        Hot1.setOnClickListener(this)
        Hot2.setOnClickListener(this)
        Hot3.setOnClickListener(this)
        bu.setOnClickListener(this)
        checkbu = view.findViewById(R.id.Home_Check_Apply)
        checkbu.setOnClickListener(this)


        ref.child("Hot").orderByChild("Count").limitToFirst(3).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                var i : Int = 0
                for (appleSnapshot in p0.getChildren())
                {

                    if(i==0)
                    {
                        Hot1.text = appleSnapshot.key.toString()

                    }
                    else if(i==1)
                    {
                        Hot2.text = appleSnapshot.key.toString()
                    }
                    else
                    {
                        Hot3.text = appleSnapshot.key.toString()
                    }
                    i++
                }

              }
        })

        ref.child("Account").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.child(MainActivity.Myuid!!).child("Myapply").exists())
                MainActivity.checkapplylist = p0.child(MainActivity.Myuid!!).child("Myapply").getValue(CheckApplyListData::class.java)
            }
        })



        return view
    }

    override fun onClick(v: View?) {

        when(v)
        {

            bu ->
            {

                            val it : Intent = Intent(activity,ApplyActivity::class.java)
                             it.putExtra("Subway","")
                            startActivity(it)

            }

            checkbu ->
            {

                val it : Intent = Intent(activity,CheckApplyActivity::class.java)
                 startActivity(it)

            }
            Hot1->
            {

                val it : Intent = Intent(activity,ApplyActivity::class.java)
                it.putExtra("Subway",Hot1.text.toString())
                startActivity(it)

            }
            Hot2 ->
            {

                val it : Intent = Intent(activity,ApplyActivity::class.java)
                it.putExtra("Subway",Hot2.text.toString())
                startActivity(it)

            }
           Hot3 ->
            {

                val it : Intent = Intent(activity,ApplyActivity::class.java)
                it.putExtra("Subway",Hot3.text.toString())
                startActivity(it)

            }

        }

    }




}