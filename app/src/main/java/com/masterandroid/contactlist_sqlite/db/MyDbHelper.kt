package com.masterandroid.contactlist_sqlite.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.masterandroid.contactlist_sqlite.model.Contact
import com.masterandroid.contactlist_sqlite.utils.Constant
import com.masterandroid.contactlist_sqlite.utils.Constant.ID
import com.masterandroid.contactlist_sqlite.utils.Constant.NAME
import com.masterandroid.contactlist_sqlite.utils.Constant.PHONE_NUMBER
import com.masterandroid.contactlist_sqlite.utils.Constant.TABLE_NAME

class MyDbHelper(context: Context) :
    SQLiteOpenHelper(context, Constant.DB_NAME, null, Constant.DB_VERSION), DatabaseService {


    override fun onCreate(db: SQLiteDatabase?) {

        val query =
            "create table ${Constant.TABLE_NAME} (${Constant.ID} integer not null primary key autoincrement unique," +
                    " ${Constant.NAME} text not null, ${Constant.PHONE_NUMBER} text not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${Constant.TABLE_NAME}")
        onCreate(db)
    }

    override fun addContact(contact: Contact) {
        val database = this.writableDatabase
        val contactValues = ContentValues()
        contactValues.put(Constant.NAME, contact.name)
        contactValues.put(Constant.PHONE_NUMBER, contact.phoneNumber)
        database.insert(Constant.TABLE_NAME, null, contactValues)
        database.close()
    }

    override fun deleteContact(contact: Contact) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, "${Constant.ID}=?", arrayOf("${contact.id}"))
        database.close()
    }

    override fun updateContact(contact: Contact): Int {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(Constant.ID, contact.id)
        contentValues.put(Constant.NAME, contact.name)
        contentValues.put(Constant.PHONE_NUMBER, contact.phoneNumber)
        return database.update(
            TABLE_NAME,
            contentValues,
            "${Constant.ID}=?",
            arrayOf(contact.id.toString())
        )
    }

    @SuppressLint("Recycle")
    override fun getContactById(id: Int): Contact {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(Constant.ID, Constant.NAME, Constant.PHONE_NUMBER),
           "${Constant.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor?.moveToFirst()

        return Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2))

    }

    @SuppressLint("Recycle")
    override fun getAllContacts(): ArrayList<Contact> {
        val list = ArrayList<Contact>()
        val query = "select * from $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val phone = cursor.getString(2)

                val contact = Contact(id, name, phone)
                list.add(contact)
            } while (cursor.moveToNext())
        }
        return list
    }

}