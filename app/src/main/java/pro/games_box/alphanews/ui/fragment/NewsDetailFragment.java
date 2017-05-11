package pro.games_box.alphanews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsItem;

/**
 * Created by Tesla on 10.05.2017.
 */

public class NewsDetailFragment extends Fragment {
    private List<NewsItem> currentNews;
    private int currentId;

    public static NewsDetailFragment newInstance(List<NewsItem> news, int position) {
        final NewsDetailFragment fragment = new NewsDetailFragment();
        ArrayList<NewsItem> tmp = new ArrayList<>(news);
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putParcelableArrayList("data", tmp);
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

        return rootView;
    }
}
