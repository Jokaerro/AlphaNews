package pro.games_box.alphanews.service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.IntentService;
import android.app.Service;
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
import pro.games_box.alphanews.model.NewsFeedEvent;
import pro.games_box.alphanews.model.response.NewsItemResponse;
import pro.games_box.alphanews.ui.activity.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlphaNewsSync extends Service {
    public static final long REQUEST_INTERVAL = 60 * 1000 * 6; // 6 minutes
    private Timer timer = null;
    private Handler handler = new Handler();

    public AlphaNewsSync() { }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        // cancel if already existed
        if (timer != null) {
            timer.cancel();
        } else {
            // recreate new
            timer = new Timer();
        }
        // schedule task
        timer.scheduleAtFixedRate(new NewsRequestTimerTask(), 0,
                REQUEST_INTERVAL);
    }

    @Subscribe
    public void onEvent(NewsFeedEvent event) throws IOException {
        requestRss();
    }

    class NewsRequestTimerTask extends TimerTask {

        @Override
        public void run() {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        requestRss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

//        private String getDateTime() {
//            // get date time in custom format
//            SimpleDateFormat sdf = new SimpleDateFormat(
//                    "[dd/MM/yyyy - HH:mm:ss]", Locale.getDefault());
//            return sdf.format(new Date());
//        }
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
        Call<NewsItemResponse> call = Api.getApiService().getFeed(1, 2, 21);
        Response<NewsItemResponse> response = call.execute();
        NewsItemResponse newsItemResponse = ((Response<NewsItemResponse>) response).body();

        EventBus.getDefault().post(newsItemResponse.getChannel().getItems());
    }
}
