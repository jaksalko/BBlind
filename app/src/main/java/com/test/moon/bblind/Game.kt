package com.test.moon.bblind

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import java.util.*

class Game : Fragment() {
    private lateinit var rullet : ImageButton
    private lateinit var child : ToggleButton
    private lateinit var adult : ToggleButton

    val child_string : Array<String> = arrayOf("눈치게임","훈민정음","더 게임 오브 데스","손병호게임","딸기","클레오파트라","잔치기","두부","홍삼","공동묘지","바니바니","출석부","지하철"
            ,"딸기당근수박참외메론","31","타이타닉","옵션걸기","오렌지게임","울트라369","텔레토비게임","후라이팬놀이")
    val child_explain_string: Array<String> = arrayOf("보이는 순간 1을 외치면 벌칙을 받지 않는 마법의 게임!!",
            "이 게임을 시작하면 기본으로 3판은 해줘야지!! 돌린사람이 초성 2개 외치기!\n사람이 많다면 와이파이로 시작하자구!(와이파이 = 정답을 외치며 머리위로 손을 들고 있는다.)",
            "신이난다~~!!! 재미난다~~!!! 더 게임 오브 데스!!!!\n 손가락 1개든 2개든 모두 할 수 있으니 정해서 시작하자구! ",
            "개발자가 첫 질문한다! 지금 취한 사람 접어!!",
            "딸기가 좋아!! 기본중의 기본이지?\n이 게임 모르면 10분안에 취한다!!\n딸기를 다른 과일로 바꾸거나 비트를 2비트, 4비트, 8비트 등 자유롭게 바꿀 수 있으니 1시간동안 이 게임만 해보자구!",
            "단순히 음만 높이면 된다!!\n안녕 클레오파트라 세상에서 제일 가는 포테이토칩!! ",
            "잔치기 잔치기 딴딴딴!!!\n지역/학교마다 방식이 다르니 말을 맞추고 시작하라구!\n참고로 한번 : 1칸, 두번 : 2칸(점프), 세칸 : 거꾸로 1칸(리버스) 가 베이직하다구!",
            "두부두부두부두부~~~\n난이도 별 4개!!!★★★★\n실수라도 3모 주문하는 날엔 그날 세모친구로 불린다구! ",
            "아싸~너!,아싸~너!  아싸 홍삼 애블바디 홍삼! \n 게임 조금 한다하는 친구들끼리 했을 때 가장 재밌는 게임!\n자리 가운데에 음료수 물 등을 적당히 섞어 놓고 시작하면 더 재밌다구!!\n이 게임으로 1시간도 가능? 어~ 가능~",
            "이게임도 기본중의 기본이지?\n 업그레이드로 바닥을 찍는 선택지도 있으니 검색해서 더 재밌는 게임 해보라구!",
            "바니바니~ 대한민국 성인중에 이게임 모르면 간첩이야~",
            "출석부~출석부! 출석부~출석부! \n개발자~  개발자 : 네~!\n서로 이름은 알지?\n이름도 모르고 있다면 자기소개부터 해야지?",
            "자기 학교가 애버라인이면 끝판왕 ㅇㅈ...."
            ,"처음에 외우기가 힘들지!\n 순서를 외워서 입에 붙기만 하면 어떤 게임보다 쉬운 게임!!",
            "베스킨! 라빈스! 서리~ 원~ 귀엽고~ 깜찍하게~ 써리~ 원~",
            "맥주잔에 맥주 반 잔을 따르고 안에 소주잔을 넣어서 소주잔이 가라앉으면 지는 게임!",
            "옵션을 걸기!\nex) 외래어 사용금지, 사람이름 말하기금지 등등이 있다.\n게임에 걸리지 않았지만 벌칙 2번을 받으면 옵션을 해제할 수 있다.",
            "오렌지 오렌지~ 오렌지 오렌지~\n노래를 부르며 가상의 오렌지를 상대에게 던져주는 방식!\n이전 사람보다 더 큰 목소리와 마임을 해야한다!",
            "숫자를 한 음절씩 말하는 것!\nex) 하, 나, 둘, 셋(짝), 넷, 다, 섯, 여(짝), 섯(짝) ...",
            "아이엠그라운드 박자에!\n1 : 짝 짝 짝 뽀\n2 : 짝 짝 나나 뽀\n3 : 짝 뚜비 나나 뽀\n 4 : 보라돌이 뚜비 나나 뽀...\n8박자까지 갔다가 줄어드는 게임!!",
            "다들 알지??\n 팅팅팅팅 탱탱탱탱 팅팅 탱탱 후라이팬 놀이!")

    val adult_string : Array<String> = arrayOf("뱀사안사","왕게임","업그레이드 이미지게임","귓속말게임","돌린사람 엉덩이로 이름쓰기","휴지 옮기기!")

    val adult_explain_string : Array<String> = arrayOf("흐흐흐 뱀사 안사? 점점 수위를 올리는 게임!","내가 왕이야! A랑 B랑 우정의 포옹 10초간 실시!","약간의 수위를 올려서!! 질문을 던졌을 때 듣기에 수위가 너무 낮으면 벌칙!!","질문하고 싶은 사람에게 원하는 질문 한가지 하기!\n나...너... 좋아하냐?","뭐해? 빨리 해~",
            "휴지 한장을 입에서 입으로 옮기는데 신체접촉없이 찢는거야!\n찢었는데 정말 조금 남았다면 다음 사람이 하기 힘들겠지?")
    var gameName : String = ""
    var gameExplain : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.roulette, container, false) as View

        child = view.findViewById(R.id.childbtn)
        adult = view.findViewById(R.id.adultbtn)
        child.setText("일반")
        adult.setText("성인")
        child.setChecked(true)
        adult.setChecked(false)

        child.setText("일반")
        child.setTextOff("일반")
        child.setTextOn("일반")
        adult.setText("성인")
        adult.setTextOff("성인")
        adult.setTextOn("성인")


       child.setTextSize(TypedValue.COMPLEX_UNIT_SP,30f)
        adult.setTextSize(TypedValue.COMPLEX_UNIT_SP,20f)
        child.setOnClickListener(View.OnClickListener {

            if (child.isChecked()) {
                SetStringChild()
                adult.setChecked(false)
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                adult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            } else {
                SetStringAdult()
                adult.setChecked(true)
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                adult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            }
        })
        adult.setOnClickListener(View.OnClickListener {

            if (adult.isChecked()) {
                SetStringAdult()
                child.setChecked(false)
                adult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            } else {
                SetStringChild()
                child.setChecked(true)
                adult.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                child.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
            }
        })
        //child.setOnClickListener { if(adult.isChecked()){adult.setChecked(false); child.setChecked(true)}; SetStringChild() }
       // adult.setOnClickListener { if(child.isChecked()){child.setChecked(false); adult.setChecked(true)}; SetStringAdult() }
        rullet = view.findViewById(R.id.btn)
        val animation : Animation = AnimationUtils.loadAnimation(context,R.anim.rotate)

        rullet.setOnClickListener { if(child.isChecked()){SetStringChild()} ;else if(adult.isChecked()){SetStringAdult()} ;rullet.startAnimation(animation) }

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                val intent = Intent(context,RoulettePopup::class.java)
                intent.putExtra("gameName",gameName)
                intent.putExtra("gameExplain",gameExplain)
                startActivity(intent)
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })



        return view


    }
    fun SetStringAdult(){
        val random = Random()
        val num = random.nextInt(adult_string.size-1)
        gameName = adult_string[num]
        gameExplain = adult_explain_string[num]
    }
    fun SetStringChild() {
        val random = Random()
        val num = random.nextInt(child_string.size - 1)
        gameName = child_string[num]
        gameExplain = child_explain_string[num]
    }
}