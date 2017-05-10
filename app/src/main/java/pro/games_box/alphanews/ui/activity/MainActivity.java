package pro.games_box.alphanews.ui.activity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsFeedEvent;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.model.response.NewsItemResponse;
import pro.games_box.alphanews.service.AlphaNewsSync;
import pro.games_box.alphanews.ui.adapter.AlphaNewsAdapter;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_recycler) RecyclerView feedRecycler;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private AlphaNewsAdapter alphaNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        startService(new Intent(this, AlphaNewsSync.class));
        alphaNewsAdapter = new AlphaNewsAdapter(this, null);

        feedRecycler.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        feedRecycler.setLayoutManager(llm);

        feedRecycler.setAdapter(alphaNewsAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
        alphaNewsAdapter = new AlphaNewsAdapter(this, littleBiteNews);
        feedRecycler.swapAdapter(alphaNewsAdapter, true);
    }
}
