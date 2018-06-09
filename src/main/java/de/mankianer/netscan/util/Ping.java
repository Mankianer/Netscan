package de.mankianer.netscan.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ping {

    /**
     *
     * @param ip
     * @return -1 = nicht ereichbar
     */
    public static int ping(String ip) throws IOException {
        List<String> commands = new ArrayList<String>();
        commands.add("ping");
        commands.add("-n");
        commands.add("1");
        commands.add("192.168.178.184");

        return CmdToPing(Cmd.doCommand(commands));
    }

    private static int CmdToPing(String[] outPut)
    {
        for (String line : outPut) {
            if(line.contains("Zeit="))
            {
                String ms = line.split("Zeit=")[1].split("ms")[0];
                System.out.println("ms: " + ms);
                return Integer.valueOf(ms);

            }
        }

        return -1;
    }
}
