package pro.games_box.alphanews.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    private void handleActionFoo(String myUrl, String guid) {
        InputStream inputstream = null;
        String data = "";
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(myUrl)
                    .build();

            Response response = client.newCall(request).execute();
            String output = response.body().string();

            File file = new File(context.getFilesDir(), guid.substring(guid.length() - 10));
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            fw.write(output);
            fw.close();

//            Log.d(">>>FILES", data);
//
//            File directory = context.getFilesDir();
//            if(directory.listFiles().length > 0) {
//                for(int i = 0; i < directory.listFiles().length; i++) {
//                    Log.d(">>>FILES", directory.listFiles()[i].getName());
////                    directory.listFiles()[i].delete();
//                }
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
