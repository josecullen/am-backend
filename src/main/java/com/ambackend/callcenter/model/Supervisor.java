package com.ambackend.callcenter.model;

import org.apache.log4j.Logger;

/**
 * Created by josecullen on 11/07/17.
 */
public class Supervisor implements Employee {
    static Logger logger = Logger.getLogger(Supervisor.class);
    private static int idCount = 0;
    private int id;

    public Supervisor(){
        id = idCount++;
    }

    @Override
    public void answerCall() {
        logger.info("Hola, soy el supervisor " + id);
    }

    public int attendPriority(){
        return Employee.SUPERVISOR;
    }

    public int getId(){
        return id;
    }
}
