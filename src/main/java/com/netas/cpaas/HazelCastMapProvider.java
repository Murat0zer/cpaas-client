package com.netas.cpaas;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class HazelCastMapProvider {

    private final HazelcastInstance hazelcastInstance;

    private static Map<String, WebSocketSession> webSocketMap = new ConcurrentHashMap<>();

    public IMap<String, Object> getMap(String mapName) {
        return hazelcastInstance.getMap(mapName);
    }

    public void putToMap(String mapName, String key, Object value) {
        hazelcastInstance.getMap(mapName).put(key, value);
    }

    public static Map<String, WebSocketSession> getWebSocketMap() {
        return webSocketMap;
    }

    public Object getValue(String mapName, String key) {
        return hazelcastInstance.getMap(mapName).get(key);
    }


    public static class MapNames {
        public static final String NVS_TOKEN = "nvsTokenMap";
        public static final String APPLICATION_TOKEN = "appTokenMap";
        public static final String NOTIFICATION_CHANNELS = "notificationChannels";
        public static final String WEBSOCKET_SESSION = "webSocketSessions";
        private MapNames() {
        }
    }
}
