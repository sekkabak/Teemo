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
import org.sekka.teemo.data.model.StoredCredential;

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

    private static final String TABLE_STOREDCREDENTIAL = "storedcredentials";
    private static final String STOREDCREDENTIAL_KEY_ID = "id";
    private static final String STOREDCREDENTIAL_KEY_NAME = "name";
    private static final String STOREDCREDENTIAL_KEY_PASSWD = "passwd";
    private static final String STOREDCREDENTIAL_KEY_DESCRIPTION = "description";

    private static final String DATABASE_NAME = "Teemo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // for testing :)
    public void DeleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE " + TABLE_CREDENTIALS + " ("
                + CREDENTIALS_KEY_ID + " INTEGER PRIMARY KEY, " + CREDENTIALS_KEY_NAME + " TEXT, " +
                CREDENTIALS_KEY_PASSWD + " TEXT, " + CREDENTIALS_KEY_LOGIN_WITH_BIO + " INTEGER)";
        db.execSQL(CREATE_CREDENTIALS_TABLE);


        String CREATE_SSTOREDCREDENTIAL_TABLE = "CREATE TABLE " + TABLE_STOREDCREDENTIAL + " (" + STOREDCREDENTIAL_KEY_ID + " INTEGER PRIMARY KEY," + STOREDCREDENTIAL_KEY_NAME + " TEXT, " + STOREDCREDENTIAL_KEY_PASSWD + " TEXT, " + STOREDCREDENTIAL_KEY_DESCRIPTION + " TEXT)";
        db.execSQL(CREATE_SSTOREDCREDENTIAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOREDCREDENTIAL);
        onCreate(db);
    }

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

    public void addStoredCredentials(StoredCredential credential) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STOREDCREDENTIAL_KEY_NAME, credential.getName());
        values.put(STOREDCREDENTIAL_KEY_PASSWD, credential.getPassword());
        values.put(STOREDCREDENTIAL_KEY_DESCRIPTION, credential.getDescription());
        db.insert(TABLE_STOREDCREDENTIAL, null, values);
        db.close();
    }

    public ArrayList<StoredCredential> getStoredCredentials() {
        ArrayList<StoredCredential> res = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_STOREDCREDENTIAL + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                StoredCredential credential = new StoredCredential(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                res.add(credential);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return res;
    }

    public StoredCredential getStoredCredentials(int id) {
        String selectQuery = "SELECT  * FROM " + TABLE_STOREDCREDENTIAL + " WHERE " + STOREDCREDENTIAL_KEY_ID + "==" + id + ";";
        StoredCredential credential = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            credential = new StoredCredential(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
        }

        cursor.close();
        return credential;
    }

    public StoredCredential updateStoredCredentials(StoredCredential credential) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STOREDCREDENTIAL_KEY_NAME, credential.getName());
        values.put(STOREDCREDENTIAL_KEY_PASSWD, credential.getPassword());
        values.put(STOREDCREDENTIAL_KEY_DESCRIPTION, credential.getDescription());
        db.update(TABLE_STOREDCREDENTIAL, values, STOREDCREDENTIAL_KEY_ID + "=" + credential.getID(), null);

        return credential;
    }

    public void deleteStoredCredentials(int credentialID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STOREDCREDENTIAL, STOREDCREDENTIAL_KEY_ID + "=" + credentialID, null);
    }
}