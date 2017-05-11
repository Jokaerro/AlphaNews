package pro.games_box.alphanews.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tesla on 10.05.2017.
 */

public class AlphaNewsContract {
    public static final String CONTENT_AUTHORITY = "pro.games_box.alphanews";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NEWS = "newsfeed";
    public static final class FeedEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_NEWS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NEWS;

        public static final String TABLE_NAME = "newsfeed";
        public static final String COLUMN_PUB_DATE = "pubdate";
        public static final String COLUMN_GUID = "guid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_LINK = "link";
        public static final String COLUMN_TEXT = "description";

        public static Uri buildNewsId() {
            return BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_NEWS).build();
        }
    }
}
