package com.masterandroid.contactlist_sqlite.activity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.masterandroid.contactlist_sqlite.R
import com.masterandroid.contactlist_sqlite.adapter.ContactAdapter
import com.masterandroid.contactlist_sqlite.databinding.ActivityMainBinding
import com.masterandroid.contactlist_sqlite.databinding.MyDialogBinding
import com.masterandroid.contactlist_sqlite.db.MyDbHelper
import com.masterandroid.contactlist_sqlite.model.Contact

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var contactAdapter: ContactAdapter
    lateinit var myDbHelper: MyDbHelper
    lateinit var list: ArrayList<Contact>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        when (itemId) {
            R.id.add -> {
                val dialog = AlertDialog.Builder(this)

                val myDialogBinding = MyDialogBinding.inflate(layoutInflater, null, false)
                dialog.setView(myDialogBinding.root)
                dialog.setPositiveButton("Save", object : DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val name = myDialogBinding.etCname.text.toString()
                        val number = myDialogBinding.etCnumber.text.toString()
                        val contact = Contact(name, number)
                        if (name != "" && number!=""){
                            myDbHelper.addContact(contact)
                            list.add(contact)
                            contactAdapter.notifyItemInserted(list.size)
                        }else{
                            Toast.makeText(this@MainActivity, "Please fill in completely", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
        myDbHelper = MyDbHelper(this)
        list = myDbHelper.getAllContacts()
        contactAdapter = ContactAdapter(list, object : ContactAdapter.OnItemClickListener {
            override fun onItemClick(contact: Contact, position: Int, imageView: ImageView) {
                val popupMenu = PopupMenu(this@MainActivity, imageView)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.setOnMenuItemClickListener(object :PopupMenu.OnMenuItemClickListener{
                    override fun onMenuItemClick(item: MenuItem?): Boolean {

                        val itemId = item?.itemId
                        when(itemId){
                            R.id.edit->{
                                val dialog = AlertDialog.Builder(this@MainActivity)

                                val myDialogBinding = MyDialogBinding.inflate(layoutInflater, null, false)
                                myDialogBinding.etCname.setText(contact.name)
                                myDialogBinding.etCnumber.setText(contact.phoneNumber)

                                dialog.setView(myDialogBinding.root)
                                dialog.setPositiveButton("Edit", object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        contact.name = myDialogBinding.etCname.text.toString()
                                        contact.phoneNumber = myDialogBinding.etCnumber.text.toString()
                                        if (contact.name != "" && contact.phoneNumber!=""){
                                            myDbHelper.updateContact(contact)
                                            list.set(position,contact)
                                            contactAdapter.notifyItemChanged(position)
                                        }else{
                                            Toast.makeText(this@MainActivity, "Please fill in completely", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                })
                                dialog.show()

                            }
                            R.id.delete->{
                                myDbHelper.deleteContact(contact)
                                list.remove(contact)
                                contactAdapter.notifyItemRemoved(list.size)
                                contactAdapter.notifyItemRangeChanged(position,list.size)
                            }
                        }

                      return  true
                    }

                })
                popupMenu.show()
            }

            override fun onItemContactClick(contact: Contact) {
                callSecondActivity(contact)
            }

        })
        binding.recyclerView.adapter = contactAdapter
    }

    private fun callSecondActivity(contact: Contact) {
        val intent = Intent(this,SecondActivity::class.java)
        intent.putExtra("id",contact.id)
        startActivity(intent)
    }
}