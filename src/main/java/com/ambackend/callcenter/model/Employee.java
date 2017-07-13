package com.ambackend.callcenter.model;

/**
 * Created by josecullen on 11/07/17.
 */
public interface Employee {
    int OPERATOR = 1;
    int SUPERVISOR = 2;
    int DIRECTOR = 3;
    void answerCall();
    int attendPriority();
    int getId();
}
