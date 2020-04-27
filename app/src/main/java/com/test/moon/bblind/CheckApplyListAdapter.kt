package com.test.moon.bblind



import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.squareup.picasso.Picasso

import java.util.ArrayList

class CheckApplyListAdapter(Data: ArrayList<CheckString>) : BaseAdapter() {


    private var CheckStringData = ArrayList<CheckString>()
    private var CheckStringDataCount = 0
    internal var inflater: LayoutInflater? = null

    init {

        CheckStringData = Data
        CheckStringDataCount = Data.size

    }


    override fun getCount(): Int {

        return CheckStringDataCount
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val context = parent.context
            if (inflater == null) {
                inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            convertView = inflater!!.inflate(R.layout.check_apply, parent, false)
        }

        //구매글들에 대한 정보들

        val da = convertView!!.findViewById<TextView>(R.id.Check_Date)
        val ti = convertView.findViewById<TextView>(R.id.Check_Time)
        val num = convertView.findViewById<TextView>(R.id.Check_PersonNum)

Log.d("checkcheck",position.toString())

        da.setText(CheckStringData[position].Date!!)
        ti.setText(CheckStringData[position].Time!!)
       num.setText(CheckStringData[position].Num!!)


        return convertView!!
    }


}
