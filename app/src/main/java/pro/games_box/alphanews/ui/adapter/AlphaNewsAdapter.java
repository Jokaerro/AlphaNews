package pro.games_box.alphanews.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pro.games_box.alphanews.AlphaNewsApplication;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.db.DataMapper;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.model.response.NewsItemResponse;
import pro.games_box.alphanews.ui.activity.MainActivity;
import pro.games_box.alphanews.ui.adapter.holder.NewsHolder;
import pro.games_box.alphanews.ui.fragment.NewsDetailFragment;

/**
 * Created by Tesla on 10.05.2017.
 */

public class AlphaNewsAdapter extends RecyclerView.Adapter<NewsHolder>{
    private final Context context;
    private AlphaNewsApplication application;
    private Cursor cursor;
    private ChangeObservable dataSetObserver;
    private boolean dataValid;
    private int rowIdColumn;
    private DataMapper dataMapper = new DataMapper();
    private boolean cache;

    public AlphaNewsAdapter(Context context, Cursor data, boolean cacheView) {
        this.context = context;
        cursor = data;
        cache = cacheView;
        dataValid = data != null;
        rowIdColumn = dataValid ? cursor.getColumnIndex(BaseColumns._ID) : -1;
        application = AlphaNewsApplication.getInstance();
        if (cursor != null)
            cursor.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.news_card, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsHolder holder, int position) {
        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(holder, cursor);
    }

    public void onBindViewHolder(NewsHolder viewHolder, final Cursor cursor) {
        final NewsHolder holder = viewHolder;
        final NewsItem item = dataMapper.fromCursorNewsItem(cursor);
        holder.fill(item);

        holder.newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_layout, NewsDetailFragment.newInstance(cursor, holder.getAdapterPosition(), cache), "newsDetail")
                        .addToBackStack("main")
                        .commit();
            }
        });
        if(cache) {
//            holder.unBook.setVisibility(View.VISIBLE);
            holder.unBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File directory = context.getFilesDir();
                    if(directory.listFiles().length > 0) {
                        for(int i = 0; i < directory.listFiles().length; i++){
                            if(directory.listFiles()[i].getName().equals(item.getGuid().substring(item.getGuid().length() - 10))) {
                                directory.listFiles()[i].delete();
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    protected Cursor getCursor() {
        return cursor;
    }

    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }
        final Cursor oldCursor = cursor;
        if (oldCursor != null && dataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(dataSetObserver);
        }
        cursor = newCursor;
        if (cursor != null) {
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
            rowIdColumn = newCursor.getColumnIndexOrThrow(BaseColumns._ID);
            dataValid = true;
            notifyDataSetChanged();
            notifyItemInserted(getItemCount());
        } else {
            rowIdColumn = -1;
            dataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }

    protected Cursor getItemCursor(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (dataValid && cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIdColumn);
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if (dataValid && cursor != null) {
            return cursor.getCount();
        }
        return 0;
    }

    public class ChangeObservable extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            notifyDataSetChanged();
        }
    }

}
