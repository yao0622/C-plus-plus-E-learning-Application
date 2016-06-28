package com.example.yaofa.client.model;

/**
 * Created by yaofa on 2016/5/9.
 */
public class ExamEntity {
    private int lesson_id;//所属节id

    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;//题目类型：1为选择题；2为填空题；3为简答题

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question; //问题 用@@符号作为分隔符

    public String getOption_a() {
        return option_a;
    }

    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }

    private String option_a;

    public String getOption_b() {
        return option_b;
    }

    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }

    private String option_b;

    public String getOption_c() {
        return option_c;
    }

    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }

    private String option_c;

    public String getOption_d() {
        return option_d;
    }

    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }

    private String option_d;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;  //答案

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    private String explanation; //解析

    public int getSelectAnswer() {
        return selectAnswer;
    }

    public void setSelectAnswer(int selectAnswer) {
        this.selectAnswer = selectAnswer;
    }

    private int selectAnswer;//用户选择的答案

    public ExamEntity(){
        lesson_id=-1;
        type=-1;
        question=null;
        option_a=null;
        option_b=null;
        option_c=null;
        option_d=null;
        answer=null;
        explanation=null;
        selectAnswer=-1;
    }

    public ExamEntity(int lesson_id,int type, String question,String option_a,
                      String option_b,String option_c,String option_d,String answer,String explanation,int selectAnswer){
        this.lesson_id=lesson_id;
        this.type=type;
        this.question=question;
        this.option_a=option_a;
        this.option_b=option_b;
        this.option_c=option_c;
        this.option_d=option_d;
        this.answer=answer;
        this.explanation=explanation;
        this.selectAnswer=selectAnswer;
    }

}
