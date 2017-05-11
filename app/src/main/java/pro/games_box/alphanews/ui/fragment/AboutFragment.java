package pro.games_box.alphanews.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.ui.activity.MainActivity;

/**
 * Created by Tesla on 10.05.2017.
 */

public class AboutFragment extends Fragment {
    public static AboutFragment newInstance() {
        final AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_fragment, container, false);
        ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        return rootView;
    }

    @OnClick(R.id.about_email)
    public void emailMe(){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "c407@mail.ru"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dear Tesla");

        startActivity(Intent.createChooser(emailIntent, "Send email on c407@mail.ru"));
    }
}
