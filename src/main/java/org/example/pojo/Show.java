package org.example.pojo;

import lombok.Data;

@Data
public class Show {
    private int todayFlow;
    private float dodFlow;
    private float yoyFlow;

    private int todayVisit;
    private float dodVisit;
    private float yoyVisit;

    private float todayStayTime;
    private float dodStayTime;
    private float yoyStayTime;

    private float todayInvalidFlow;
    private float dodInvalidFlow;
    private float yoyInvalidFlow;

    private float todayDeepUser;
    private float dodDeepUser;
    private float yoyDeepUser;
    public Show(int todayFlow,float dodFlow,float yoyFlow,int todayVisit,float dodVisit,
                float yoyVisit,float todayStayTime,float dodStayTime,float yoyStayTime,
                float todayInvalidFlow,float dodInvalidFlow,float yoyInvalidFlow,
                float todayDeepUser,float dodDeepUser,float yoyDeepUser) {
        this.todayFlow = todayFlow;
        this.dodFlow = dodFlow;
        this.yoyFlow = yoyFlow;
        this.todayVisit = todayVisit;
        this.dodVisit = dodVisit;
        this.yoyVisit = yoyVisit;
        this.todayStayTime = todayStayTime;
        this.dodStayTime = dodStayTime;
        this.yoyStayTime = yoyStayTime;
        this.todayInvalidFlow = todayInvalidFlow;
        this.dodInvalidFlow = dodInvalidFlow;
        this.yoyInvalidFlow = yoyInvalidFlow;
        this.todayDeepUser = todayDeepUser;
        this.dodDeepUser = dodDeepUser;
        this.yoyDeepUser = yoyDeepUser;

    }
}
