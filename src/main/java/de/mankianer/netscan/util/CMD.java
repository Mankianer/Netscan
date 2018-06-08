package de.mankianer.netscan.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CMD {

    public static String[] doCommand(List<String> command) throws IOException
    {
        ArrayList<String> ret = new ArrayList<>();

        String s = null;

        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        // read the output from the command
//        ret.add("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null)
        {
            ret.add(s);
        }

        // read any errors from the attempted command
//        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null)
        {
            ret.add("Error" + s);
        }

        return ret.toArray(new String[0]);
    }
}
