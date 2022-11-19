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

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // for testing :)
    public void DeleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CREDENTIALS_TABLE = "CREATE TABLE " + TABLE_CREDENTIALS + "("
                + CREDENTIALS_KEY_ID + " INTEGER PRIMARY KEY," + CREDENTIALS_KEY_NAME + " TEXT, " +
                CREDENTIALS_KEY_PASSWD + " TEXT, " + CREDENTIALS_KEY_LOGIN_WITH_BIO + " INTEGER)";
        db.execSQL(CREATE_CREDENTIALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDENTIALS);
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
}