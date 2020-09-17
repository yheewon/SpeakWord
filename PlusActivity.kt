package kr.heewon.speak_word

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class PlusActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus)
        setTitle("사용자 추가 화면")

        val edit = findViewById<EditText>(R.id.edit)
        val plus = findViewById<Button>(R.id.plus)




        plus.setOnClickListener {
            val outIntent = Intent(applicationContext,MainActivity::class.java)
            outIntent.putExtra("value",edit.text.toString())
            setResult(Activity.RESULT_OK,outIntent)
            finish()
        }
    }
}