package com.example.studentadmin.Model;

public class QuestionModel {
    private String id, quastion, A, B, C, D, answer;



    public QuestionModel(String id, String quastion, String a, String b, String c, String d, String answer) {
        this.id = id;
        this.quastion = quastion;
        A = a;
        B = b;
        C = c;
        D = d;
        this.answer = answer;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuastion() {
        return quastion;
    }

    public void setQuastion(String quastion) {
        this.quastion = quastion;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


}
