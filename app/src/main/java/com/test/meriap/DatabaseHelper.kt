package com.test.meriap

import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "User", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table User (id INT primary key, nama TEXT, email TEXT, username TEXT, password TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("drop table if exists User ")
    }

    fun insert(nama: String, email: String, username : String, password: String): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("nama", nama)
        cv.put("email", email)
        cv.put("username", username)
        cv.put("password", password)


        val result = db.insert("User", null, cv)
        if (result==-1. toLong()){
            return false
        }
        return true
    }

    fun checkUserPass(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val selection = "username = ? AND password = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query("User", null, selection, selectionArgs, null, null, null)
        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

}