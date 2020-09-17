package kr.heewon.speak_word

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.widget.ImageButton
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {
    var tts: TextToSpeech? = null
    var elephant : ImageButton? = null
    var apple : ImageButton? = null
    var sunflower : ImageButton? = null
    var plus : ImageButton? = null
    var id : String = ""
    val GET_GALLERY_IMAGE = 200
    var switch = 0
    var plus_value : String? = null//사용자 추가 텍스트
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("메인화면")

        val logintent = Intent(this,LoginActivity::class.java)
        startActivityForResult(logintent,0)

        elephant = findViewById(R.id.elephant) as ImageButton
        apple = findViewById(R.id.apple) as ImageButton
        sunflower = findViewById(R.id.sunflower) as ImageButton
        plus = findViewById(R.id.plus) as ImageButton

        tts = TextToSpeech(applicationContext,
            TextToSpeech.OnInitListener { status ->
                if (status != TextToSpeech.ERROR) {
                    tts!!.language = Locale.KOREAN
                }
            })

        elephant!!.setOnClickListener {
            speak("코끼리")
        }

        apple!!.setOnClickListener {
            speak("사과")
        }

        sunflower!!.setOnClickListener {
            speak("해바라기")
        }

        plus!!.setOnClickListener {
            if(switch ==0){ // 사진첩으로 이동
                switch = 1
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, GET_GALLERY_IMAGE)
            }
            else if(switch==1){ // 단어 입력 인텐트 이동
                switch = 2
                val intent = Intent(applicationContext,PlusActivity::class.java)
                startActivityForResult(intent,0)
            }
            else speak(plus_value!!)
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(switch == 1){
            if (requestCode == GET_GALLERY_IMAGE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                val selectedImageUri = data.data
                plus!!.setImageURI(selectedImageUri)
            }
        }
        else if(switch == 2){ // 입력 단어 받아오기
            if(resultCode == Activity.RESULT_OK) plus_value = data!!.getStringExtra("value")
        }
        else{
            if(resultCode == Activity.RESULT_OK) id = data!!.getStringExtra("id")
            Toast.makeText(applicationContext,id+"님 환영합니다.",Toast.LENGTH_SHORT).show()
        }
    }

    fun speak(text: String){// 음성 출력
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ttsGreater21(text)
        else ttsUnder20(text)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    private fun ttsUnder20(text: String) {
        val map =
            HashMap<String, String>()
        map[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "MessageId"
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, map)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun ttsGreater21(text: String) {
        val utteranceId = this.hashCode().toString() + ""
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }



}