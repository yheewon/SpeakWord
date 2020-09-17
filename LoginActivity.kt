package kr.heewon.speak_word

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    var myHelper : LoginDB? = null
    var sqlDB : SQLiteDatabase? = null
    var login : Button? = null
    var insert : Button? = null
    var id : EditText? = null
    var pw : EditText? = null

    var str_id : String? = null
    var str_pw : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTitle("로그인 화면")

        id = findViewById(R.id.id) as EditText
        pw = findViewById(R.id.pw) as EditText
        login = findViewById(R.id.login) as Button//로그인
        insert = findViewById(R.id.insert) as Button//회원가입
        myHelper = LoginDB(this)

        init()//디비 초기화

        login!!.setOnClickListener { //로그인 디비에 저장되어있는 id-pw일치하는지 확인
            str_id = id!!.text.toString()
            str_pw = pw!!.text.toString()
            var result = search(str_id!!,str_pw!!)

            if(result == "false") Toast.makeText(applicationContext,"다시 입력해주세요.",Toast.LENGTH_SHORT).show() // 로그인 실패
            else intent(result)
        }

        insert!!.setOnClickListener { //회원가입
            sqlDB = myHelper!!.writableDatabase
            sqlDB!!.execSQL("INSERT INTO groupTBL VALUES('" +id!!.text.toString()+"'," +pw!!.text.toString()+");")
            sqlDB!!.close()
            Toast.makeText(applicationContext,"입력됨",Toast.LENGTH_SHORT).show()
            show()
        }


    }

    fun init(){
        sqlDB = myHelper!!.writableDatabase
        myHelper!!.onUpgrade(sqlDB!!,1,2)
        Toast.makeText(applicationContext,"초기",Toast.LENGTH_SHORT).show()
        sqlDB!!.close()
    }

    fun show(){
        var d_id : String = ""
        var d_pw: String = ""
        myHelper = LoginDB(this)
        sqlDB=myHelper!!.readableDatabase
        val cursor: Cursor
        cursor=sqlDB!!.rawQuery("SELECT * FROM groupTBL;", null)
        while(cursor.moveToNext()){
            d_id+=cursor.getString(0).toString()+"\r\n"
            d_pw+=cursor.getString(1).toString()+"\r\n"
        }
        cursor.close()
        sqlDB!!.close()
        Log.i("DB",d_id)
        Log.i("DB_P",d_pw)
    }

    fun search(id:String,pw:String): String{
        var d_id = ""
        var d_pw = ""
        var result ="false"
        Log.i("DB_id",id)
        Log.i("DB_pw",pw)
        sqlDB=myHelper!!.readableDatabase
        val cursor: Cursor
        cursor=sqlDB!!.rawQuery("SELECT * FROM groupTBL;", null)
        while(cursor.moveToNext()){
            d_id =cursor.getString(0).toString()+"\r\n"
            d_pw =cursor.getString(1).toString()+"\r\n"
            var t_id = d_id.trim()//공백 제거
            var t_pw = d_pw.trim()
            Log.i("DB_data_id",t_id)
            Log.i("DB_data_pw",t_pw)
            if(t_id == id && t_pw == pw) {
                result = t_id
                break
            }
        }
        cursor.close()
        sqlDB!!.close()
        return result
    }

    fun intent(result : String){
        val outIntent = Intent(applicationContext,MainActivity::class.java)
        outIntent.putExtra("id",result)//로그인한 사용자 id 보내주기
        setResult(Activity.RESULT_OK,outIntent)
        finish()
    }


}