package pro.games_box.alphanews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.service.AlphaNewsSync;
import pro.games_box.alphanews.service.AlphaNewsSyncJob;

/**
 * Created by Tesla on 10.05.2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        AlphaNewsSyncJob.schedulePeriodicJob();
    }

    @OnClick(R.id.login)
    public void startMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
