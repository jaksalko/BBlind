package com.test.moon.bblind

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import android.content.Intent
import android.widget.TextView


class RoulettePopup : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)


        setContentView(R.layout.roulette_popup)
        val name : TextView = findViewById(R.id.Game_Name)
        val ex : TextView = findViewById(R.id.Game_Explain)
        val intent = intent
        val gameName : String = intent.getStringExtra("gameName")
        val gameExplain : String = intent.getStringExtra("gameExplain")
        name.text = gameName
        ex.text = gameExplain

    }
    public fun mOnClose(v : View){
        finish()
    }
    public override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!!.action == MotionEvent.ACTION_OUTSIDE){return false}
        return true;
    }
    public override fun onBackPressed() {
       return;
    }
}