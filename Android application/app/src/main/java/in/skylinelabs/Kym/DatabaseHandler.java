package in.skylinelabs.Kym;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay Lohokare on 24-06-2017.
 */


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "kymMessages";

    // Contacts table name
    private static final String TABLE = "messages";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_TAG = "tag";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT," + KEY_MESSAGE + " TEXT,"
                + KEY_TAG + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // Create tables again
        onCreate(db);
    }

    void addtodatabase(ChatMessage cm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, cm.getDate());
        values.put(KEY_MESSAGE, cm.getMessage());
        values.put(KEY_TAG, cm.getTag());

        // Inserting Row
        db.insert(TABLE, null, values);
        db.close(); // Closing database connection
    }
    public List<ChatMessage> getAllMessages() {
        List<ChatMessage> messageList = new ArrayList<ChatMessage>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatMessage cm = new ChatMessage();
                cm.setId(Integer.parseInt(cursor.getString(0)));
                cm.setDate(cursor.getString(1));
                cm.setMessage(cursor.getString(2));
                cm.setTag(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                messageList.add(cm);
            } while (cursor.moveToNext());
        }


        return messageList;
    }
}













