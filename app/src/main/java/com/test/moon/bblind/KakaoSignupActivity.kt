package com.test.moon.bblind
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import android.content.Intent



class KakaoSignupActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun redirectLoginActivity()
    {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun redirectAccountActivity()
    {
        startActivity(Intent(this, Account::class.java))
        finish()
    }
    private fun redirectmainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }


}