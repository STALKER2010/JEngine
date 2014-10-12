package andrews.jengine;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DB {
	public Map<String, GameObject> objects = new ConcurrentHashMap<>();
	public Map<String, Background> backgrounds = new ConcurrentHashMap<>();
	public Map<String, Room> rooms = new ConcurrentHashMap<>();
	public static final DB db;
	static {
		db = new DB();
	}

    @JsonIgnore
	public void onGameLoaded(final Game game) {
	}

    @JsonIgnore
    public String getFreeName(String prefix) {
        int i = 0;
        while (objects.containsKey(prefix + i)) {
            i++;
        }
        return prefix + i;
    }
}
