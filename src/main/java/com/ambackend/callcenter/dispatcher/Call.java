package com.ambackend.callcenter.dispatcher;

import com.ambackend.callcenter.model.Employee;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created by josecullen on 11/07/17.
 * Represent a call between the client and an Employee
 */
public class Call {
    static Logger logger = Logger.getLogger(Call.class);
    private static int callCount = 0;
    private int id;
    private Employee attender;

    public Call() {
        id = callCount++;
    }

    void setAttender(Employee attender) {
        this.attender = attender;
    }

    public int getId() {
        return id;
    }

    /**
     * Represents the call per se
     * @return the Employee who's attended the call
     */
    public Employee resolveCall() {
        logger.info("call "+ id);
        attender.answerCall();
        try {
            TimeUnit.SECONDS.sleep((long) (Math.random()*5 + 5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("call "+ id +" ended. Attender "+attender.getId() + " priority " + attender.attendPriority());
        return attender;
    }

}
