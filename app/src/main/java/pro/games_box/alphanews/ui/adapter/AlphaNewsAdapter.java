package pro.games_box.alphanews.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pro.games_box.alphanews.AlphaNewsApplication;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsItem;
import pro.games_box.alphanews.model.response.NewsItemResponse;
import pro.games_box.alphanews.ui.adapter.holder.NewsHolder;

/**
 * Created by Tesla on 10.05.2017.
 */

public class AlphaNewsAdapter extends RecyclerView.Adapter<NewsHolder>{
    private final Context context;
    private AlphaNewsApplication application;
    private static List<NewsItem> news;

    public AlphaNewsAdapter(Context context, List<NewsItem> data) {
        this.context = context;
        application = AlphaNewsApplication.getInstance();
        if (data != null)
            news = new ArrayList<>(data);
        else
            news = new ArrayList<>();
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.news_card, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsHolder holder, int position) {
        holder.fill(news.get(position));
        holder.newsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked:" + news.get(holder.getAdapterPosition()).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (news != null) {
            return news.size();
        }
        return 0;
    }

}
