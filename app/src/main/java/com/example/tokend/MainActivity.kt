package com.example.tokend

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokend.adapter.ContactsAdapter

class MainActivity : AppCompatActivity() {

    val REQUEST_READ_CONTACTS = 79

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contacts: RecyclerView = findViewById(R.id.contactList)
        contacts.layoutManager = LinearLayoutManager(this)


        var mycontacts: ArrayList<String>?=null
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED)
            mycontacts = getAllContacts()
        else
            requestPermission()

        contacts.adapter = ContactsAdapter(mycontacts)
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
            Toast.makeText(applicationContext,"Please allow this permission !",Toast.LENGTH_SHORT).show()
            // show UI part if you want here to show some rationale !!!
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS);
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_CONTACTS)) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_READ_CONTACTS);
        }
    }

    private fun getAllContacts(): ArrayList<String>{
        val names =  ArrayList<String>()
        val cr: ContentResolver = contentResolver
        val cursor: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null)

        if( (if(cursor != null) cursor.count else 0) > 0){
            while( cursor != null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                names.add(name)

                if( cursor.getInt(cursor.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pcur: Cursor? = cr.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI ,null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" =? ",
                        arrayOf(id), null)

                    while( pcur!!.moveToNext()) {
                        val phone = pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    pcur.close()
                }
            }
        }
        if (cursor != null) cursor.close()

        return names
    }
    }

