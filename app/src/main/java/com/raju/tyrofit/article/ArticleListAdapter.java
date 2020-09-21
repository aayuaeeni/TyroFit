package com.raju.tyrofit.article;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.prof.rssparser.Article;
import com.raju.tyrofit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raju Sah.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.MyViewHolder> implements Filterable
{
    private Context context;
    public List<Article> articlesList;
    public List<Article> articlesListFiltered;
    private ArticleListAdapterListner listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvDescp;
        public ImageView ivImage,ivShare;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDescp = view.findViewById(R.id.tvDescp);
            ivImage = view.findViewById(R.id.ivImage);
            ivShare = view.findViewById(R.id.ivShare);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onItemClicked(articlesListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ArticleListAdapter(Context context, List<Article> articlesList, ArticleListAdapterListner listener) {
        this.context = context;
        this.listener = listener;
        this.articlesList = articlesList;
        this.articlesListFiltered = articlesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position)
    {
        final Article article = articlesListFiltered.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDescp.setText(article.getDescription());

        if (article.getImage() != null && !TextUtils.isEmpty(article.getImage()))
        {
            Picasso.get().load(article.getImage()).placeholder(R.drawable.default_placeholder).resize(200,200).centerCrop().into(holder.ivImage);
        }
        else
        {
            Picasso.get().load(R.drawable.default_placeholder).resize(200,200).centerCrop().into(holder.ivImage);

        }

        holder.ivShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.onShareClicked(article);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return articlesListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    articlesListFiltered = articlesList;
                } else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article row : articlesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    articlesListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = articlesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                articlesListFiltered = (ArrayList<Article>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ArticleListAdapterListner
    {
        void onItemClicked(Article article);
        void onShareClicked(Article article);
    }
}
