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

class ChatRoomAdapter(Data: ArrayList<ChatRoomListData>) : BaseAdapter() {


    private var chatRoomListData = ArrayList<ChatRoomListData>()
    private var chatRoomListDataCount = 0
    internal var inflater: LayoutInflater? = null

    init {

        chatRoomListData = Data
        chatRoomListDataCount = Data.size

    }


    override fun getCount(): Int {

        return chatRoomListDataCount
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
            convertView = inflater!!.inflate(R.layout.chatroomlist2, parent, false)//change
         }

        //구매글들에 대한 정보들

        val NoticeDataCount = convertView!!.findViewById<TextView>(R.id.List_MeetDate)
        val NoticeDataPersonNum = convertView.findViewById<TextView>(R.id.List_PersonNum)
        val NoticeDataTitle = convertView.findViewById<TextView>(R.id.List_Subway)
        val NoticeLastMsg = convertView.findViewById<TextView>(R.id.Last_Msg)//change
        val NoticeCountMsg = convertView.findViewById<TextView>(R.id.Count_Msg)//change
        NoticeDataCount.setText(chatRoomListData[position].MeetDate)
        NoticeDataPersonNum.setText(chatRoomListData[position].PersonNum)
        NoticeLastMsg.setText(chatRoomListData[position].LastMsg)//change
        if(MainActivity.Mysex=="Man")
        NoticeCountMsg.setText(chatRoomListData[position].WomanMsg.toString())//change
        else
            NoticeCountMsg.setText(chatRoomListData[position].ManMsg.toString())//change

        return convertView!!
    }


}
