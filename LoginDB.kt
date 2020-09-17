package kr.heewon.speak_word

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LoginDB (context: Context?):
    SQLiteOpenHelper(context, "groupDB", null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE groupTBL ( gId CHAR(10), gPw INTEGER )")
    }
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ){
        db.execSQL("DROP TABLE IF EXISTS groupTBL")
        onCreate(db)
    }
}

