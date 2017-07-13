package com.ambackend;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class App {
    static Logger logger = Logger.getLogger(App.class);
    public static void main( String[] args ) {
        BasicConfigurator.configure();
        logger.info("Me sobró el main :P");
    }
}


