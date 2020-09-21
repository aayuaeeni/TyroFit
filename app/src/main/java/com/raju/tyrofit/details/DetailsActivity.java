package com.raju.tyrofit.details;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raju.tyrofit.R;

public class DetailsActivity extends AppCompatActivity
{
    private WebView webView;
    private RelativeLayout rlFullView;
    private ImageView ivBackArrow;
    private TextView tvHeading;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        setViews();
    }

    private void initViews()
    {
        webView = findViewById(R.id.webView);
        rlFullView = findViewById(R.id.rlFullView);
        ivBackArrow = findViewById(R.id.ivBackArrow);
        tvHeading = findViewById(R.id.tvHeading);

        ivBackArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void setViews()
    {
        String artilcleLink = getIntent().getStringExtra("articleLink");
        String articleTitle = getIntent().getStringExtra("articleTitle");
        tvHeading.setText(articleTitle);
        if (artilcleLink != null && !TextUtils.isEmpty(artilcleLink))
        {
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(artilcleLink);
        }

        webView.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                rlFullView.setVisibility(View.GONE);
            }
        });
    }
}