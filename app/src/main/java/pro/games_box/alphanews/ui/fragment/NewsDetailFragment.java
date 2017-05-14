package pro.games_box.alphanews.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.db.DataMapper;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.ui.activity.MainActivity;
import pro.games_box.alphanews.ui.adapter.NewsPagerAdapter;

/**
 * Created by Tesla on 10.05.2017.
 */

public class NewsDetailFragment extends Fragment {
    private List<NewsItem> currentNews;
    private int currentId;
    private boolean cache;
    private static DataMapper dataMapper = new DataMapper();
    @BindView(R.id.pager) ViewPager pager;
    private NewsPagerAdapter pagerAdapter;

    public static NewsDetailFragment newInstance(Cursor cursor, int position, boolean cacheView) {
        final NewsDetailFragment fragment = new NewsDetailFragment();
        ArrayList<NewsItem> tmp = new ArrayList<>();
        if(cursor.moveToFirst())
            tmp.add(dataMapper.fromCursorNewsItem(cursor));
        else
            return null;
        while (cursor.moveToNext()){
            tmp.add(dataMapper.fromCursorNewsItem(cursor));
        }
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelableArrayList("data", tmp);
        args.putBoolean("cache", cacheView);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);
        currentNews = getArguments().getParcelableArrayList("data");
        currentId = getArguments().getInt("position");
        cache = getArguments().getBoolean("cache");

        pagerAdapter = new NewsPagerAdapter(getContext(), currentNews, cache, this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(currentId);

        ActionBar actionBar =  ((MainActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            if(cache){
                String cacheStr = getResources().getString(R.string.action_cache);
                String newsTitleStr = getResources().getString(R.string.news_details);
                actionBar.setTitle(cacheStr + " " + newsTitleStr.toLowerCase());
            }
            else
                actionBar.setTitle(R.string.news_details);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                getFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pagerAdapter.onActivityResult(requestCode, resultCode, data);
    }
}
