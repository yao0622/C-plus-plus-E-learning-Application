package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by yaofa on 2016/4/24.
 */
public class Main extends Activity {
//视频、ppt、大纲讲义、在线答题、实验讲义
    Button mainBook,mainPpt,mainVideo,mainExam,mainExperiment,mainRes;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mainRes=(Button) findViewById(R.id.mainResource);
        mainRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,ResList.class);
                startActivity(intent);
            }
        });

        mainPpt=(Button) findViewById(R.id.mainPpt);
        mainPpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,PptList.class);
                startActivity(intent);
            }
        });

        mainExperiment=(Button) findViewById(R.id.mainExperiment);
        mainExperiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,ExperimentList.class);
                startActivity(intent);
            }
        });

        mainBook=(Button) findViewById(R.id.mainBook);
        mainBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,BookList.class);
                startActivity(intent);
            }
        });

        mainVideo=(Button)findViewById(R.id.mainVideo);
        mainVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,VideoList.class);
                startActivity(intent);
            }
        });

        mainExam=(Button)findViewById(R.id.mainExam);
        mainExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main.this,ExamShow.class);
                startActivity(intent);
            }
        });

    }
}