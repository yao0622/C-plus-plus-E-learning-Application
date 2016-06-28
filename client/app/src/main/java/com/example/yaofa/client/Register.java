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
 * Created by yaofa on 2016/4/19.
 */
public class Register extends Activity {

    EditText registerPhone,registerName,registerPassword,registerConfirmPassword;
    Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        registerPhone=(EditText)findViewById(R.id.registerPhoneEditText);
        registerName=(EditText)findViewById(R.id.registerNameEditText);
        registerPassword=(EditText)findViewById(R.id.registerPasswordEditText);
        registerConfirmPassword=(EditText) findViewById(R.id.registerConfirmPasswordEditText);
        registerButton=(Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行输入校验
                if(validateRegister()){
                    if(registerOk()){
                        //启动Main
                        Intent intent=new Intent(Register.this,Main.class);
                        startActivity(intent);
                        //结束目前的Activity
                        finish();
                    }
                    else{
                        DialogUtil.showDialog(Register.this,"你输入的用户名和电话已经存在！",false);
                    }
                }
                else {
                    DialogUtil.showDialog(Register.this,"注册失败！",false);
                }
            }
        });
    }
    private boolean registerOk(){
        //判断注册是否成功
        JSONObject jsonObj;
        String username=registerName.getText().toString().trim();
        String password=registerPassword.getText().toString().trim();
        String phone=registerPhone.getText().toString().trim();
        try{
            jsonObj=insert(username,password,phone);
            Integer code = jsonObj.getInt("code");
            if(code.equals(0)){
                return true;
            }
        }
        catch (Exception e){
            DialogUtil.showDialog(this,"服务器异常，请稍后再试",false);
            e.printStackTrace();
        }
        return false;
    }

    //对输入的各种信息进行校验
    private boolean validateRegister(){
        String username=registerName.getText().toString().trim();
        String password=registerPassword.getText().toString().trim();
        String confirmPassword=registerConfirmPassword.getText().toString().trim();
        String phone=registerPhone.getText().toString().trim();
        if(username.equals("")){
            DialogUtil.showDialog(this,"用户名不能为空！",false);
            return false;
        }
        if(password.equals("")){
            DialogUtil.showDialog(this,"密码不能为空！",false);
            return false;
        }
        if(!password.equals(confirmPassword)){
            DialogUtil.showDialog(this,"两次输入的密码不一致！",false);
            return false;
        }
        if(!FormatCheckUtils.isPhone(phone)){
            DialogUtil.showDialog(this,"电话格式不正确！",false);
            return false;
        }
        return true;
    }

    private JSONObject insert (String username,String password,String phone) throws Exception {
        //使用Map封装请求参数
        Map<String,String> map=new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        map.put("phone",phone);
        //定义发送请求的URL
        String url= HttpUtil.BASE_URL+"/user/register";
        MapUrl mapUrl=new MapUrl();
        mapUrl.setUrl(url);
        mapUrl.setJsonString(map);
        HttpTask httpTask=new HttpTask();
        return httpTask.execute(mapUrl).get();
    }
}