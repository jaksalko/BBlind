package com.test.moon.bblind

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QnA : AppCompatActivity()
{
    val arr :Array<String> = arrayOf("위팅을 통해 좋은 인연 만드셨나요? 더 나은 서비스를 제공하도록 노력하는 위팅이 되겠습니다.\n" +
            "삭제 방법은 내 신청현황, 채팅방을 모두 삭제 후에 '회원탈퇴' 버튼을 눌러서 이용이 가능합니다.\n",
            "위팅을 통해 좋은 인연을 만들 준비 되셨나요? 더 나은 서비스를 제공하도록 노력하는 위팅이 되겠습니다.\n" +
                    "불편을 드려 죄송합니다! 결제 정보를 주고받는 과정이 원활하지 못한 것으로 판단됩니다.\n" +
                    "우선 스토어에 다시 들어가셔서 결제확인 알림이 뜨는지 확인해주시고\t 그래도 해결되지 않는다면\n" +
                    "메일로 문의주시면 해결해드리겠습니다.(결제 영수증을 첨부해 주시면 더욱 빠른 해결이 가능할 수 있습니다.)\n",
            "어플리케이션의 왼쪽 메뉴 바에 알림설정을 통해 푸시알림을 키고 끌 수 있습니다.\n" +
                    "그래도 해결되지 않는 경우에는 휴대폰의 설정->알림 -> 위팅의 알림을 꺼두시면 알림이 오지 않습니다.",
            "어플리케이션 화면이 깜빡거리는 현상에 대해 데이터를 주고받는 과정이 원활하지 않았던 점 죄송합니다.\n" +
                    "빠른 시일내로 더 나은 서비스를 제공하기 위해서 노력하겠습니다.",
            "지인에게 어플리케이션을 소개하여 \n" +
                    "지인이 플레이스토어를 통해 받은 후 회원가입 시 \n" +
                    "카카오메세지 상단에 적힌 추천인 코드를 입력하면\n" +
                    "추천인과 가입자 모두에게 하트를 지급해드립니다!",
            "해당 채팅방을 꾹! 누르시면 팝업창이 뜨는데 '삭제하기' 버튼을 누를 시 \n" +
                    "해당 채팅방과 정보가 삭제됩니다.",
            "해당 채팅방을 꾹! 누르시면 팝업창이 뜨는데 '신고하기' 버튼을 누를 시\n" +
                    "신고사유와 내용을 적어주시면 관리자가 확인 후에 제재를 줄 수 있습니다.")

    var tv1 : TextView? = null
    var tv2 : TextView? = null
    var tv3 : TextView? = null
    var tv4 : TextView? = null
    var tv5 : TextView? = null
    var tv6 : TextView? = null
    var tv7 : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qna)

        tv1 = findViewById(R.id.qna1)
        tv2 = findViewById(R.id.qna2)
        tv3 = findViewById(R.id.qna3)
        tv4 = findViewById(R.id.qna4)
        tv5 = findViewById(R.id.qna5)
        tv6 = findViewById(R.id.qna6)
        tv7 = findViewById(R.id.qna7)

        tv1!!.setText(arr[0])
        tv2!!.setText(arr[1])
        tv3!!.setText(arr[2])
        tv4!!.setText(arr[3])
        tv5!!.setText(arr[4])
        tv6!!.setText(arr[5])
        tv7!!.setText(arr[6])


    }


}