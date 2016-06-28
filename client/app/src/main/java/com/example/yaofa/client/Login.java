package com.example.yaofa.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yaofa.client.util.DialogUtil;
import com.example.yaofa.client.util.FormatCheckUtils;
import com.example.yaofa.client.util.HttpTask;
import com.example.yaofa.client.util.HttpUtil;
import com.example.yaofa.client.util.MapUrl;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaofa on 2016/4/21.
 */
public class Login extends Activity{
    EditText phoneText,passwordText;
    Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        phoneText=(EditText) findViewById(R.id.loginPhoneEditText);
        passwordText=(EditText) findViewById(R.id.loginPasswordEditText);
        loginButton=(Button) findViewById(R.id.loginButton);
        //为login按钮绑定事件监听器
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(validateLogin()){
                    if(loginSucess()){
                        Intent intent=new Intent(Login.this,Main.class);
                        startActivity(intent);
                        //结束该Activity
                        finish();
                    }
                    else {
                        DialogUtil.showDialog(Login.this,"您输入的信息有误，请重新输入",false);
                    }
                }
            }
        });
    }

    private boolean validateLogin(){
        String phone=phoneText.getText().toString().trim();
        String password=passwordText.getText().toString().trim();
        if(phone.equals("")){
            DialogUtil.showDialog(this,"电话不能为空！",false);
            return false;
        }
        else if(password.equals("")){
            DialogUtil.showDialog(this,"密码不能为空！",false);
            return false;
        }
        else if(!FormatCheckUtils.isPhone(phone)){
            DialogUtil.showDialog(this,"电话的格式不正确！",false);
            return false;
        }
        else
            return true;
    }

    private boolean loginSucess(){
        String phone=phoneText.getText().toString().trim();
        String password=passwordText.getText().toString().trim();
        JSONObject jsonObject;
        try{
            jsonObject=query(phone,password);
            Integer code = jsonObject.getInt("code");
            if(code.equals(0)){
                return true;
            }
        }
        catch (Exception e){
            DialogUtil.showDialog(this,"服务器响应异常，请稍后再试！",false);
            e.printStackTrace();
        }
        return false;
    }

    private JSONObject query(String phone,String password) throws Exception{
        Map<String,String> map=new HashMap<>();
        map.put("phone",phone);
        map.put("password",password);
        String url= HttpUtil.BASE_URL+"/user/login";
       // return new JSONObject(HttpUtil.post(url,map));
        MapUrl mapUrl=new MapUrl();
        mapUrl.setUrl(url);
        mapUrl.setJsonString(map);
        HttpTask httpTask=new HttpTask();
        return httpTask.execute(mapUrl).get();
    }
}