package com.masterandroid.contactlist_sqlite.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.masterandroid.contactlist_sqlite.R
import com.masterandroid.contactlist_sqlite.databinding.ActivitySecondBinding
import com.masterandroid.contactlist_sqlite.db.MyDbHelper

class SecondActivity : AppCompatActivity() {
    lateinit var binding:ActivitySecondBinding
    lateinit var myDbHelper: MyDbHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDbHelper = MyDbHelper(this)
        val id = intent.getIntExtra("id", 0)
        val contact = myDbHelper.getContactById(id)
        binding.tvName.text = contact.name
        binding.tvNumber.text = contact.phoneNumber
    }
}