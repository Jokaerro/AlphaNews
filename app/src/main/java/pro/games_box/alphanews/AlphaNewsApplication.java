package pro.games_box.alphanews;

import com.facebook.stetho.Stetho;

import android.app.Application;
import android.content.Context;

/**
 * Created by Tesla on 10.05.2017.
 */

public class AlphaNewsApplication extends Application {
    private static AlphaNewsApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (BuildConfig.DEBUG) {
            stethoInit();
        }
    }

    private void stethoInit() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

    }

    public static AlphaNewsApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return getInstance().getApplicationContext();
    }
}
