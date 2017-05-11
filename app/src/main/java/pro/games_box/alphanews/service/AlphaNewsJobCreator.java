package pro.games_box.alphanews.service;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by Tesla on 11.05.2017.
 */

public class AlphaNewsJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag) {
            case AlphaNewsSyncJob.TAG:
                return new AlphaNewsSyncJob();
            default:
                return null;
        }
    }
}
