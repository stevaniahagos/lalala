package com.datalex.controller;

/* *
 * Application's starting point
 * */
public class FileReaderWriter
{

    static Operations ops = new Operations();

    public static void main(String[] args) throws Exception
    {
        ops.readFile("com/datalex/initial/data/initPersons.csv");

        ops.displayOptions();
    }
}
