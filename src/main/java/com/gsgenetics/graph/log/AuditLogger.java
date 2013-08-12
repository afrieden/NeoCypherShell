package com.gsgenetics.graph.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created with IntelliJ IDEA.
 * User: afrieden
 * Date: 6/27/13
 * Time: 8:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class AuditLogger {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
    static public void setup() throws IOException, ParseException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.INFO);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        java.util.Date date = new java.util.Date();
//        System.out.println("Current Date : " + dateFormat.format(date));
        fileTxt = new FileHandler("/sandbox/logs/Logging" + dateFormat.format(date) + ".txt");
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);
    }
}
