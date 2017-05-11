package pro.games_box.alphanews.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.service.AlphaNewsSync;
import pro.games_box.alphanews.service.AlphaNewsSyncJob;

/**
 * Created by Tesla on 10.05.2017.
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.input_phone) EditText phone;
    @BindView(R.id.flag) ImageView flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ButterKnife.bind(this);
        startService(new Intent(this, AlphaNewsSync.class));
        AlphaNewsSyncJob.schedulePeriodicJob();

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = editable.toString();
                if(phone.startsWith("8")){
                    flag.setBackground(getResources().getDrawable(R.drawable.ru));
                } else if(phone.startsWith("+7")){
                    flag.setBackground(getResources().getDrawable(R.drawable.ru));
                } else if(phone.startsWith("+374")){
                    flag.setBackground(getResources().getDrawable(R.drawable.ar));
                } else if(phone.startsWith("+375")){
                    flag.setBackground(getResources().getDrawable(R.drawable.br));
                } else if(phone.startsWith("+992")){
                    flag.setBackground(getResources().getDrawable(R.drawable.tj));
                } else if(phone.startsWith("+380")){
                    flag.setBackground(getResources().getDrawable(R.drawable.ua));
                } else if(phone.startsWith("+998")){
                    flag.setBackground(getResources().getDrawable(R.drawable.uz));
                } else if(phone.startsWith("+666")) {
                    flag.setBackground(getResources().getDrawable(R.drawable.prt));
                } else {
                    flag.setBackground(null);
                }
            }
        });
    }

    @OnClick(R.id.login)
    public void startMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
