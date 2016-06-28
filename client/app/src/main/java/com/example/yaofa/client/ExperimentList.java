package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaofa on 2016/5/12.
 */
public class ExperimentList extends Activity {
    TextView experimentDirectory;
    ListView experimentList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experimentlist);
        experimentList=(ListView) findViewById(R.id.experimentList);
        experimentDirectory=(TextView) findViewById(R.id.experimentDirectory);
        //创建一个List集合，List集合的元素是Map
        List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
        //这个地方是可以扩展的，可以将listItem转化成一个类
        Resources resources=getResources();
        String[] tittleList=resources.getStringArray(R.array.experimentList);
        for(int i=0;i<tittleList.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("name",tittleList[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,
                R.layout.experiment_tittle_item,new String[]{"name"},new int[]{R.id.experimentTittle});
        //为ListView设置Adapter
        experimentList.setAdapter(simpleAdapter);
        //为ListView的列表项的单击事件绑定监听器
        experimentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           //第i项被激发该方法
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转到另一个界面
                Intent intent=new Intent(ExperimentList.this,Experiment.class);
                intent.putExtra("id",position);
                startActivity(intent);
                finish();
            }
        });
    }
}
