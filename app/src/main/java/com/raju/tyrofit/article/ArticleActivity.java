package com.raju.tyrofit.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.prof.rssparser.Article;
import com.prof.rssparser.Channel;
import com.prof.rssparser.OnTaskCompleted;
import com.prof.rssparser.Parser;
import com.raju.tyrofit.R;
import com.raju.tyrofit.details.DetailsActivity;
import com.raju.tyrofit.utils.AppConstats;

import java.util.ArrayList;
import java.util.List;

import static com.raju.tyrofit.utils.AppConstats.editor;

public class ArticleActivity extends AppCompatActivity implements ArticleListAdapter.ArticleListAdapterListner
{

    private AppCompatEditText etSearch;
    private RecyclerView rvArticles;
    private List<Article> articleListLive;
    private RelativeLayout rlProgressContainer;
    private String urlString = "http://joeroganexp.joerogan.libsynpro.com/rss/";
//    private String urlString = "https://feeds.megaphone.fm/WWO8086402096/";

    private ArticleListAdapter articleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        initViews();
        setViews();
        fetchFeed();
    }

    private void setViews()
    {
        rvArticles.setHasFixedSize(true);
        rvArticles.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvArticles.setItemAnimator(new DefaultItemAnimator());
        rvArticles.setNestedScrollingEnabled(true);


        etSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.length() != 0)
                {
                    if (articleListAdapter != null)
                    {
                        articleListAdapter.getFilter().filter(s);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                {
                    if (articleListAdapter != null)
                    {
                        articleListAdapter.getFilter().filter(s);
                    }
                }

            }
        });

    }

    private void initViews()
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>()
        {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult)
            {
                Log.e("newToken", instanceIdResult.getToken());
                editor.putString(AppConstats.FIREBASE_TOKEN, instanceIdResult.getToken());
                editor.apply();
            }
        });

        etSearch = findViewById(R.id.etSearch);
        rvArticles = findViewById(R.id.rvArticles);
        rlProgressContainer = findViewById(R.id.rlProgressContainer);
        articleListLive = new ArrayList<>();

        rlProgressContainer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
    }

    public void fetchFeed()
    {
        rlProgressContainer.setVisibility(View.VISIBLE);
        Parser parser = new Parser();
        parser.execute(urlString);
        parser.onFinish(new OnTaskCompleted()
        {
            @Override
            public void onTaskCompleted(Channel channel)
            {
                try
                {
                  runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            rlProgressContainer.setVisibility(View.GONE);
                        }
                    });
                  List<Article> articleList = channel.getArticles();

                    if (articleList.size() != 0)
                    {
                        articleListLive = new ArrayList<>();
                        articleListLive.addAll(articleList);
                        articleListAdapter = new ArticleListAdapter(ArticleActivity.this,articleListLive, ArticleActivity.this);
                        rvArticles.setAdapter(articleListAdapter);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(final Exception e)
            {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        rlProgressContainer.setVisibility(View.GONE);
                        Toast.makeText(ArticleActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    @Override
    public void onItemClicked(Article article)
    {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("articleLink",article.getLink());
        intent.putExtra("articleTitle",article.getTitle());
        startActivity(intent);
    }

    @Override
    public void onShareClicked(Article article)
    {
        openShareIntentDialog(article);
    }

    private void openShareIntentDialog(Article article)
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            intent.putExtra(Intent.EXTRA_TEXT, article.getTitle() + "\n" + article.getLink());

            startActivity(Intent.createChooser(intent, "Share Via"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}