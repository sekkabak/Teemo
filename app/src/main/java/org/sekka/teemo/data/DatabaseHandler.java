package org.sekka.teemo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.hotspot2.pps.Credential;
import android.util.Log;

import net.jcip.annotations.ThreadSafe;

import org.sekka.teemo.data.model.LoginCredentials;

import java.util.ArrayList;
import java.util.List;

@ThreadSafe
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREDENTIALS = "credentials";
    private static final String CREDENTIALS_KEY_ID = "id";
    private static final String CREDENTIALS_KEY_NAME = "name";
    private static final String CREDENTIALS_KEY_PASSWD = "passwd";
    private static final String CREDENTIALS_KEY_LOGIN_WITH_BIO = "biometrics";

    private static final String DATABASE_NAME = "Teemo";
//    private static final String TABLE_CONTACTS = "contacts";
//    private static final String CONTACTS_KEY_ID = "id";
//    private static final String CONTACTS_KEY_NAME = "name";
//    private static final String CONTACTS_KEY_PH_NO = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // for testing :)
    public void DeleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
//        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
//                + CONTACTS_KEY_ID + " INTEGER PRIMARY KEY," + CONTACTS_KEY_NAME + " TEXT,"
//                + CONTACTS_KEY_PH_NO + " TEXT" + ")";
//        db.execSQL(CREATE_CONTACTS_TABLE);


        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE " + TABLE_CREDENTIALS + "("
                + CREDENTIALS_KEY_ID + " INTEGER PRIMARY KEY," + CREDENTIALS_KEY_NAME + " TEXT, " +
                CREDENTIALS_KEY_PASSWD + " TEXT, " + CREDENTIALS_KEY_LOGIN_WITH_BIO + " INTEGER)";
        db.execSQL(CREATE_CREDENTIALS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);

//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

//    // code to add the new contact
//    public void addContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(CONTACTS_KEY_NAME, contact.getName()); // Contact Name
//        values.put(CONTACTS_KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
//
//        // Inserting Row
//        db.insert(TABLE_CONTACTS, null, values);
//        //2nd argument is String containing nullColumnHack
//        db.close(); // Closing database connection
//    }

    public void addCredentials(LoginCredentials credentials) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CREDENTIALS_KEY_NAME, credentials.get_name());
        values.put(CREDENTIALS_KEY_PASSWD, credentials.get_passwd());
        values.put(CREDENTIALS_KEY_LOGIN_WITH_BIO, credentials.is_loginWithBiometrics() ? 1 : 0);
        db.insert(TABLE_CREDENTIALS, null, values);
        db.close();
    }

    public LoginCredentials getCredentials() {
        String selectQuery = "SELECT  * FROM " + TABLE_CREDENTIALS + ";";
        LoginCredentials credential = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            credential = new LoginCredentials(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    Integer.parseInt(cursor.getString(3)) == 1
                    );
        }

        cursor.close();
        return credential;
    }

    // code to get the single contact
//    Contact getContact(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_CONTACTS, new String[] {CONTACTS_KEY_ID,
//                        CONTACTS_KEY_NAME, CONTACTS_KEY_PH_NO}, CONTACTS_KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
//                cursor.getString(1), cursor.getString(2));
//        // return contact
//        return contact;
//    }

    // code to get all contacts in a list view
//    public List<Contact> getAllContacts() {
//        List<Contact> contactList = new ArrayList<Contact>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                Contact contact = new Contact();
//                contact.setID(Integer.parseInt(cursor.getString(0)));
//                contact.setName(cursor.getString(1));
//                contact.setPhoneNumber(cursor.getString(2));
//                // Adding contact to list
//                contactList.add(contact);
//            } while (cursor.moveToNext());
//        }
//
//        // return contact list
//        return contactList;
//    }

    // code to update the single contact
//    public int updateContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(CONTACTS_KEY_NAME, contact.getName());
//        values.put(CONTACTS_KEY_PH_NO, contact.getPhoneNumber());
//
//        // updating row
//        return db.update(TABLE_CONTACTS, values, CONTACTS_KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//    }
//
//    // Deleting single contact
//    public void deleteContact(Contact contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, CONTACTS_KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }
//
//    // Getting contacts Count
//    public int getContactsCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }

}