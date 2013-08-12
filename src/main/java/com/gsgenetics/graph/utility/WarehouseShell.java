package com.gsgenetics.graph.utility;

import com.gsgenetics.graph.utility.WarehouseCypherEngine;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: afrieden
 * Date: 7/1/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class WarehouseShell {



        public static class JavaStringHistory
        {
            private List<String> history = new ArrayList<String>();
        }

        public static void main(String[] args) throws java.io.IOException {
            JavaStringHistory javaStringHistory = new JavaStringHistory();
            javaStringHistory.history.add("");

            Integer indexOfHistory = 0;


            String commandLine;
            BufferedReader console = new BufferedReader
                    (new InputStreamReader(System.in));


            WarehouseCypherEngine warehouseCypherEngine = new WarehouseCypherEngine();
            //Break with Ctrl+C
            System.out.println("Welcome to Good Start Genetics Cypher Shell");
            try
            {
                while (true) {


                    System.out.print("Shell>");
                    commandLine = console.readLine();
                    //javaStringHistory.history.add(commandLine);

                    //if just a return, loop
                    if (commandLine.equals(""))
                        continue;
                    //history

                    //help command
                    else if (commandLine.equals("help"))
                    {
                        System.out.println();
                        System.out.println();
                        System.out.println("Welcome to the shell");
                        System.out.println("--------------------");
                        System.out.println();
                        System.out.println("Commands to use:");
                        System.out.println("1) cat");
                        System.out.println("2) exit");
                        System.out.println("3) clear");
                        System.out.println();
                        System.out.println();
                        System.out.println("---------------------");
                        System.out.println();
                    }

                    else if (commandLine.equals("clear"))
                    {

                        for(int cls = 0; cls < 20; cls++ )
                        {
                            System.out.println();
                        }


                    }




                    else if(commandLine.startsWith("cat"))
                    {
                        System.out.println("test");
                        //ProcessBuilder pb = new ProcessBuilder();
                        //pb = new ProcessBuilder(commandLine);
                    }




                    else if (commandLine.equals("exit"))
                    {

                        System.out.println("...Terminating the Virtual Machine");
                        System.out.println("...Done");
                        System.out.println("Please Close manually with Options > Close");
                        System.exit(0);
                    }
                    else
                    {
    //                    System.out.println("Incorrect Command");
                        //com.gsgenetics.graph.structure.Run Cypher Query

                        warehouseCypherEngine.RunQuery(commandLine.toString());


                    }

                    indexOfHistory++;




                }
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }
    }

