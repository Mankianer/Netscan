package de.mankianer.netscan;

import de.mankianer.netscan.util.CMD;
import de.mankianer.netscan.util.Ping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.*;

@EnableScheduling
@Component
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private HashMap<String, List<Integer>> cache;

    @PostConstruct
    public void intit()
    {
        cache = new HashMap<>();
        add("192.168.178.184");
    }

    public void add(String ip)
    {
        cache.put(ip, new ArrayList<>());
    }


    @RequestMapping("/ping")
    public String greeting(@RequestParam(value="ip", defaultValue="localhost") String ip) {
        String ret = ip;
        try {
            ret += " " + InetAddress.getByName(ip).isReachable(100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @RequestMapping("/getPings")
    public HashMap<String, List<Integer>> getPings() {

        return cache;
    }

    @Scheduled(fixedRate = 1000)
    public void ping()
    {
        try {
            for (Map.Entry<String, List<Integer>> entry : cache.entrySet()) {
                entry.getValue().add(Ping.ping(entry.getKey()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
