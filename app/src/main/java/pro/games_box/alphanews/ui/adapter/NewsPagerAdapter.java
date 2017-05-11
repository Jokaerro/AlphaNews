package pro.games_box.alphanews.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsItem;

/**
 * Created by Tesla on 11.05.2017.
 */

public class NewsPagerAdapter extends PagerAdapter{
    private Context context;
    private List<NewsItem> news;
    private LayoutInflater inflater;
    private int currentPosition;

    @BindView(R.id.news_detail_pub_date) TextView pubDate;
    @BindView(R.id.news_detail_title) TextView pubTitle;
    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.fab) FloatingActionButton book;

    public NewsPagerAdapter(Context context, List<NewsItem> littleBiteNews) {
        this.context = context;
        this.news = littleBiteNews;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.news_pager, container,
                false);

        ButterKnife.bind(this, itemView);
        currentPosition = position;
        pubDate.setText(news.get(position).getPubDate());
        pubTitle.setText(news.get(position).getTitle());
//        webView.loadData(news.get(position).getDescription(), "text/html; charset=utf-8", "UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(news.get(position).getLink());
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    @OnClick(R.id.share)
    public void simpleShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(news.get(currentPosition).getTitle() +
                "<br/>" + news.get(currentPosition).getDescription() + "<br/>" +
                news.get(currentPosition).getLink()));
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
