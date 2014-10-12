package andrews.happyfarm.rooms;

import andrews.jengine.Room;

import static andrews.jengine.DB.db;

/**
 * @author STALKER_2010
 */
public class GameRoom extends Room {
    public GameRoom() {
        super();
    }
    public GameRoom(String name) {
        super(name);
        background = "game_bg";
        objectsIDs.add("player");
    }
}
