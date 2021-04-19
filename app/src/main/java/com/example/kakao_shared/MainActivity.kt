package com.example.kakao_shared

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.*
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button : Button = findViewById(R.id.button)
        button.setOnClickListener { kakaoLink() }
    }


    fun kakaoLink() {
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    "디저트 사진",
                    "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com").build()
                )
                    .setDescrption("아메리카노, 빵, 케익")
                    .build()
            )
            .setSocial( //하트수 표시
                SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                    .setSharedCount(30).setViewCount(40).build()
            )
            .addButton(
                ButtonObject(
                    "웹에서 보기",
                    LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl(
                        "https://developers.kakao.com"
                    ).build()
                )
            )
            .addButton(
                ButtonObject(
                    "앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()
                )
            )
            .build()

        val serverCallbackArgs: MutableMap<String, String> =
            HashMap()
        serverCallbackArgs["user_id"] = "\${current_user_id}"
        serverCallbackArgs["product_id"] = "\${shared_product_id}"

        KakaoLinkService.getInstance().sendDefault(
            this,
            params,
            serverCallbackArgs,
            object : ResponseCallback<KakaoLinkResponse?>() {
                override fun onFailure(errorResult: ErrorResult) {
                    //Logger.e(errorResult.toString())
                    Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: KakaoLinkResponse?) { // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                    Toast.makeText(applicationContext, "성공", Toast.LENGTH_SHORT).show()
                }
            })
    }
}