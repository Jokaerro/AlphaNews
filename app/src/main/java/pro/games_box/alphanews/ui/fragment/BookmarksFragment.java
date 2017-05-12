package pro.games_box.alphanews.ui.fragment;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.db.AlphaNewsContract;
import pro.games_box.alphanews.ui.activity.MainActivity;
import pro.games_box.alphanews.ui.adapter.AlphaNewsAdapter;

/**
 * Created by Tesla on 10.05.2017.
 */

public class BookmarksFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int LOADER_ID = 11;
    @BindView(R.id.main_recycler) RecyclerView feedRecycler;

    private AlphaNewsAdapter alphaNewsAdapter;

    public static BookmarksFragment newInstance() {
        final BookmarksFragment fragment = new BookmarksFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container, false);
        ButterKnife.bind(this, rootView);
        ((MainActivity) getActivity()).getSupportActionBar().show();
        alphaNewsAdapter = new AlphaNewsAdapter(getContext(), null);

        feedRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        feedRecycler.setLayoutManager(llm);

        feedRecycler.setAdapter(alphaNewsAdapter);

        getLoaderManager().initLoader(LOADER_ID, null, this);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            File directory = getContext().getCacheDir();
            ArrayList<String> fileNames = new ArrayList<>();
            String selection = "";
            if(directory.listFiles().length > 0){
                selection = AlphaNewsContract.FeedEntry.COLUMN_GUID + " IN (";
                for(int i = 0; i < directory.listFiles().length; i++){
                    fileNames.add(directory.listFiles()[i].getName());
                    selection += "?,";
                }
                selection = selection.substring(0, selection.length() - 1);
                selection += ")";
            }

            return new CursorLoader(getContext(), AlphaNewsContract.FeedEntry.buildNewsId(),
                    null, selection, fileNames.toArray(new String[fileNames.size()]), null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader,  Cursor data) {
        alphaNewsAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        alphaNewsAdapter.swapCursor(null);
    }
}
