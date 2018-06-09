package de.mankianer.netscan.PingService.rest;

import de.mankianer.netscan.PingService.logic.IpTracker;
import de.mankianer.netscan.util.Ping;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

@EnableScheduling
@Component
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    IpTracker ipTracker;

    @PostConstruct
    public void intit()
    {
        ipTracker.add("192.168.178.184");
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
        ipTracker.add(ip);
        return "ok";
    }

    @RequestMapping("/removeIp/{ip}")
    public String removeIp(@PathVariable("ip") String ip) {
        ipTracker.remove(ip);
        return "ok";
    }

    @RequestMapping("/getPings")
    public HashMap<String, List<Map.Entry<Integer, Date>>> getPings() {

        return ipTracker.getCache();
    }

    @Scheduled(fixedRate = 1000)
    public void ping()
    {
        ipTracker.ping();
    }
}
