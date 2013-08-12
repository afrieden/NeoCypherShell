package com.gsgenetics.graph.log;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

/**
 * Created with IntelliJ IDEA.
 * User: afrieden
 * Date: 7/2/13
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
    static private java.util.logging.Logger logger = null;
    static private StringBuilder userName = new StringBuilder();
    public QueryLogger(String pathToLog) throws IOException {
        logger = java.util.logging.Logger.getLogger(java.util.logging.Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler(pathToLog + "/neo4j.log");
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
        System.out.println(System.getProperty("user.name"));
        userName = new StringBuilder();
        userName.append(System.getProperty("user.name"));

    }

    public void log(String _query)
    {
        Date date = new Date();
        logger.info(date.toString() + "\t" + userName.toString() + "\t" +  _query);
    }

}
