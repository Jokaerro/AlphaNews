package pro.games_box.alphanews.service;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import pro.games_box.alphanews.AlphaNewsApplication;

/**
 * Created by Tesla on 11.05.2017.
 */

public class AlphaNewsSyncJob extends Job {
    public static final String TAG = "job_news_tag";
    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        AlphaNewsApplication inst = AlphaNewsApplication.getInstance();
        inst.startService(new Intent(inst.getAppContext(), AlphaNewsSync.class));
        return Result.SUCCESS;
    }

    public static  void schedulePeriodicJob() {
        int jobId = new JobRequest.Builder(AlphaNewsSyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setPersisted(true)
                .build()
                .schedule();
    }
}
