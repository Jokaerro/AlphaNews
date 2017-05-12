package pro.games_box.alphanews.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.service.AlphaNewsPageDownloader;

/**
 * Created by Tesla on 11.05.2017.
 */

public class NewsPagerAdapter extends PagerAdapter{
    private Context context;
    private List<NewsItem> news;
    private LayoutInflater inflater;
    private int currentPosition;

    private ConnectivityManager myConnMgr;
    private NetworkInfo networkinfo;

    @BindView(R.id.news_detail_pub_date) TextView pubDate;
    @BindView(R.id.news_detail_title) TextView pubTitle;
    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.fab) FloatingActionButton book;
    @BindView(R.id.share) FloatingActionButton share;

    public NewsPagerAdapter(Context context, List<NewsItem> littleBiteNews) {
        this.context = context;
        this.news = littleBiteNews;

        this.myConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkinfo = myConnMgr.getActiveNetworkInfo();
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
        currentPosition = position - 1;
        pubDate.setText(news.get(position).getPubDate());
        pubTitle.setText(news.get(position).getTitle());

        if (networkinfo != null && networkinfo.isConnected()) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(news.get(position).getLink());
        } else {
            book.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            String cacheFile = readFile(news.get(position).getGuid());
            webView.loadData(cacheFile, "text/html; charset=UTF-8", "UTF-8");
//            webView.loadData(cacheFile, "text/html; charset=WINDOWS-1251", "WINDOWS-1251");
        }
        ((ViewPager) container).addView(itemView);

        return itemView;
    }

    private String readFile(String fileName){
        String result = "";
        File cacheDir = context.getFilesDir();
        File tmpFile = new File(cacheDir.getPath() + "/" + fileName) ;

        String line="";
        StringBuilder text = new StringBuilder();
        try {
            FileReader fReader = new FileReader(tmpFile);
            BufferedReader bReader = new BufferedReader(fReader);

            while( (line=bReader.readLine()) != null  ){
                text.append(line+"\n");
            }
            result = text.toString();
            Log.d("FILE>>>", result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
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

    @OnClick(R.id.fab)
    public void cachePage(){
        if (networkinfo != null && networkinfo.isConnected()) {
            try {
                downloadOneUrl(news.get(currentPosition).getLink(), news.get(currentPosition).getGuid());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("INTERNET GONE","" );
            return;
        }
    }

    private void downloadOneUrl(String myurl, String guid) throws IOException {
        Intent intent = new Intent(context, AlphaNewsPageDownloader.class);
        intent.putExtra(AlphaNewsPageDownloader.URL, myurl);
        intent.putExtra(AlphaNewsPageDownloader.GUID, guid);
        context.startService(intent);
    }
}
