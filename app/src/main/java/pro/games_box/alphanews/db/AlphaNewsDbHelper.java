package pro.games_box.alphanews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import pro.games_box.alphanews.db.AlphaNewsContract.FeedEntry;

/**
 * Created by Tesla on 10.05.2017.
 */

public class AlphaNewsDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    static final String DATABASE_NAME = "rssfeed.db";

    public AlphaNewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FEED_TABLE = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FeedEntry.COLUMN_PUB_DATE + " TEXT, " +
                FeedEntry.COLUMN_GUID + " TEXT, " +
                FeedEntry.COLUMN_TITLE + " TEXT, " +
                FeedEntry.COLUMN_LINK + " TEXT, " +
                FeedEntry.COLUMN_TEXT + " TEXT, " +

                " UNIQUE (" + FeedEntry.COLUMN_GUID +  ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FEED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
