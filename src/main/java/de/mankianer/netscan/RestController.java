package de.mankianer.netscan;

import de.mankianer.netscan.util.CMD;
import de.mankianer.netscan.util.Ping;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private HashMap<String, List<Map.Entry<Integer, Date>>> cache;

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

    public void remove(String ip)
    {
        cache.remove(ip, new ArrayList<>());
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

    @RequestMapping("/addIp/{ip}")
    public String addIp(@PathVariable("ip") String ip) {
        add(ip);
        return "ok";
    }

    @RequestMapping("/removeIp/{ip}")
    public String removeIp(@PathVariable("ip") String ip) {
        remove(ip);
        return "ok";
    }

    @RequestMapping("/getPings")
    public HashMap<String, List<Map.Entry<Integer, Date>>> getPings() {

        return cache;
    }

    @Scheduled(fixedRate = 1000)
    public void ping()
    {
        try {
            for (Map.Entry<String, List<Map.Entry<Integer, Date>>> entry : cache.entrySet()) {
                long time = System.currentTimeMillis();
                Date date = new Date();
                entry.getValue().add(new AbstractMap.SimpleEntry<>(Ping.ping(entry.getKey()),date));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
