package com.netas.cpaas;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class HazelCastMapProvider {

    private static String nvsTokenMapName = "nvsTokenMap";

    private static String applicationTokenMapName = "appTokenMap";

    private final HazelcastInstance hazelcastInstance;

    public IMap<String, Object> getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    public void putToMap(String mapName, String key, Object value) {
        hazelcastInstance.getMap(mapName).put(key, value);
    }

    public Object getValue(String mapName, String key) {
        return hazelcastInstance.getMap(mapName).get(key);
    }

    public static String getNvsTokenMapName() {
        return nvsTokenMapName;
    }

    public static String getApplicationTokenMapName() {
        return applicationTokenMapName;
    }
}
