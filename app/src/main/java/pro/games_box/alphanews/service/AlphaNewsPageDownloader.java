package pro.games_box.alphanews.service;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AlphaNewsPageDownloader extends IntentService {
    public static final String URL = "pro.games_box.alphanews.service.extra.URL";
    public static final String GUID = "pro.games_box.alphanews.service.extra.GUID";

    private Context context;

    public AlphaNewsPageDownloader() {
        super("AlphaNewsPageDownloader");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            context = getApplicationContext();
            final String action = intent.getAction();

            final String url = intent.getStringExtra(URL);
            final String guid = intent.getStringExtra(GUID);
            if(url.isEmpty() || guid.isEmpty())
                return;
            handleActionFoo(url, guid);

        }
    }

    private void handleActionFoo(String myUrl, final String guid) {
        Ion.with(context).load(myUrl).asString().setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    File file = new File(context.getCacheDir(), guid.substring(guid.length() - 10));

                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(file.getAbsoluteFile());
                        fw.write(result);
                        fw.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    Log.d(">>>FILES", result);
                }
            });
//
//            File directory = context.getCacheDir();
//            if(directory.listFiles().length > 0) {
//                for(int i = 0; i < directory.listFiles().length; i++)
//                    Log.d(">>>FILES", directory.listFiles()[i].getName());
//            }


    }

}
