package pro.games_box.alphanews.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by TESLA on 11.05.2017.
 */
@Root(name = "channel")
public class NewsChannel {
    @Element(name = "title")
    private String title;

    @Element(name = "link")
    private String link;

    @Element(name = "description")
    private String description;

    @Element(name = "language")
    private String language;

    @Element(name = "docs")
    private String docs;

    @Element(name = "managingEditor")
    private String managingEditor;

    @Element(name = "webMaster")
    private String webMaster;

    @Element(name = "pubDate")
    private String pubDate;

    @ElementList(name = "item", inline = true)
    private List<NewsItem> item;

    public NewsChannel() {}

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public List<NewsItem> getItems() {
        return item;
    }
}
