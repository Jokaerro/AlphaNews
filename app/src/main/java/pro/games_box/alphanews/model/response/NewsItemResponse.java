package pro.games_box.alphanews.model.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import pro.games_box.alphanews.model.NewsChannel;
import pro.games_box.alphanews.model.NewsItem;

/**
 * Created by Tesla on 10.05.2017.
 */

@Root(name = "rss", strict=false)
public class NewsItemResponse {
    public NewsChannel getChannel() {
        return channel;
    }

    @Element(name = "channel")
    private NewsChannel channel;

    public NewsItemResponse() {}
}
