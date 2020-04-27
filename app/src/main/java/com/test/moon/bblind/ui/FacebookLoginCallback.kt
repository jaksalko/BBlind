package com.test.moon.bblind.ui

import android.os.Bundle
import android.util.Log
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult

class FacebookLoginCallback : FacebookCallback<LoginResult> {


    // 로그인 성공 시 호출 됩니다. Access Token 발급 성공.

    override fun onSuccess(loginResult: LoginResult) {


        requestMe(loginResult.accessToken)

    }


    // 로그인 창을 닫을 경우, 호출됩니다.

    override fun onCancel() {


    }


    // 로그인 실패 시에 호출됩니다.

    override fun onError(error: FacebookException) {


    }


    // 사용자 정보 요청

    fun requestMe(token: AccessToken) {

        val graphRequest = GraphRequest.newMeRequest(token

        ) { `object`, response -> Log.e("result", `object`.toString()) }


        val parameters = Bundle()

        parameters.putString("fields", "id,name,email,gender,birthday")

        graphRequest.parameters = parameters

        graphRequest.executeAsync()

    }

}