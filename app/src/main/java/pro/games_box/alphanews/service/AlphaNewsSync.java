package pro.games_box.alphanews.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class AlphaNewsSync extends Service {
    public static final long REQUEST_INTERVAL = 60 * 1000; // * 6; // 6 minutes
    private Timer timer = null;
    private Handler handler = new Handler();

    public AlphaNewsSync() {
    }

    @Override
    public void onCreate() {
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

    class NewsRequestTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            handler.post(new Runnable() {

                @Override
                public void run() {
//                    Toast.makeText(getApplicationContext(), "Пора кормить кота!",
//                            Toast.LENGTH_SHORT).show();
                }
            });
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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }
}
