package pro.games_box.alphanews.ui.adapter;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.joaquimley.faboptions.FabOptions;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKServiceActivity;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.games_box.alphanews.AlphaNewsApplication;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.service.AlphaNewsPageDownloader;

/**
 * Created by Tesla on 11.05.2017.
 */

public class NewsPagerAdapter extends PagerAdapter{
    private static final String VKACCOUNT_TOKEN = "VKACCOUNT_TOKEN";

    private Context context;
    private List<NewsItem> news;
    private LayoutInflater inflater;
    private int currentPosition;
    private boolean cache;

    private ConnectivityManager myConnMgr;
    private NetworkInfo networkinfo;
    private View thisView;
    private AlphaNewsApplication appInstance;
    private Fragment parent;

    private SharedPreferences alphaPreferences;
    private String vkToken = "";

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @BindView(R.id.news_detail_pub_date) TextView pubDate;
    @BindView(R.id.news_detail_title) TextView pubTitle;
    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.fab_options) FabOptions fabOptions;

    public NewsPagerAdapter(Context context, List<NewsItem> littleBiteNews, boolean cacheView, Fragment parent) {
        this.context = context;
        this.news = littleBiteNews;
        this.cache = cacheView;
        this.parent = parent;
        appInstance = AlphaNewsApplication.getInstance();
        this.myConnMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.networkinfo = myConnMgr.getActiveNetworkInfo();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(parent);
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

        alphaPreferences = context.getSharedPreferences("alpha_main", Context.MODE_PRIVATE);
        vkToken = alphaPreferences.getString(VKACCOUNT_TOKEN, "");

        if (!cache) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(news.get(position).getLink());
        } else {
            fabOptions.setVisibility(View.GONE);
            String cacheFile = readFile(news.get(position).getGuid());
            webView.loadData(cacheFile, "text/html; charset=UTF-8", "UTF-8");
        }
        ((ViewPager) container).addView(itemView);
        thisView = itemView;
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                String token = res.accessToken;
                SharedPreferences.Editor editor = alphaPreferences.edit();
                editor.putString(VKACCOUNT_TOKEN, token);
                editor.apply();
                vkShare();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(context, "Error access", Toast.LENGTH_SHORT);
            }
        }));
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }

    @OnClick(R.id.action_facebook)
    public void facebookShare(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(news.get(currentPosition).getTitle())
                    .setContentDescription(news.get(currentPosition).getDescription())
                    .setContentUrl(Uri.parse(news.get(currentPosition).getLink()))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @OnClick(R.id.action_vk)
    public void vkShare(){
        if(VKSdk.isLoggedIn()) {
            VKShareDialogBuilder builder = new VKShareDialogBuilder();
            builder.setText(Html.fromHtml(news.get(currentPosition).getDescription()));
            builder.setAttachmentLink(news.get(currentPosition).getTitle(),
                    news.get(currentPosition).getLink());
            builder.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
                @Override
                public void onVkShareComplete(int postId) {
                    // recycle bitmap if need
                    VKSdk.logout();
                }

                @Override
                public void onVkShareCancel() {
                    // recycle bitmap if need
                }

                @Override
                public void onVkShareError(VKError error) {
                    // recycle bitmap if need
                }
            });
            builder.show(parent.getFragmentManager(), "VK_SHARE_DIALOG");
        } else {
            makeShareVk();
        }
    }

    private void makeShareVk(){
        Intent intent = new Intent(context, VKServiceActivity.class);
        intent.putExtra("arg1", "Authorization");
        ArrayList scopes = new ArrayList<>();
        scopes.add(VKScope.FRIENDS);
        scopes.add(VKScope.WALL);
        scopes.add(VKScope.PHOTOS);
        scopes.add(VKScope.OFFLINE);
        scopes.add(VKScope.EMAIL);
        intent.putStringArrayListExtra("arg2", scopes);
        intent.putExtra("arg4", VKSdk.isCustomInitialize());
        parent.startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.getOuterCode());
    }

    @OnClick(R.id.action_share)
    public void simpleShare(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(news.get(currentPosition).getTitle() +
                "<br/>" + news.get(currentPosition).getDescription() + "<br/>" +
                news.get(currentPosition).getLink()));
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    @OnClick(R.id.action_book)
    public void bookPage(){
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
