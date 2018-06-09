package de.mankianer.netscan.PingService.logic;

import de.mankianer.netscan.util.Ping;
import lombok.Getter;
import lombok.Singular;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;


@Component
public class IpTracker {

    private static HashMap<String, List<Map.Entry<Integer, Date>>> cache = new HashMap<>();

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

    public void add(String ip)
    {
        cache.put(ip, new ArrayList<>());
    }

    public void remove(String ip)
    {
        cache.remove(ip, new ArrayList<>());
    }

    public HashMap<String, List<Map.Entry<Integer, Date>>> getCache()
    {
        return cache;
    }
}
