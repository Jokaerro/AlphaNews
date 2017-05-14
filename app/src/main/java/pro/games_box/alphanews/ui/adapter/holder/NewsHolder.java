package pro.games_box.alphanews.ui.adapter.holder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pro.games_box.alphanews.R;
import pro.games_box.alphanews.model.NewsItem;

/**
 * Created by Tesla on 10.05.2017.
 */

public class NewsHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.news_card_pubdate) TextView newsCardPubdate;
    @BindView(R.id.news_card_title) TextView newsCardTitle;
    @BindView(R.id.news_card) public CardView newsCard;
    @BindView(R.id.not_viewed) public ImageView unBook;

    public Context context;
    public NewsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }
    public void fill(final NewsItem item) {
        newsCardPubdate.setText(item.getPubDate());
        newsCardTitle.setText(item.getTitle());
    }
}
