package com.gsgenetics.graph.utility;

import com.gsgenetics.graph.log.QueryLogger;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: afrieden
 * Date: 7/1/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseCypherEngine {
    QueryLogger queryLogger = null;
    public WarehouseCypherEngine(String MyPath)
    {
        try {
            queryLogger = new QueryLogger(MyPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void RunQuery(String _query, FileInputStream fileInputStream)
    {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("/change/me/now/prodConfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        final String DB_PATH = prop.getProperty("DB_PATH");
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        ExecutionEngine engine = new ExecutionEngine(graphDb);
        ExecutionResult result = engine.execute(_query);

        String myResult = result.dumpToString();
        String[] LinesOfResults = myResult.split("\n");
        for(String line: LinesOfResults)
        {
            System.out.println(line);
        }

        queryLogger.log(_query);


        graphDb.shutdown();


    }
    public void RunQuery(String _query)
    {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("/sandbox/afrieden/MyTools/EmbeddedGraphPlatform/prodConfig.properties"));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        final String DB_PATH = prop.getProperty("DB_PATH");
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        ExecutionEngine engine = new ExecutionEngine(graphDb);
        ExecutionResult result = engine.execute(_query);

        //    System.out.println(result.dumpToString());
        String myResult = result.dumpToString();
        String[] LinesOfResults = myResult.split("\n");
        for(String line: LinesOfResults)
        {
            System.out.println(line);
        }

        //log query
        queryLogger.log(_query);


        graphDb.shutdown();


    }

}
