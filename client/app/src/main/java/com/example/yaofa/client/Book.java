package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.yaofa.client.util.DialogUtil;

/**
* Created by yaofa on 2016/4/26.
*/

public class Book extends Activity {
    public static final String HTML_PATH="file:///android_asset/html/book/";
    WebView webView;
    Button last,next,returnDirectory;
    int tittleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        webView=(WebView)findViewById(R.id.webView);
        last=(Button) findViewById(R.id.last);
        next=(Button) findViewById(R.id.next);
        returnDirectory=(Button) findViewById(R.id.returnDirectory);
        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent=getIntent();
        tittleId=intent.getIntExtra("id",-1);
        if (tittleId<0){
            DialogUtil.showDialog(Book.this, "无法找到你所需要的内容", false);
        }
        tittleId++;
        String url=HTML_PATH+((Integer)tittleId).toString()+".html";
        webView.loadUrl(url);
        returnDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Book.this,BookList.class);
                startActivity(intent);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Book.this,Book.class);
                if (tittleId>=61)
                    intent.putExtra("id",tittleId-1);
                else
                    intent.putExtra("id",tittleId);
                startActivity(intent);
                finish();
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Book.this,Book.class);
                if(tittleId<2)
                    intent.putExtra("id",tittleId-1);
                else
                    intent.putExtra("id",tittleId-2);
                startActivity(intent);
                finish();
            }
        });
    }
}
