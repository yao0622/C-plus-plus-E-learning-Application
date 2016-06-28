package com.example.yaofa.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.yaofa.client.model.ExamEntity;
import com.example.yaofa.client.util.DialogUtil;
import com.example.yaofa.client.util.HttpTask;
import com.example.yaofa.client.util.HttpUtil;
import com.example.yaofa.client.util.MapUrl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaofa on 2016/5/9.
 */
public class ExamShow extends Activity {
    RadioButton[] radioButtons;
    RadioGroup radioGroup;
    Button lastQuiz,nextQuiz;
    TextView explanation,question;
    List<ExamEntity> examList;
    ExamEntity examEntity;
    int index=0,randomExamNum=25;
    boolean wrongMode=false; //是否核对答案

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exam);
        radioButtons=new RadioButton[4];
        question=(TextView) findViewById(R.id.question);
        explanation=(TextView)findViewById(R.id.explanation);
        lastQuiz=(Button)findViewById(R.id.lastQuiz);
        nextQuiz=(Button) findViewById(R.id.nextQuiz);
        radioButtons[0]=(RadioButton)findViewById(R.id.optionA);
        radioButtons[1]=(RadioButton) findViewById(R.id.optionB);
        radioButtons[2]=(RadioButton) findViewById(R.id.optionC);
        radioButtons[3]=(RadioButton) findViewById(R.id.optionD);
        radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
        JSONObject jsonObject;
        examList=new ArrayList<ExamEntity>();
        index=0;
        try{
            jsonObject=queryRandomExam();
            Integer code=jsonObject.getInt("code");
            if (code.equals(0)){
                JSONArray array = jsonObject.getJSONArray("randomExamList");
                //length = array.length();
                for(int i=0;i<array.length();i++){
                    examEntity=new ExamEntity();
                    JSONObject obj = (JSONObject)array.get(i);
                    examEntity.setLesson_id(obj.getInt("lesson_id"));
                    examEntity.setType(obj.getInt("type"));
                    examEntity.setQuestion(obj.getString("question"));
                    examEntity.setOption_a(obj.getString("option_a"));
                    examEntity.setOption_b(obj.getString("option_b"));
                    examEntity.setOption_c(obj.getString("option_c"));
                    examEntity.setOption_d(obj.getString("option_d"));
                    examEntity.setAnswer(obj.getString("answer"));
                    examEntity.setExplanation(obj.getString("explanation"));
                    examEntity.setSelectAnswer(-1);
                    examList.add(examEntity);
                }
                examEntity=examList.get(index);
                showQuiz(examEntity);
                addListener();
            }
        }
        catch (Exception e){
            DialogUtil.showDialog(this,"服务器响应异常，请稍后再试！",false);
            e.printStackTrace();
        }
    }

    private JSONObject queryRandomExam() throws Exception{
        Map<String,String> map=new HashMap<>();
        map.put("num",((Integer)randomExamNum).toString());
        String url= HttpUtil.BASE_URL+"/exam/randomExam";
        MapUrl mapUrl=new MapUrl();
        mapUrl.setUrl(url);
        mapUrl.setJsonString(map);
        HttpTask httpTask=new HttpTask();
        return httpTask.execute(mapUrl).get();
    }

    private List<Integer> checkAnswer(){
        List<Integer> wrongList=new ArrayList<Integer>();
        for (int i=0;i<examList.size();i++){
            char tmpAnswer=examList.get(i).getAnswer().charAt(0);
            int tmpSelectAnswer=examList.get(i).getSelectAnswer();
            if(('A'+tmpSelectAnswer)!=tmpAnswer){
                wrongList.add(i);
            }
        }
        return wrongList;
    }

    private void showQuiz(ExamEntity tmpExam){
        String tmp=((Integer)(index+1)).toString()+". "+tmpExam.getQuestion();
        String tmpExplanation="\n\n你选择的答案是："+(char)(tmpExam.getSelectAnswer()+'A')+
                "正确答案是："+String.valueOf(tmpExam.getAnswer())+"\n"+tmpExam.getExplanation();
        radioButtons[0].setText(tmpExam.getOption_a());
        radioButtons[1].setText(tmpExam.getOption_b());
        radioButtons[2].setText(tmpExam.getOption_c());
        radioButtons[3].setText(tmpExam.getOption_d());
        question.setText(tmp);
        explanation.setText(tmpExplanation);
       // int answer=examEntity.getAnswer().indexOf(0)-'A';
      //  radioButtons[answer].setChecked(true);
    }

    private void addListener(){
        //下一题
        nextQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index<examList.size()-1){
                    index++;
                    examEntity=examList.get(index);
                    showQuiz(examEntity);
                    radioGroup.clearCheck();
                    if(examEntity.getSelectAnswer()!=-1){
                        radioButtons[examEntity.getSelectAnswer()].setChecked(true);
                    }
                }
                else if(index==examList.size()-1) {//最后一题
                    final List<Integer> wrongList = checkAnswer();
                    if (wrongList.size() < 1) {
                        new AlertDialog.Builder(ExamShow.this).setTitle("提示").setMessage("恭喜你全部回答正确！")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ExamShow.this.finish();
                                    }
                                }).show();
                    }
                    else if((index==examList.size()-1)&&(wrongMode==true)){
                        new AlertDialog.Builder(ExamShow.this).setTitle("提示")
                                .setMessage("已经到达最后一题，是否退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExamShow.this.finish();
                            }
                        }).setNegativeButton("取消",null).show();
                    }
                    else {
                        new AlertDialog.Builder(ExamShow.this).setTitle("提示")
                                .setMessage("您答对了"+(examList.size()-wrongList.size())+"道题目，答错了"
                                +wrongList.size()+"道题目。是否查看错题？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                wrongMode=true;
                                List<ExamEntity> newList=new ArrayList<ExamEntity>();
                                for(int i=0;i<wrongList.size();i++){
                                    newList.add(examList.get(wrongList.get(i)));
                                }
                                examList.clear();
                                for(int i=0;i<newList.size();i++){
                                    examList.add(newList.get(i));
                                }
                                index=0;
                                examEntity=examList.get(index);
                                showQuiz(examEntity);
                                explanation.setVisibility(View.VISIBLE);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ExamShow.this.finish();
                            }
                        }).show();
                    }
                }
            }
        });

        //上一题
        lastQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index>0){
                    index--;
                    examEntity=examList.get(index);
                    showQuiz(examEntity);
                    radioGroup.clearCheck();
                    if(examEntity.getSelectAnswer()!=-1){
                        radioButtons[examEntity.getSelectAnswer()].setChecked(true);
                    }
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for(int i=0;i<4;i++){
                    if(radioButtons[i].isChecked()==true){
                        examList.get(index).setSelectAnswer(i);
                        break;
                    }
                }
            }
        });
    }
}
