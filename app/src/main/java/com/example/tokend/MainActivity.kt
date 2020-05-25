package com.example.tokend

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.example.tokend.adapter.ContactsAdapter
import com.codility.contacts.model.Contact
import android.content.ContentUris
import android.net.Uri
import java.io.IOException
import java.io.InputStream

import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    var contactList = ArrayList<Contact>()
    lateinit
    var recycler: RecyclerView
    private
    var adapter: ContactsAdapter? = null

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.contactList)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        loadContacts()

    }

    private fun loadContacts() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.CALL_PHONE
                ), PERMISSIONS_REQUEST_READ_CONTACTS
            )
            //callback onRequestPermissionsResult
        } else {

            adapter = ContactsAdapter(getContacts())
            recycler.adapter = adapter

            searchbrandedit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    //after the change calling the method and passing the search input
                    filter(editable.toString())
                }
            })

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                showToast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private  fun getContacts(): ArrayList<Contact> {

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber =
                    (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()
                val photoUri =
                    (cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)))

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )

                    if (cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {

                            val number =
                                cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            contactList.add(Contact(name, photoUri))
                        }
                    }

                    cursorPhone.close()
                }

            }

        } else {
            showToast("No contacts available!")
        }
        cursor.close()
        return contactList
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun openDisplayPhoto(contactId: Long): InputStream? {
        val contactUri =
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        val displayPhotoUri =
            Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO)
        try {
            val fd = contentResolver.openAssetFileDescriptor(displayPhotoUri, "r")
            return fd!!.createInputStream()
        } catch (e: IOException) {
            return null
        }

    }

    private fun filter(text: String) {
        //new array list that will hold the filtered data
        val filteredNames = ArrayList<Contact>()
        //looping through existing elements and adding the element to filtered list
        contactList!!.filterTo(filteredNames) {
            //if the existing elements contains the search input
            it.name.toLowerCase().contains(text.toLowerCase()) || it.image!!.toLowerCase().contains(
                text.toLowerCase()
            )
        }
        //calling a method of the adapter class and passing the filtered list
        if (filteredNames != null) {
            adapter!!.filterList(filteredNames)
        }
    }

}




