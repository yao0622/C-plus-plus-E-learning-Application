package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.yaofa.client.util.DialogUtil;

/**
 * Created by yaofa on 2016/5/12.
 */
public class Experiment extends Activity{
    public static final String HTML_EXPERIMENT_PATH="file:///android_asset/html/experiment/";
    WebView webView;
    Button last,next,returnDirectory;
    int tittleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experiment);
        webView=(WebView) findViewById(R.id.webViewEx);
        last=(Button) findViewById(R.id.lastEx);
        next=(Button) findViewById(R.id.nextEx);
        returnDirectory=(Button) findViewById(R.id.returnDirectoryEx);
        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent=getIntent();
        tittleId=intent.getIntExtra("id",-1);
        if(tittleId<0){
            DialogUtil.showDialog(Experiment.this, "无法找到你所需要的内容", false);
        }
        tittleId++;//tittleId是从0开始的，加上1正好和html的标号对应上
        String url=HTML_EXPERIMENT_PATH+((Integer)tittleId).toString()+".html";
        webView.loadUrl(url);
        returnDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(Experiment.this,ExperimentList.class);
                startActivity(intent1);
                finish();
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(Experiment.this,Experiment.class);
                if(tittleId<2){
                    intent3.putExtra("id",tittleId-1);
                }
                else{
                    intent3.putExtra("id",tittleId-2);
                }
                startActivity(intent3);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(Experiment.this,Experiment.class);
                if(tittleId>=20){
                    intent2.putExtra("id",tittleId-1);
                }
                else {
                    intent2.putExtra("id",tittleId);
                }
                startActivity(intent2);
                finish();
            }
        });
    }
}
