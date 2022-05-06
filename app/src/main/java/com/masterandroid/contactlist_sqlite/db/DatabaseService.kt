package com.masterandroid.contactlist_sqlite.db

import com.masterandroid.contactlist_sqlite.model.Contact


interface DatabaseService {

    fun addContact(contact: Contact)
    fun deleteContact(contact: Contact)
    fun updateContact(contact: Contact):Int
    fun getContactById(id:Int):Contact

    fun getAllContacts():ArrayList<Contact>
}