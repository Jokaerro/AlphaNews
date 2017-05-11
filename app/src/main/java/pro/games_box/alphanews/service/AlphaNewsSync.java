package pro.games_box.alphanews.service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pro.games_box.alphanews.api.Api;
import pro.games_box.alphanews.db.AlphaNewsContract;
import pro.games_box.alphanews.db.DataMapper;
import pro.games_box.alphanews.model.NewsFeedEvent;
import pro.games_box.alphanews.model.response.NewsItemResponse;
import pro.games_box.alphanews.ui.activity.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlphaNewsSync extends Service {
    private Context context;
    private DataMapper dataMapper = new DataMapper();
    private Thread thread;

    public AlphaNewsSync() { }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    requestRss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void requestRss() throws IOException {
        Log.d(">>>>>>>>>>","REQUEST");
        try {
            Call<NewsItemResponse> call = Api.getApiService().getFeed(1, 2, 21);
            Response<NewsItemResponse> response = call.execute();
            NewsItemResponse newsItemResponse = ((Response<NewsItemResponse>) response).body();

            if (response.isSuccessful()) {
                for (int i = 0; i < newsItemResponse.getChannel().getItems().size(); i++) {
                    ContentValues dailyValue = dataMapper.fromNewsItemsResponse(newsItemResponse.getChannel().getItems().get(i));

                    context.getContentResolver()
                            .insert(AlphaNewsContract.FeedEntry.CONTENT_URI, dailyValue);
                }
            }
        } catch (Exception ex) {
            Log.d("BAD RESPONSE","" + ex.getMessage());
        }
        stopSelf();

    }
}
