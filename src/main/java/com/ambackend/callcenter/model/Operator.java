package com.ambackend.callcenter.model;

import org.apache.log4j.Logger;

/**
 * Created by josecullen on 11/07/17.
 */
public class Operator implements Employee {
    static Logger logger = Logger.getLogger(Operator.class);
    private static int idCount = 0;
    private int id;

    public Operator(){
        id = idCount++;
    }

    @Override
    public void answerCall() {
        logger.info("Hola, soy el operador " + id);
    }

    public int attendPriority(){
        return Employee.OPERATOR;
    }

    public int getId(){
        return id;
    }
}

