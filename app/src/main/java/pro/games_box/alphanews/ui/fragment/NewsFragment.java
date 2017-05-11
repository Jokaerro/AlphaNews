package pro.games_box.alphanews.ui.fragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsFeedEvent;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.model.ReceiverReadyEvent;
import pro.games_box.alphanews.ui.adapter.AlphaNewsAdapter;

/**
 * Created by Tesla on 11.05.2017.
 */

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.main_recycler) RecyclerView feedRecycler;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private AlphaNewsAdapter alphaNewsAdapter;
    private List<NewsItem> news;

    public static NewsFragment newInstance() {
        final NewsFragment fragment = new NewsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.news_fragment, container, false);
        ButterKnife.bind(this, rootView);

        alphaNewsAdapter = new AlphaNewsAdapter(getContext(), null);

        feedRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        feedRecycler.setLayoutManager(llm);

        feedRecycler.setAdapter(alphaNewsAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        return rootView;
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                EventBus.getDefault().post(new NewsFeedEvent("Give me little bite!"));
            }
        }, 1000);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<NewsItem> littleBiteNews) {
        news = littleBiteNews;
        alphaNewsAdapter = new AlphaNewsAdapter(getContext(), littleBiteNews);
        feedRecycler.swapAdapter(alphaNewsAdapter, true);
    }

    @Subscribe
    public void onMessageEvent(ReceiverReadyEvent event){
        EventBus.getDefault().post(news);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}