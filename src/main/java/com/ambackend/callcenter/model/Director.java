package com.ambackend.callcenter.model;


import org.apache.log4j.Logger;

/**
 * Created by josecullen on 11/07/17.
 */
public class Director implements Employee {
    static Logger logger = Logger.getLogger(Director.class);
    private static int idCount = 0;
    private int id;

    public Director(){
        id = idCount++;
    }

    public void answerCall() {
        logger.info("Hola, soy el director " + id);
    }

    public int attendPriority(){
        return Employee.DIRECTOR;
    }

    public int getId(){
        return id;
    }
}
