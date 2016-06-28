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
 * Created by yaofa on 2016/4/29.
 */
public class BookList extends Activity {
    TextView tittleDirectory;
    ListView bookList;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);
        bookList=(ListView) findViewById(R.id.bookList);
        tittleDirectory=(TextView) findViewById(R.id.tittleDirectory);
        //创建一个List集合，List集合的元素是Map
        List<Map<String,Object>> listItems=
                new ArrayList<Map<String,Object>>();
        //这个地方是可以扩展的，当每个
        // listItem要加其他东西的时候可以将其转化为一个类
        // TODO: 2016/5/4  listItem要加其他东西的时候可以将其转化为一个类
        Resources res=getResources();
        String[] tittleList=res.getStringArray(R.array.bookTittleList);
        for(int i=0;i<tittleList.length;i++){
            Map<String,Object> listItem=new HashMap<String,Object>();
            listItem.put("name",tittleList[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,listItems,
                R.layout.book_tittle_item,new String[]{"name"},new int[]{R.id.bookTittle});

        //为ListView设置Adapter
        bookList.setAdapter(simpleAdapter);
        //为ListView的列表项的单击事件绑定事件监听器
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //第i项被单击时激发该方法
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //跳转到另一个界面，
                Intent intent=new Intent(BookList.this,Book.class);
                intent.putExtra("id",position);
                startActivity(intent);
                finish();
            }
        });
    }
}
