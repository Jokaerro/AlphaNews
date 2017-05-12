package pro.games_box.alphanews.db;

import android.content.ContentValues;
import android.database.Cursor;

import pro.games_box.alphanews.model.NewsItem;

/**
 * Created by Tesla on 10.05.2017.
 */

public class DataMapper {
    public ContentValues fromNewsItemsResponse(NewsItem item){
        ContentValues newsItemValue = new ContentValues();
        newsItemValue.put(AlphaNewsContract.FeedEntry.COLUMN_PUB_DATE, item.getPubDate());
        newsItemValue.put(AlphaNewsContract.FeedEntry.COLUMN_GUID, item.getGuid().substring(item.getGuid().length() - 10));
        newsItemValue.put(AlphaNewsContract.FeedEntry.COLUMN_TITLE, item.getTitle());
        newsItemValue.put(AlphaNewsContract.FeedEntry.COLUMN_LINK, item.getLink());
        newsItemValue.put(AlphaNewsContract.FeedEntry.COLUMN_TEXT, item.getDescription());

        return newsItemValue;
    }

    public NewsItem fromCursorNewsItem(Cursor cursor){
        NewsItem item = new NewsItem();

        item.setPubDate(cursor.getString(cursor.getColumnIndex(AlphaNewsContract.FeedEntry.COLUMN_PUB_DATE)));
        item.setGuid(cursor.getString(cursor.getColumnIndex(AlphaNewsContract.FeedEntry.COLUMN_GUID)));
        item.setTitle(cursor.getString(cursor.getColumnIndex(AlphaNewsContract.FeedEntry.COLUMN_TITLE)));
        item.setLink(cursor.getString(cursor.getColumnIndex(AlphaNewsContract.FeedEntry.COLUMN_LINK)));
        item.setDescription(cursor.getString(cursor.getColumnIndex(AlphaNewsContract.FeedEntry.COLUMN_TEXT)));

        return item;
    }
}
