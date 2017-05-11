package pro.games_box.alphanews.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import pro.games_box.alphanews.R;
import pro.games_box.alphanews.ui.fragment.AboutFragment;
import pro.games_box.alphanews.ui.fragment.NewsFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
//        startService(new Intent(this, AlphaNewsSync.class));

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, NewsFragment.newInstance(), "main")
                    .commit();
        }
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
        if (id == R.id.action_about) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.root_layout, AboutFragment.newInstance(), "about")
                    .addToBackStack("main")
                    .commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
